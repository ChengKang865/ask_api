package com.autoask.service.impl.merchant;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.exception.ApiException;
import com.autoask.common.phone.MessageService;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.entity.mongo.merchant.BaseMerchant;
import com.autoask.entity.mysql.MerchantAssets;
import com.autoask.entity.mysql.MerchantBonusRecord;
import com.autoask.mapper.MerchantAssetsMapper;
import com.autoask.mapper.MerchantBonusRecordMapper;
import com.autoask.service.log.PayLogService;
import com.autoask.service.merchant.MerchantAssetsService;
import com.autoask.service.merchant.MerchantService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by weid on 16-9-27.
 */
@Service("merchantAssetsService")
public class MerchantAssetsServiceImpl implements MerchantAssetsService {

    private static final Logger LOG = LoggerFactory.getLogger(MerchantAssetsServiceImpl.class);

    @Autowired
    private MerchantAssetsMapper merchantAssetsMapper;

    @Autowired
    private MerchantBonusRecordMapper merchantBonusRecordMapper;

    @Autowired
    private PayLogService payLogService;

    @Autowired
    private MerchantService merchantService;

    @Override
    public MerchantAssets getMerchantAssets(String merchantType, String merchantId) {
        Example example = new Example(MerchantAssets.class);
        example.createCriteria().andEqualTo("merchantType", merchantType).andEqualTo("merchantId", merchantId);
        List<MerchantAssets> merchantAssetsList = merchantAssetsMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(merchantAssetsList)) {
            return merchantAssetsList.get(0);
        }
        return null;
    }

    @Override
    public void addMerchantAssets(String merchantType, String merchantId) throws ApiException {
        // 重复检测
        Example example = new Example(MerchantAssets.class);
        example.createCriteria().andEqualTo("merchantType", merchantType)
                .andEqualTo("merchantId", merchantId);

        List<MerchantAssets> merchantAssetsList = merchantAssetsMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(merchantAssetsList)) {
            throw new ApiException("存在重复的记录");
        }

        // 创建记录
        MerchantAssets merchantAssets = new MerchantAssets();
        merchantAssets.setMerchantType(merchantType);
        merchantAssets.setMerchantId(merchantId);
        merchantAssets.setCreateTime(DateUtil.getDate());
        merchantAssets.setBalance(new BigDecimal(0.0));
        merchantAssets.setIncomeAmount(new BigDecimal(0.0));
        merchantAssets.setVersion(0L);
        merchantAssetsMapper.insert(merchantAssets);
    }

    @Override
    public void updateMerchantBonusRecord(JSONObject json) throws ApiException {
        payLogService.savePayLog(Constants.PayLogType.PINGPP_BONUS_CALLBACK, json);

        JSONObject refundObj = json.getJSONObject("data").getJSONObject("object");
        String transferId = refundObj.getString("id");
//        BigDecimal amount = refundObj.getBigDecimal("amount");

        MerchantBonusRecord merchantBonusRecord = findMerchantBonusRecord(transferId);
        MerchantAssets merchantAssets = findMerchantAssets(merchantBonusRecord);
        BigDecimal amount = merchantBonusRecord.getChangeAmount();

        MerchantBonusRecord bonusUpdateParam = new MerchantBonusRecord();
        bonusUpdateParam.setId(merchantBonusRecord.getId());
        bonusUpdateParam.setStatus(Constants.MerchantBonusStatus.SUCCESS);
        merchantBonusRecordMapper.updateByPrimaryKeySelective(bonusUpdateParam);

//        //更新商户的资产
//        merchantAssetsMapper.updateBalance(merchantAssets.getMerchantType(), merchantAssets.getMerchantId(),
//                amount.multiply(BigDecimal.valueOf(-1)));

        //发送短信
        BaseMerchant merchantInfo = merchantService.getMerchantInfo(merchantAssets.getMerchantType(), merchantAssets.getMerchantId());
        MessageService.sendIncomeMessage(merchantInfo.getPhone(), amount);
    }

    /**
     * 获取商户的资产信息
     *
     * @param merchantBonusRecord
     * @return
     * @throws ApiException
     */
    private MerchantAssets findMerchantAssets(MerchantBonusRecord merchantBonusRecord) throws ApiException {
        //获取商户的资产信息
        MerchantAssets assetsParam = new MerchantAssets();
        assetsParam.setMerchantId(merchantBonusRecord.getMerchantId());
        assetsParam.setMerchantType(merchantBonusRecord.getMerchantType());
        MerchantAssets merchantAssets = merchantAssetsMapper.selectOne(assetsParam);
        if (merchantAssets == null) {
            LOG.error("Failed to get merchant asset, merchantId:{}, merchantType:{}",
                    merchantBonusRecord.getMerchantId(), merchantBonusRecord.getMerchantType());
            throw new ApiException("获取商户的资产信息失败");
        }
        return merchantAssets;
    }

    /**
     * 获取商户Ping++转账记录
     *
     * @param transferId
     * @return
     * @throws ApiException
     */
    private MerchantBonusRecord findMerchantBonusRecord(String transferId) throws ApiException {
        //获取发红包的记录
        MerchantBonusRecord param = new MerchantBonusRecord();
        param.setPingppTransferId(transferId);
        param.setStatus(Constants.MerchantBonusStatus.DOING);
        MerchantBonusRecord merchantBonusRecord = merchantBonusRecordMapper.selectOne(param);
        if (merchantBonusRecord == null) {
            LOG.error("Query merchantBonusRecord failure, pingppTransferId:{}", transferId);
            throw new ApiException("获取用户发红包记录失败");
        }
        return merchantBonusRecord;
    }
}
