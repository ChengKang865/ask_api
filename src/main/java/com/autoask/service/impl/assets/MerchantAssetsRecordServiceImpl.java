package com.autoask.service.impl.assets;

import com.autoask.common.exception.ApiException;
import com.autoask.common.phone.MessageService;
import com.autoask.common.util.*;
import com.autoask.entity.mongo.merchant.BaseMerchant;
import com.autoask.entity.mongo.merchant.Outlets;
import com.autoask.entity.mysql.*;
import com.autoask.mapper.*;
import com.autoask.pay.pingpp.PingppTransferUtil;
import com.autoask.pay.pingpp.config.PingppConfig;
import com.autoask.service.assets.MerchantAssetsRecordService;
import com.autoask.service.log.PayLogService;
import com.autoask.service.merchant.MerchantService;
import com.autoask.service.merchant.OutletsService;
import com.pingplusplus.model.Transfer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author hyy
 * @create 16/11/6 17:22
 */
@Service
public class MerchantAssetsRecordServiceImpl implements MerchantAssetsRecordService {

    @Autowired
    private MerchantAssetsRecordMapper merchantAssetsRecordMapper;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderShareMapper orderShareMapper;

    @Autowired
    private AssetsRecordItemMapper assetsRecordItemMapper;

    @Autowired
    private MerchantAssetsMapper merchantAssetsMapper;

    @Autowired
    private OutletsService outletsService;

    @Autowired
    private PayLogService payLogService;

    @Override
    public ListSlice selectMerchantAssetsRecordList(String merchantType, String factoryId, String serviceProviderId, String outletsId, String mechanicId,
                                                    String orderId, String startTime, String endTime,
                                                    int start, int limit) throws ApiException {
        //TODO
        List<String> merchantIds = null;
        Long totalNum = merchantAssetsRecordMapper.countMerchantAssetsRecord(merchantType, merchantIds, orderId, startTime, endTime);
        List<MerchantAssetsRecord> merchantAssetsRecords = merchantAssetsRecordMapper.selectMerchantAssetsRecordList(merchantType, merchantIds, orderId, startTime, endTime, start, limit);
        if (CollectionUtils.isNotEmpty(merchantAssetsRecords)) {
            for (MerchantAssetsRecord merchantAssetsRecord : merchantAssetsRecords) {
                BaseMerchant merchantInfo = merchantService.getMerchantInfo(merchantAssetsRecord.getMerchantType(), merchantAssetsRecord.getMerchantId());
                if (null != merchantInfo) {
                    merchantAssetsRecord.setMerchantName(merchantInfo.getName());
                }
            }
        }
        return new ListSlice(merchantAssetsRecords, totalNum);
    }

    @Override
    public void updateMerchantAssetsByOrder(String orderId) throws ApiException {
        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        if (null == orderInfo) {
            throw new ApiException("订单不存在");
        }

        OrderShare orderShare = orderShareMapper.getOrderShareByOrderId(orderId);
        if (null == orderShare) {
            throw new ApiException("订单异常");
        }

        Map<String, MerchantShare> merchantShareMap = new HashMap<>();
        List<AssetsRecordItem> assetsRecordItemList = new ArrayList<>();

        initPromoteFee(orderShare, merchantShareMap, assetsRecordItemList);
        initAdFee(orderShare, merchantShareMap, assetsRecordItemList);
        initServiceFee(orderShare, merchantShareMap, assetsRecordItemList);
        initFactoryFee(orderShare, merchantShareMap, assetsRecordItemList);

        Map<String, MerchantAssetsRecord> recordMap = new HashMap<>(merchantShareMap.size());
        //重新计算对应的recordId
        reInitRecordId(orderId, merchantShareMap, assetsRecordItemList, recordMap);
        //插入数据
        List<MerchantAssetsRecord> merchantAssetsRecordList = new ArrayList<>(recordMap.size());
        merchantAssetsRecordList.addAll(recordMap.values());
        merchantAssetsRecordMapper.insertList(merchantAssetsRecordList);
        assetsRecordItemMapper.insertList(assetsRecordItemList);

        //更新商户的资产
        for (MerchantAssetsRecord merchantAssetsRecord : merchantAssetsRecordList) {
            String merchantType = merchantAssetsRecord.getMerchantType();
            String merchantId = merchantAssetsRecord.getMerchantId();
            BigDecimal amount = merchantAssetsRecord.getChangeAmount();
            //会计入总收入
            merchantAssetsMapper.incomeBalance(merchantType, merchantId, amount);
            // 特殊处理 分销店的费用，需要此时通过微信转账转到商户的资产中
            //更新商户的收入资产 需要累加总资产
            if (StringUtils.equals(merchantType, Constants.MerchantType.OUTLETS)) {
                String outletsId = merchantId;
                Outlets outlets = outletsService.findById(outletsId);
                //分销点存在且已经激活的状态下
                if (null != outlets && outlets.getActivated()) {
                    //向分销店发送信息 转告分销点 已经收到分成   当前余额度
                    MerchantAssets assets = merchantAssetsMapper.selectForLock(Constants.MerchantType.OUTLETS, outletsId);
                    if (amount.compareTo(new BigDecimal(0)) == 0) {
                        return;
                    }
                    MessageService.sendOutletsShareMessage(outlets.getPhone(), amount, assets.getBalance(), Constants.INCOME_REFUND_TIME);
                }
            }

        }
    }

    private void reInitRecordId(String orderId, Map<String, MerchantShare> merchantShareMap, List<AssetsRecordItem> assetsRecordItemList, Map<String, MerchantAssetsRecord> recordMap) {
        for (Map.Entry<String, MerchantShare> itemEntry : merchantShareMap.entrySet()) {
            MerchantAssetsRecord merchantAssetsRecord = new MerchantAssetsRecord();

            merchantAssetsRecord.setRecordId(CodeGenerator.uuid());
            merchantAssetsRecord.setMerchantType(itemEntry.getValue().getMerchantType());
            merchantAssetsRecord.setMerchantId(itemEntry.getValue().getMerchantId());
            merchantAssetsRecord.setChangeAmount(itemEntry.getValue().getTotalShare());
            merchantAssetsRecord.setRelatedType(Constants.RecordRelatedType.ORDER);
            merchantAssetsRecord.setRelatedId(orderId);
            merchantAssetsRecord.setCreateTime(DateUtil.getDate());

            recordMap.put(itemEntry.getKey(), merchantAssetsRecord);
        }
        for (AssetsRecordItem assetsRecordItem : assetsRecordItemList) {
            String key = assetsRecordItem.getRecordId();
            MerchantAssetsRecord record = recordMap.get(key);
            assetsRecordItem.setRecordId(record.getRecordId());
        }
    }

    private void initPromoteFee(OrderShare orderShare, Map<String, MerchantShare> merchantShareMap, List<AssetsRecordItem> assetsRecordItemList) {
        String promoteType = orderShare.getPromoteType();
        String promoteId = orderShare.getPromoteId();
        String joinId = promoteType + promoteId;
        BigDecimal promoteFee = orderShare.getPromoteFee();
        if (StringUtils.isNotEmpty(promoteType) && !StringUtils.equals(promoteType, Constants.MerchantType.AUTOASK)) {
            merchantShareMap.put(joinId, new MerchantShare(promoteType, promoteId, promoteFee));
            AssetsRecordItem assetsRecordItem = new AssetsRecordItem(CodeGenerator.uuid(), joinId, promoteFee, Constants.IncomeType.PROMOTE_FEE);
            assetsRecordItemList.add(assetsRecordItem);
        }
    }

    private void initAdFee(OrderShare orderShare, Map<String, MerchantShare> merchantShareMap, List<AssetsRecordItem> assetsRecordItemList) {
        String adType = orderShare.getAdType();
        String adId = orderShare.getAdId();
        String joinId = adType + adId;
        BigDecimal adFee = orderShare.getAdFee();
        if (StringUtils.isNotEmpty(adType) && !StringUtils.equals(adType, Constants.MerchantType.AUTOASK)) {
            MerchantShare pre = merchantShareMap.get(joinId);
            if (null != pre) {
                pre.setTotalShare(pre.getTotalShare().add(adFee));
            } else {
                pre = new MerchantShare(adType, adId, adFee);
            }
            merchantShareMap.put(joinId, pre);
            AssetsRecordItem assetsRecordItem = new AssetsRecordItem(CodeGenerator.uuid(), joinId, adFee, Constants.IncomeType.AD_FEE);
            assetsRecordItemList.add(assetsRecordItem);
        }
    }

    private void initServiceFee(OrderShare orderShare, Map<String, MerchantShare> merchantShareMap, List<AssetsRecordItem> assetsRecordItemList) {
        String serviceType = orderShare.getServiceType();
        String serviceId = orderShare.getServiceId();
        String joinId = serviceType + serviceId;
        BigDecimal serviceFee = orderShare.getServiceFee();
        if (StringUtils.isNotEmpty(serviceType) && !StringUtils.equals(serviceType, Constants.MerchantType.AUTOASK)) {
            MerchantShare pre = merchantShareMap.get(joinId);
            if (null != pre) {
                pre.setTotalShare(pre.getTotalShare().add(serviceFee));
            } else {
                pre = new MerchantShare(serviceType, serviceId, serviceFee);
            }
            merchantShareMap.put(joinId, pre);
            AssetsRecordItem assetsRecordItem = new AssetsRecordItem(CodeGenerator.uuid(), joinId, serviceFee, Constants.IncomeType.SERVICE_FEE);
            assetsRecordItemList.add(assetsRecordItem);
        }
    }

    private void initFactoryFee(OrderShare orderShare, Map<String, MerchantShare> merchantShareMap, List<AssetsRecordItem> assetsRecordItemList) {
        String factoryId = orderShare.getFactoryId();
        BigDecimal factoryFee = orderShare.getFactoryFee();
        String joinId = Constants.MerchantType.FACTORY + factoryId;
        if (StringUtils.isNotEmpty(factoryId)) {
            MerchantShare pre = merchantShareMap.get(joinId);
            if (null != pre) {
                pre.setTotalShare(pre.getTotalShare().add(factoryFee));
            } else {
                pre = new MerchantShare(Constants.MerchantType.FACTORY, factoryId, factoryFee);
            }
            merchantShareMap.put(joinId, pre);

            AssetsRecordItem assetsRecordItem = new AssetsRecordItem(CodeGenerator.uuid(), joinId, factoryFee, Constants.IncomeType.FACTORY_FEE);
            assetsRecordItemList.add(assetsRecordItem);
        }
    }

    private static class MerchantShare {
        String merchantType;
        String merchantId;
        BigDecimal totalShare;

        public MerchantShare() {

        }

        public MerchantShare(String merchantType, String merchantId, BigDecimal totalShare) {
            this.merchantType = merchantType;
            this.merchantId = merchantId;
            this.totalShare = totalShare;
        }

        public String getMerchantType() {
            return merchantType;
        }

        public void setMerchantType(String merchantType) {
            this.merchantType = merchantType;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public BigDecimal getTotalShare() {
            return totalShare;
        }

        public void setTotalShare(BigDecimal totalShare) {
            this.totalShare = totalShare;
        }
    }


    @Override
    public BigDecimal countIncome(String merchantType, String merchantId, Date startTime, Date endTime) throws ApiException {
        return merchantAssetsRecordMapper.countIncome(merchantType, merchantId, startTime, endTime);
    }
}
