package com.autoask.service.impl.product;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.store.GoodsNumRecordDao;
import com.autoask.entity.mongo.merchant.BaseMerchant;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.entity.mongo.store.GoodsNumRecord;
import com.autoask.entity.mysql.*;
import com.autoask.mapper.GoodsNumMapper;
import com.autoask.mapper.OrderInfoMapper;
import com.autoask.service.merchant.MerchantService;
import com.autoask.service.merchant.ServiceProviderService;
import com.autoask.service.product.GoodsNumService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author hyy
 * @create 2016-11-29 17:04
 */
@Service
public class GoodsNumServiceImpl implements GoodsNumService {
//
//    @Autowired
//    private GoodsNumMapper goodsNumMapper;
//
//    @Autowired
//    private GoodsNumRecordDao goodsNumRecordDao;
//
//    @Autowired
//    private ServiceProviderService serviceProviderService;
//
//
//    @Autowired
//    private MerchantService merchantService;
//
//
//    @Autowired
//    private OrderInfoMapper orderInfoMapper;
//
//
//    @Override
//    public Long getGoodsNum(String merchantType, String merchantId, String goodsId) throws ApiException {
//        Long num = goodsNumMapper.getGoodsNum(merchantType, merchantId, goodsId);
//        if (null == num) {
//            return 0L;
//        }
//        return num;
//    }
//
//    @Override
//    public ListSlice getSubMerchantGoodsNumList(String merchantType, String factoryId,
//                                                String productCategoryId, String productId, String goodsId, String goodsName,
//                                                int start, int limit) throws ApiException {
//        if (StringUtils.isEmpty(merchantType)) {
//            throw new ApiException("参数非法");
//        }
//
//        List<String> merchantIds = null;
//        if (StringUtils.isNotEmpty(factoryId)) {
//            merchantIds = Arrays.asList(factoryId);
//        }
//
//        Long totalNum = goodsNumMapper.countGoodsNumListSize(merchantType, merchantIds, productCategoryId, productId, goodsId, goodsName);
//        List<GoodsNum> goodsNumList = goodsNumMapper.getGoodsNumList(merchantType, merchantIds, productCategoryId, productId, goodsId, goodsName, start, limit);
//
//        if (CollectionUtils.isNotEmpty(goodsNumList)) {
//            for (GoodsNum goodsNum : goodsNumList) {
//                BaseMerchant merchantInfo = merchantService.getMerchantInfo(goodsNum.getMerchantType(), goodsNum.getMerchantId());
//                if (null != merchantInfo) {
//                    goodsNum.setMerchantName(merchantInfo.getName());
//                }
//            }
//        }
//
//        return new ListSlice(goodsNumList, totalNum);
//    }
//
//    @Override
//    public void reduceGoodsNumByOrderList(List<OrderInfo> orderInfoList) throws ApiException {
//        for (OrderInfo orderInfo : orderInfoList) {
//            if (StringUtils.equals(orderInfo.getServeType(), Constants.OrderServeType.ONLINE)) {
//                OrderDelivery orderDelivery = orderInfo.getOrderDelivery();
//                String merchantType = orderDelivery.getMerchantType();
//                String merchantId = orderDelivery.getMerchantId();
//                //线上 除了返货方是autoask之外其他的库存都要减掉
//                if (!StringUtils.equals(Constants.MerchantType.AUTOASK, merchantType)) {
//                    for (OrderGoods orderGoods : orderInfo.getOrderGoodsList()) {
//                        String goodsId = orderGoods.getGoodsSnapshot().getGoodsId();
//                        int num = orderGoods.getNum();
//                        //必须传递负数
//                        Long addNum = new Long(0 - num);
//                        goodsNumMapper.updateGoodsNum(merchantType, merchantId, goodsId, addNum);
//                    }
//                }
//            } else {
//                //线下订单
//                OrderServe orderServe = orderInfo.getOrderServe();
//                for (OrderGoods orderGoods : orderInfo.getOrderGoodsList()) {
//                    String goodsId = orderGoods.getGoodsSnapshot().getGoodsId();
//                    int num = orderGoods.getNum();
//                    //必须传递负数
//                    Long addNum = new Long(0 - num);
//                    goodsNumMapper.updateGoodsNum(Constants.MerchantType.SERVICE_PROVIDER, orderServe.getServiceProviderId(), goodsId, addNum);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void reduceGoodsNumByOrderId(String orderId) throws ApiException {
//        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
//        if (null == orderInfo) {
//            throw new ApiException("参数异常");
//        }
//        reduceGoodsNumByOrderList(Arrays.asList(orderInfo));
//    }
//
//    private void updateGoodsNumWithNoCheck(String merchantType, String merchantId, String goodsId, Long addNum) throws ApiException {
//        int rowNum = goodsNumMapper.updateGoodsNum(merchantType, merchantId, goodsId, addNum);
//        if (0 == rowNum) {
//            insertGoodsNum(merchantType, merchantId, goodsId, addNum);
//        }
//    }
//
//    @Override
//    public void updateGoodsNumBack(OrderInfo orderInfo) throws ApiException {
//        if (null == orderInfo) {
//            throw new ApiException("订单不存在");
//        }
//        List<OrderGoods> orderGoodsList = orderInfo.getOrderGoodsList();
//        if (CollectionUtils.isEmpty(orderGoodsList)) {
//            throw new ApiException("参数异常");
//        }
//        String merchantType = orderInfo.getOrderDelivery().getMerchantType();
//        String merchantId = orderInfo.getOrderDelivery().getMerchantId();
//
//        //非autoask 需要退掉库存
//        if (!StringUtils.equals(merchantType, Constants.MerchantType.AUTOASK) && StringUtils.isNotEmpty(merchantType)) {
//            for (OrderGoods orderGoods : orderGoodsList) {
//                String goodsId = orderGoods.getGoodsSnapshot().getGoodsId();
//                Integer num = orderGoods.getNum();
//                goodsNumMapper.updateGoodsNum(merchantType, merchantId, goodsId, new Long(num));
//            }
//        }
//    }
//
//    @Override
//    public void updateGoodsNum(GoodsNumRecord goodsNumRecord) throws ApiException {
//        checkUpdateGoodsNumParam(goodsNumRecord);
//
//        List<GoodsNumRecord.GoodsInfo> goodsInfoList = goodsNumRecord.getGoodsInfoList();
//        String merchantType = goodsNumRecord.getMerchantType();
//        String merchantId = goodsNumRecord.getMerchantId();
//        String loginType = LoginUtil.getSessionInfo().getLoginType();
//        String loginId = LoginUtil.getSessionInfo().getLoginId();
//        for (GoodsNumRecord.GoodsInfo goodsInfo : goodsInfoList) {
//            String goodsId = goodsInfo.getGoodsId();
//            Long num = goodsInfo.getNum();
//
//            if (!StringUtils.equals(loginType, Constants.LoginType.STAFF)) {
//                if (goodsNumRecord.getAddType()) {
//                    //发货需要检查是否有足够的库存
//                    Long selfNum = getGoodsNum(loginType, loginId, goodsId);
//
//                    if (selfNum < num) {
//                        throw new ApiException(MessageFormat.format("{0}库存不足，无法发货", goodsInfo.getGoodsName()));
//                    }
//                }
//                if (!goodsNumRecord.getAddType()) {
//                    //退货需要检查退货方是否有此库存
//                    num = checkSubNum(merchantType, merchantId, goodsId, num);
//                }
//                this.updateGoodsNumWithNoCheck(merchantType, merchantId, goodsId, num);
//                //非 autoask 发货，需要更改自己的库存
//                this.updateGoodsNumWithNoCheck(loginType, loginId, goodsId, 0 - num);
//            } else {
//                //autoask 操作，无需检查autoask的库存
//                if (!goodsNumRecord.getAddType()) {
//                    //退货需要检查退货方是否有此库存
//                    num = checkSubNum(merchantType, merchantId, goodsId, num);
//                }
//                this.updateGoodsNumWithNoCheck(merchantType, merchantId, goodsId, num);
//            }
//        }
//        //成功了 记录记录
//        goodsNumRecord.setCreatorId(LoginUtil.getSessionInfo().getLoginId());
//        goodsNumRecord.setCreatorType(LoginUtil.getSessionInfo().getLoginType());
//        goodsNumRecord.setCreateTime(DateUtil.getDate());
//        goodsNumRecordDao.save(goodsNumRecord);
//    }
//
////    private Long checkSubNum(String merchantType, String merchantId, String goodsId, Long num) throws ApiException {
////        //退货需要检查退货方是否有此库存
////        Long goodsNum = getGoodsNum(merchantType, merchantId, goodsId);
////        if (goodsNum < num) {
////            throw new ApiException("退货方没有足够的库存退货");
////        }
////        //退货需要减少库存
////        num = 0 - num;
////        return num;
////    }
//
//    /**
//     * 检查更新的参数是否合法
//     *
//     * @param goodsNumRecord
//     * @throws ApiException
//     */
//    private void checkUpdateGoodsNumParam(GoodsNumRecord goodsNumRecord) throws ApiException {
//        String loginType = LoginUtil.getSessionInfo().getLoginType();
//        String loginId = LoginUtil.getSessionInfo().getLoginId();
//        if (!StringUtils.equals(loginType, Constants.LoginType.STAFF)) {
//            throw new ApiException("没有权限");
//        }
//        Query query = Query.query(Criteria.where("serial").is(goodsNumRecord.getSerial()).
//                and("addType").is(goodsNumRecord.getAddType()));
//        GoodsNumRecord preRecord = goodsNumRecordDao.findOne(query);
//        if (null != preRecord) {
//            throw new ApiException("发货/退货单号 重复");
//        }
//    }
//
//    private GoodsNum insertGoodsNum(String merchantType, String merchantId, String goodsId, Long num) throws ApiException {
//        GoodsNum goodsNum = new GoodsNum();
//        goodsNum.setGoodsId(goodsId);
//        goodsNum.setMerchantType(merchantType);
//        goodsNum.setMerchantId(merchantId);
//        goodsNum.setNum(num);
//        goodsNum.setCreateTime(DateUtil.getDate());
//        goodsNum.setModifyTime(DateUtil.getDate());
//        goodsNumMapper.insert(goodsNum);
//        return goodsNum;
//    }
//
//    @Override
//    public List<GoodsNum> getLackingGoodsNum(String merchantType, Integer start, Integer limit) throws ApiException {
//        String loginType = LoginUtil.getLoginType();
//        String loginId = LoginUtil.getLoginId();
//        List<GoodsNum> goodsNumList = null;
//        if (StringUtils.equals(merchantType, Constants.MerchantType.SERVICE_PROVIDER)) {
//            if (StringUtils.equals(loginType, Constants.LoginType.SERVICE_PROVIDER)) {
//                goodsNumList = goodsNumMapper.getGoodsNumLackList(Constants.MerchantType.SERVICE_PROVIDER, Arrays.asList(loginId), Constants.WARNING_GOODS_NUM, start, limit);
//
//            }
//        } else if (StringUtils.equals(merchantType, Constants.MerchantType.FACTORY)) {
//            if (StringUtils.equals(loginType, Constants.LoginType.FACTORY)) {
//                goodsNumList = goodsNumMapper.getGoodsNumLackList(Constants.MerchantType.FACTORY, Arrays.asList(loginId), Constants.WARNING_GOODS_NUM, start, limit);
//            }
//        }
//        if (CollectionUtils.isNotEmpty(goodsNumList)) {
//            ArrayList<BaseMerchant> merchantList = new ArrayList<>();
//            for (GoodsNum goodsNum : goodsNumList) {
//                BaseMerchant baseMerchant = new BaseMerchant();
//                baseMerchant.setLoginType(goodsNum.getMerchantType());
//                baseMerchant.setId(goodsNum.getMerchantId());
//                merchantList.add(baseMerchant);
//            }
//            Map<String, String> merchantNameMap = merchantService.getMerchantNameMap(merchantList);
//            for (GoodsNum goodsNum : goodsNumList) {
//                goodsNum.setMerchantName(merchantNameMap.get(goodsNum.getMerchantType() + goodsNum.getMerchantId()));
//            }
//        }
//        return goodsNumList;
//    }

}
