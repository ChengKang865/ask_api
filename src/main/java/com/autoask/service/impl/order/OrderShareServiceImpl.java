package com.autoask.service.impl.order;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.LoginUtil;
import com.autoask.entity.mongo.merchant.BaseMerchant;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.entity.mysql.*;
import com.autoask.mapper.OrderGoodsShareMapper;
import com.autoask.mapper.OrderShareMapper;
import com.autoask.service.merchant.MerchantService;
import com.autoask.service.merchant.ServiceProviderService;
import com.autoask.service.order.OrderShareService;
import com.autoask.service.product.ProductCategoryService;
import com.autoask.service.user.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hyy
 * @create 2016-12-04 14:53
 */
@Service
public class OrderShareServiceImpl implements OrderShareService {

    @Autowired
    private UserService userService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private OrderShareMapper orderShareMapper;

    @Autowired
    private OrderGoodsShareMapper orderGoodsShareMapper;


    /**
     * 计算 orderInfo 对应的 orderShare
     *
     * @param orderInfo
     * @param cardMap
     * @param goodsMap
     * @return
     * @throws ApiException
     */
    @Override
    public OrderShare initOrderShare(OrderInfo orderInfo, Map<String, Card> cardMap, Map<String, Goods> goodsMap) throws ApiException {
        OrderShare orderShare = new OrderShare();

        orderShare.setOrderId(orderInfo.getOrderId());
        orderShare.setOrderGoodsShareList(initOrderGoodsShareList(orderInfo, cardMap, goodsMap));
        calcOrderShare(orderInfo, orderShare, goodsMap);

        orderInfo.setOrderShare(orderShare);
        calcOrderDeliveryPreShare(orderInfo);
        calcOrderServePreShare(orderInfo);
        return orderShare;
    }

    /**
     * 重新计算 orderShare中的各个总和
     *
     * @param orderInfo
     * @param orderShare
     */
    private void calcOrderShare(OrderInfo orderInfo, OrderShare orderShare, Map<String, Goods> goodsMap) throws ApiException {

        boolean taxFlag = StringUtils.isNotEmpty(orderInfo.getInvoiceId());

        BigDecimal promoteFee = new BigDecimal(0);
        BigDecimal originAdFee = new BigDecimal(0);
        BigDecimal handleFee = new BigDecimal(0);
        BigDecimal factoryFee = new BigDecimal(0);
        List<OrderGoodsShare> orderGoodsShareList = orderShare.getOrderGoodsShareList();
        for (OrderGoodsShare orderGoodsShare : orderGoodsShareList) {
            promoteFee = promoteFee.add(orderGoodsShare.getPromoteFee());
            originAdFee = originAdFee.add(orderGoodsShare.getOriginAdFee());
            handleFee = handleFee.add(orderGoodsShare.getHandleFee());
            factoryFee = factoryFee.add(orderGoodsShare.getFactoryFee());
        }
        OrderGoodsShare firstShare = orderGoodsShareList.get(0);
        orderShare.setPromoteFee(promoteFee);
        orderShare.setPromoteType(firstShare.getPromoteType());
        orderShare.setPromoteId(firstShare.getPromoteId());

        orderShare.setOriginAdFee(originAdFee);
        if (taxFlag) {
            originAdFee = originAdFee.multiply(Constants.TAX_RATE);
        }
        orderShare.setAdFee(originAdFee);
        orderShare.setAdType(firstShare.getAdType());
        orderShare.setAdId(firstShare.getAdId());

        orderShare.setHandleFee(handleFee);
        orderShare.setHandleId(firstShare.getHandleId());

        orderShare.setFactoryFee(factoryFee);
        orderShare.setFactoryId(firstShare.getFactoryId());

        //serviceFee
        OrderServe orderServe = orderInfo.getOrderServe();
        if (null != orderServe) {
            orderShare.setServiceType(Constants.MerchantType.SERVICE_PROVIDER);
            orderShare.setServiceId(orderServe.getServiceProviderId());
            List<String> goodsIds = new ArrayList<>(orderInfo.getOrderGoodsList().size());
            for (OrderGoods orderGoods : orderInfo.getOrderGoodsList()) {
                String goodsId = goodsMap.get(orderGoods.getGoodsSnapshotId()).getGoodsId();
                goodsIds.add(goodsId);
            }
            BigDecimal serviceFee = productCategoryService.getServiceFee(goodsIds);
            orderShare.setOriginServiceFee(serviceFee);
            if (taxFlag) {
                serviceFee = serviceFee.multiply(Constants.TAX_RATE);
            }
            orderShare.setServiceFee(serviceFee);
        } else {
            orderShare.setServiceType(null);
            orderShare.setServiceId(null);
            orderShare.setServiceFee(new BigDecimal(0));
            orderShare.setOriginServiceFee(new BigDecimal(0));
        }
    }

    private List<OrderGoodsShare> initOrderGoodsShareList(OrderInfo orderInfo, Map<String, Card> cardMap, Map<String, Goods> goodsMap) throws ApiException {
        String userId = LoginUtil.getSessionInfo().getLoginId();
        User user = userService.getUser(userId);
        String promoteType = user.getPromoteType();
        String promoteId = user.getPromoteId();

        List<OrderGoods> orderGoodsList = orderInfo.getOrderGoodsList();

        List<OrderGoodsShare> orderGoodsShareList = new ArrayList<>(orderGoodsList.size());
        for (OrderGoods orderGoods : orderGoodsList) {
            Goods goods = goodsMap.get(orderGoods.getGoodsSnapshotId());

            //引流费
            BigDecimal promoteFee = new BigDecimal(0);
            //广告费
            BigDecimal adFee = new BigDecimal(0);
            //处理费
            BigDecimal handleFee = new BigDecimal(0);
            //工厂费
            BigDecimal factoryFee = new BigDecimal(0);

            List<OrderGoodsCard> orderGoodsCardList = orderGoods.getOrderGoodsCardList();
            int cardNum = orderGoodsCardList == null ? 0 : orderGoodsCardList.size();
            int goodsNum = orderGoods.getNum() - cardNum;

            promoteFee = promoteFee.add(goods.getPromoteFee().multiply(BigDecimal.valueOf(goodsNum)));
            adFee = adFee.add(goods.getAdFee().multiply(BigDecimal.valueOf(goodsNum)));
            handleFee = handleFee.add(goods.getHandleFee().multiply(BigDecimal.valueOf(goodsNum)));
            factoryFee = factoryFee.add(goods.getFactoryFee().multiply(BigDecimal.valueOf(goodsNum)));

            if (CollectionUtils.isNotEmpty(orderGoodsCardList)) {
                for (OrderGoodsCard orderGoodsCard : orderGoodsCardList) {
                    String cardId = orderGoodsCard.getCardId();
                    Card card = cardMap.get(cardId);
                    if (null == card) {
                        throw new ApiException("卡不存在");
                    }
                    CardType cardType = card.getCardType();
                    if (null == cardType) {
                        throw new ApiException("数据异常");
                    }
                    promoteFee = promoteFee.add(cardType.getPromoteFee());
                    adFee = adFee.add(cardType.getAdFee());
                    handleFee = handleFee.add(cardType.getHandleFee());
                    factoryFee = factoryFee.add(cardType.getFactoryFee());
                }
            }

            OrderGoodsShare orderGoodsShare = initOrderGoodsShare(orderInfo, orderGoods, promoteType, promoteId, adFee, factoryFee, promoteFee);

            orderGoodsShareList.add(orderGoodsShare);
        }

        return orderGoodsShareList;
    }

    private OrderGoodsShare initOrderGoodsShare(OrderInfo orderInfo, OrderGoods orderGoods, String promoteType, String promoteId, BigDecimal adFee, BigDecimal factoryFee, BigDecimal promoteFee) {
        OrderGoodsShare orderGoodsShare = new OrderGoodsShare();

        boolean taxFlag = StringUtils.isNotEmpty(orderInfo.getInvoiceId());

        orderGoodsShare.setOrderGoodsId(orderGoods.getOrderGoodsId());
        orderGoodsShare.setOrderId(orderGoods.getOrderId());
        orderGoodsShare.setPromoteType(promoteType);
        orderGoodsShare.setPromoteId(promoteId);

        //promote fee
        if (StringUtils.isEmpty(promoteType) || StringUtils.isEmpty(promoteId)) {
            promoteFee = new BigDecimal(0);
        }
        orderGoodsShare.setPromoteFee(promoteFee);

        //handle fee
        orderGoodsShare.setHandleFee(new BigDecimal(0));
        orderGoodsShare.setHandleId(null);

        //ad fee factory fee
        OrderServe orderServe = orderInfo.getOrderServe();
        if (null != orderServe) {
            orderGoodsShare.setAdType(Constants.MerchantType.SERVICE_PROVIDER);
            orderGoodsShare.setAdId(orderServe.getServiceProviderId());
            orderGoodsShare.setOriginAdFee(adFee);
            if (taxFlag) {
                adFee = adFee.multiply(Constants.TAX_RATE);
            }
            //实际收入
            orderGoodsShare.setAdFee(adFee);

            orderGoodsShare.setFactoryId(null);
            orderGoodsShare.setFactoryFee(new BigDecimal(0));
        } else {
            orderGoodsShare.setAdType(null);
            orderGoodsShare.setAdId(null);
            orderGoodsShare.setAdFee(new BigDecimal(0));
            orderGoodsShare.setOriginAdFee(new BigDecimal(0));

            orderGoodsShare.setFactoryId(orderInfo.getOrderDelivery().getMerchantId());
            orderGoodsShare.setFactoryFee(factoryFee);
        }

        return orderGoodsShare;
    }

    //计算快递发货方的预计分成
    private void calcOrderDeliveryPreShare(OrderInfo orderInfo) {
        OrderDelivery orderDelivery = orderInfo.getOrderDelivery();
        if (null == orderDelivery) {
            return;
        }
        String merchantType = orderDelivery.getMerchantType();
        if (StringUtils.equals(Constants.MerchantType.AUTOASK, merchantType)) {
            orderDelivery.setPreShare(new BigDecimal(0));
        } else if (StringUtils.equals(Constants.MerchantType.FACTORY, merchantType)) {
            //factory 会分成 引流费 和 制造费
            BigDecimal totalPreShare = new BigDecimal(0);
            String merchantId = orderDelivery.getMerchantId();
            OrderShare orderShare = orderInfo.getOrderShare();
            if (StringUtils.equals(Constants.MerchantType.FACTORY, orderShare.getPromoteType()) && StringUtils.equals(merchantId, orderShare.getPromoteId())) {
                totalPreShare = totalPreShare.add(orderShare.getPromoteFee());
            }
            totalPreShare = totalPreShare.add(orderShare.getFactoryFee());
            orderDelivery.setPreShare(totalPreShare);
        }
    }

    //计算orderServe的修理厂的预计分成
    private void calcOrderServePreShare(OrderInfo orderInfo) {
        OrderServe orderServe = orderInfo.getOrderServe();
        OrderShare orderShare = orderInfo.getOrderShare();
        if (null == orderServe) {
            return;
        }
        BigDecimal totalPreShare = orderShare.getServiceFee().add(orderShare.getAdFee());
        if (StringUtils.equals(Constants.MerchantType.SERVICE_PROVIDER, orderShare.getPromoteType()) && StringUtils.equals(orderServe.getServiceProviderId(), orderShare.getPromoteId())) {
            totalPreShare = totalPreShare.add(orderShare.getPromoteFee());
        }
        orderServe.setServiceProviderPreShare(totalPreShare);
    }


    @Override
    public void initOrderShareMerchantName(List<OrderShare> orderShareList) throws ApiException {
        if (CollectionUtils.isNotEmpty(orderShareList)) {
            List<BaseMerchant> merchantList = new ArrayList<>(orderShareList.size() * 8);
            for (OrderShare orderShare : orderShareList) {

                BaseMerchant promoteMerchant = new BaseMerchant();
                promoteMerchant.setLoginType(orderShare.getPromoteType());
                promoteMerchant.setId(orderShare.getPromoteId());
                merchantList.add(promoteMerchant);

                BaseMerchant mechanicMerchant = new BaseMerchant();
                mechanicMerchant.setLoginType(Constants.MerchantType.MECHANIC);
                mechanicMerchant.setId(orderShare.getHandleId());
                merchantList.add(mechanicMerchant);


                BaseMerchant adMerchant = new BaseMerchant();
                adMerchant.setLoginType(orderShare.getAdType());
                adMerchant.setId(orderShare.getAdId());
                merchantList.add(adMerchant);

                BaseMerchant serviceMerchant = new BaseMerchant();
                serviceMerchant.setLoginType(orderShare.getServiceType());
                serviceMerchant.setId(orderShare.getServiceId());
                merchantList.add(serviceMerchant);

                BaseMerchant factoryMerchant = new BaseMerchant();
                factoryMerchant.setLoginType(Constants.MerchantType.FACTORY);
                factoryMerchant.setId(orderShare.getFactoryId());
                merchantList.add(factoryMerchant);
            }

            Map<String, String> merchantNameMap = merchantService.getMerchantNameMap(merchantList);

            for (OrderShare orderShare : orderShareList) {
                orderShare.setPromoteName(merchantNameMap.get(orderShare.getPromoteType() + orderShare.getPromoteId()));
                orderShare.setHandelName(merchantNameMap.get(Constants.MerchantType.MECHANIC + orderShare.getHandleId()));
                orderShare.setAdName(merchantNameMap.get(orderShare.getAdType() + orderShare.getAdId()));
                orderShare.setServiceName(merchantNameMap.get(orderShare.getServiceType() + orderShare.getServiceId()));
                orderShare.setFactoryName(merchantNameMap.get(Constants.MerchantType.FACTORY + orderShare.getFactoryId()));
            }
        }
    }


    @Override
    public void updateOrderShareNoServiceProvider(String orderId) throws ApiException {
        orderShareMapper.updateOrderShareNoServiceProvider(orderId);
        orderGoodsShareMapper.updateNoServiceProvider(orderId);
    }
}
