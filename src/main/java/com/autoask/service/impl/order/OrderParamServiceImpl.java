package com.autoask.service.impl.order;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.*;
import com.autoask.entity.common.Address;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.entity.mysql.*;
import com.autoask.entity.param.OrderParam;
import com.autoask.mapper.CardMapper;
import com.autoask.mapper.GoodsMapper;
import com.autoask.service.express.ExpressTemplateService;
import com.autoask.service.merchant.ServiceProviderService;
import com.autoask.service.order.OrderParamService;
import com.autoask.service.product.ProductCategoryService;
import com.autoask.service.user.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author hyy
 * @create 2016-12-01 16:26
 */
@Service
public class OrderParamServiceImpl implements OrderParamService {

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExpressTemplateService expressTemplateService;


    /**
     * 查询出所有的商品 goodsSnapshotId --- goods
     *
     * @param orderParam
     * @return
     * @throws ApiException
     */
    @Override
    public Map<String, Goods> getGoodsMap(OrderParam orderParam) throws ApiException {
        OrderParam.Online online = orderParam.getOnline();

        List<String> goodsSnapshotIdList = new ArrayList<>();
        if (null != online) {
            if (MapUtils.isNotEmpty(online.getSnapshotCountMap())) {
                goodsSnapshotIdList.addAll(online.getSnapshotCountMap().keySet());
            }
        }
        Example goodsExp = new Example(Goods.class);
        goodsExp.createCriteria().andIn("goodsSnapshotId", goodsSnapshotIdList).andEqualTo("deleteFlag", false).andEqualTo("saleFlag", true);
        List<Goods> goodsList = goodsMapper.selectByExample(goodsExp);
        HashMap<String, Goods> goodsMap = new HashMap<>(goodsList.size(), 1);
        for (Goods goods : goodsList) {
            goodsMap.put(goods.getGoodsSnapshotId(), goods);
        }
        if (goodsSnapshotIdList.size() != goodsMap.size()) {
            throw new ApiException("购物车中有下架的商品，请重新下单");
        }
        return goodsMap;
    }

    /**
     * 查询出所有的卡 cardId --- card:cardType:goodsCardTypeList
     *
     * @param orderParam
     * @return
     */
    @Override
    public Map<String, Card> getCardMap(OrderParam orderParam) {
        OrderParam.Online online = orderParam.getOnline();

        List<String> cardIdList = new ArrayList<>();
        if (null != online) {
            if (CollectionUtils.isNotEmpty(online.getCardList())) {
                cardIdList.addAll(online.getCardList());
            }
        }
        if (CollectionUtils.isNotEmpty(cardIdList)) {
            List<Card> cardList = cardMapper.getCardDetailList(cardIdList);
            HashMap<String, Card> cardMap = new HashMap<>(cardList.size());
            for (Card card : cardList) {
                cardMap.put(card.getCardId(), card);
            }
            return cardMap;
        }
        return null;
    }

    /**
     * 根据 snapshotCountMap 以及 cardList 初始化 对应的 snapshotInfoList
     *
     * @param orderParam
     * @param goodsMap
     * @param cardMap
     */
    @Override
    public void initOrderParam(OrderParam orderParam, Map<String, Goods> goodsMap, Map<String, Card> cardMap) throws ApiException {
        //(goodsId:goodsSnapshotId)
        HashMap<String, String> id2SnapshotIdMap = new HashMap<>(goodsMap.size());
        for (Map.Entry<String, Goods> goodsEntry : goodsMap.entrySet()) {
            id2SnapshotIdMap.put(goodsEntry.getValue().getGoodsId(), goodsEntry.getKey());
        }
        OrderParam.Online online = orderParam.getOnline();
        if (null != online) {
            Map<String, Integer> snapshotCountMap = online.getSnapshotCountMap();
            List<OrderParam.SnapshotInfo> snapshotInfoList = initSnapshotInfoList(cardMap, id2SnapshotIdMap, snapshotCountMap, online.getCardList());
            online.setSnapshotInfoList(snapshotInfoList);
        }
    }

    /**
     * 初始化 snapshotInfoList
     *
     * @param cardMap
     * @param id2SnapshotIdMap
     * @param snapshotCountMap
     * @param cardList
     * @return
     * @throws ApiException
     */
    private List<OrderParam.SnapshotInfo> initSnapshotInfoList(Map<String, Card> cardMap, HashMap<String, String> id2SnapshotIdMap, Map<String, Integer> snapshotCountMap, List<String> cardList) throws ApiException {
        HashMap<String, OrderParam.SnapshotInfo> snapshotInfoMap = new HashMap<>(snapshotCountMap.size());
        for (Map.Entry<String, Integer> itemEntry : snapshotCountMap.entrySet()) {
            OrderParam.SnapshotInfo snapshotInfo = new OrderParam.SnapshotInfo();
            snapshotInfo.setGoodsSnapshotId(itemEntry.getKey());
            snapshotInfo.setNum(itemEntry.getValue());

            snapshotInfoMap.put(itemEntry.getKey(), snapshotInfo);
        }
        if (CollectionUtils.isNotEmpty(cardList)) {
            //对snapshotCountMap的深拷贝
            Map<String, Integer> backUpCountMap = new HashMap<>(snapshotCountMap.size());
            backUpCountMap.putAll(snapshotCountMap);
            initSnapshotInfoCardList(cardMap, id2SnapshotIdMap, cardList, backUpCountMap, snapshotInfoMap);
        }
        List<OrderParam.SnapshotInfo> snapshotInfoList = new ArrayList<>(snapshotInfoMap.size());
        snapshotInfoList.addAll(snapshotInfoMap.values());
        return snapshotInfoList;
    }

    /**
     * 初始化 snapshotInfo中的cardList
     *
     * @param cardMap
     * @param id2SnapshotIdMap
     * @param cardList
     * @param backUpCountMap
     * @param snapshotInfoMap
     * @throws ApiException
     */
    private void initSnapshotInfoCardList(Map<String, Card> cardMap, HashMap<String, String> id2SnapshotIdMap, List<String> cardList, Map<String, Integer> backUpCountMap, HashMap<String, OrderParam.SnapshotInfo> snapshotInfoMap) throws ApiException {
        HashSet<String> cardIdSet = new HashSet<>();
        cardIdSet.addAll(cardList);
        if (cardIdSet.size() != cardList.size()) {
            throw new ApiException("卡列表中由重复的卡");
        }
        int exchangeNum = 0;
        for (String cardId : cardList) {
            Card card = cardMap.get(cardId);
            if (null == card) {
                throw new ApiException(MessageFormat.format("{0}卡不存在或者已经兑换", cardId));
            }
            List<GoodsCardType> goodsCardTypeList = card.getCardType().getGoodsCardTypeList();
            for (GoodsCardType goodsCardType : goodsCardTypeList) {
                String goodsId = goodsCardType.getGoodsId();
                String snapshotId = id2SnapshotIdMap.get(goodsId);
                if (StringUtils.isNotEmpty(snapshotId)) {
                    Integer nowNum = backUpCountMap.get(snapshotId);
                    if (null != nowNum && nowNum > 0) {
                        nowNum = nowNum - 1;
                        backUpCountMap.put(snapshotId, nowNum);

                        OrderParam.SnapshotInfo snapshotInfo = snapshotInfoMap.get(snapshotId);
                        if (null == snapshotInfoMap) {
                            throw new ApiException("数据异常");
                        }
                        ArrayList<String> infoCardList = snapshotInfo.getCardList();
                        if (CollectionUtils.isEmpty(infoCardList)) {
                            infoCardList = new ArrayList<>();
                        }
                        infoCardList.add(cardId);
                        snapshotInfo.setCardList(infoCardList);
                        snapshotInfoMap.put(snapshotId, snapshotInfo);

                        //兑换数量+1
                        exchangeNum++;
                        break;
                    }
                }
            }
        }
        if (exchangeNum != cardList.size()) {
            throw new ApiException("存在对应兑换商品的卡");
        }
    }

    @Override
    public void checkParam(OrderParam orderParam, Map<String, Goods> goodsMap, Map<String, Card> cardMap) throws ApiException {
        //检查渠道 channel ['m','pc']
        if (!Constants.Channel.TYPE_LIST.contains(orderParam.getChannel())) {
            throw new ApiException("支付渠道参数错误");
        }
        if (null != orderParam.getOnline()) {
            checkOnlineParam(orderParam, goodsMap, cardMap);
        }
    }

    private void checkOnlineParam(OrderParam orderParam, Map<String, Goods> goodsMap, Map<String, Card> cardMap) throws ApiException {
        OrderParam.Online online = orderParam.getOnline();
        if (!Constants.PayType.NO_CASH_TYPES.contains(orderParam.getPayType())) {
            throw new ApiException("支付类型非法");
        }
        if (MapUtils.isEmpty(goodsMap)) {
            throw new ApiException("购物车存在下架商品，请清空购物车重新下单");
        }
        //判断收货信息是否存在
        Address address = orderParam.getOnline().getAddress();
        if (null == address) {
            throw new ApiException("收货地址不能为空");
        }
        if (StringUtils.isEmpty(address.getProvince())) {
            throw new ApiException("省不能为空");
        }
        if (StringUtils.isEmpty(address.getCity())) {
            throw new ApiException("市不能为空");
        }
        if (StringUtils.isEmpty(address.getRegion())) {
            throw new ApiException("区不能为空");
        }
        if (StringUtils.isEmpty(address.getStreet())) {
            throw new ApiException("街道不能为空");
        }
        if (StringUtils.isEmpty(address.getDetail())) {
            throw new ApiException("详细地址不能为空");
        }

        //检查价格信息
        checkOnlinePrice(orderParam, goodsMap, cardMap);
    }

    private void checkOnlinePrice(OrderParam orderParam, Map<String, Goods> goodsMap, Map<String, Card> cardMap) throws ApiException {
        OrderParam.Online online = orderParam.getOnline();
        List<OrderParam.SnapshotInfo> snapshotInfoList = online.getSnapshotInfoList();
        //快照总价
        BigDecimal totalSnapshotPrice = new BigDecimal(0);
        //卡生成的折扣总价
        BigDecimal totalDiscountPrice = new BigDecimal(0);
        //商品总重量
        BigDecimal totalWeight = new BigDecimal(0);
        for (OrderParam.SnapshotInfo snapshotInfo : snapshotInfoList) {
            String goodsSnapshotId = snapshotInfo.getGoodsSnapshotId();
            Goods goods = goodsMap.get(goodsSnapshotId);
            //检查商品的信息
            checkGoodsInfo(goodsSnapshotId, goods, true);
            //检查卡兑换信息
            if (CollectionUtils.isNotEmpty(snapshotInfo.getCardList())) {
                checkCardParam(cardMap, snapshotInfo, goods);
            }
            int cardNum = CollectionUtils.isEmpty(snapshotInfo.getCardList()) ? 0 : snapshotInfo.getCardList().size();
            if (snapshotInfo.getNum() < cardNum) {
                throw new ApiException("卡兑换数量不能大于商品数量");
            }
            totalSnapshotPrice = totalSnapshotPrice.add(goods.getOnlinePrice().multiply(new BigDecimal(snapshotInfo.getNum())));
            totalDiscountPrice = totalDiscountPrice.add(goods.getOnlinePrice().multiply(new BigDecimal(cardNum)));
            totalWeight = totalWeight.add(goods.getWeight().multiply(new BigDecimal(snapshotInfo.getNum())));
        }

        //计算邮费
        String province = orderParam.getOnline().getAddress().getProvince();
        BigDecimal deliverFee = expressTemplateService.getExpressPrice(totalSnapshotPrice.toString(), province, totalWeight.toString());
        if (online.getSnapshotTotalPrice().compareTo(totalSnapshotPrice) != 0) {
            throw new ApiException("商品价格信息不一致");
        }
        if (online.getDeliveryFee().compareTo(deliverFee) != 0) {
            throw new ApiException("快递费用不一致");
        }
        String serviceProviderId = online.getServiceProviderId();
        BigDecimal serviceFee = new BigDecimal(0);
        if (StringUtils.isNotEmpty(serviceProviderId)) {
            ServiceProvider serviceProvider = serviceProviderService.findById(serviceProviderId);
            if (null == serviceProvider) {
                throw new ApiException("服务店不存在");
            }
            //检查服务费是否一致
            List<String> goodsIds = new ArrayList<>(goodsMap.size());
            for (Map.Entry<String, Goods> entry : goodsMap.entrySet()) {
                goodsIds.add(entry.getValue().getGoodsId());
            }
            serviceFee = productCategoryService.getServiceFee(goodsIds);
            if (online.getServiceFee().compareTo(serviceFee) != 0) {
                throw new ApiException("服务费不一致");
            }
        }
        BigDecimal totalPrice = totalSnapshotPrice.add(serviceFee).add(deliverFee).subtract(totalDiscountPrice);
        if (online.getPayTotalPrice().compareTo(totalPrice) != 0) {
            throw new ApiException("总价不一致");
        }
    }

    /**
     * 检查商品是否上下架、是否是最新快照、是否符合购买方式（线上商品不能选择线下服务）
     *
     * @param goodsSnapshotId
     * @param goods
     * @param onlineFlag
     * @throws ApiException
     */
    private void checkGoodsInfo(String goodsSnapshotId, Goods goods, Boolean onlineFlag) throws ApiException {
        //商品不存在
        if (null == goods) {
            throw new ApiException("商品不存在或已下架，请清空购物车重新下单");
        }
        if (!onlineFlag && StringUtils.equals(Constants.GoodsType.ONLINE, goods.getType())) {
            throw new ApiException(MessageFormat.format("{0}服务店无货,请选择寄送到家", goods.getName()));
        }
        //快照不是最新商品信息
        if (!StringUtils.equals(goodsSnapshotId, goods.getGoodsSnapshotId())) {
            throw new ApiException(MessageFormat.format("{0}信息已经更新，请重新下单", goods.getName()));
        }
    }

    /**
     * 检查卡号是否重复，检查卡是否能够兑换对应的商品，检查卡是否已经使用过
     *
     * @param cardMap
     * @param snapshotInfo
     * @param goods
     * @throws ApiException
     */
    private void checkCardParam(Map<String, Card> cardMap, OrderParam.SnapshotInfo snapshotInfo, Goods goods) throws ApiException {
        //检查卡号是否重复
        ArrayList<String> cardList = snapshotInfo.getCardList();
        HashSet<String> cardIdSet = new HashSet<>(cardList.size());
        for (String cardId : cardList) {
            cardIdSet.add(cardId);
            Card card = cardMap.get(cardId);
            if (null == card || null == card.getCardType() || CollectionUtils.isEmpty(card.getCardType().getGoodsCardTypeList())) {
                throw new ApiException(MessageFormat.format("{0}卡不存在", cardId));
            }
            Card.canUse(card);
            boolean matchFlag = false;
            for (GoodsCardType goodsCardType : card.getCardType().getGoodsCardTypeList()) {
                if (StringUtils.equals(goodsCardType.getGoodsId(), goods.getGoodsId())) {
                    matchFlag = true;
                    break;
                }
            }
            if (!matchFlag) {
                throw new ApiException(MessageFormat.format("{0}卡不能兑换{1}", cardId, goods.getName()));
            }
        }
        if (cardIdSet.size() != cardList.size()) {
            throw new ApiException("有重复的卡");
        }
    }

    @Override
    public List<OrderInfo> getOnlineOrderList(OrderParam orderParam, Map<String, Goods> goodsMap, Map<String, Card> cardMap) throws ApiException {
        boolean isDeliveryFree = false;
        if (orderParam.getOnline().getDeliveryFee().compareTo(BigDecimal.ZERO) == 0) {
            isDeliveryFree = true;
        }

        OrderParam.Online online = orderParam.getOnline();
        List<OrderParam.SnapshotInfo> snapshotInfoList = online.getSnapshotInfoList();
        Date createTime = DateUtil.getDate();

        //工厂
        Map<String, OrderInfo> f2cMap = new HashMap<>();

        //autoask
        OrderInfo autoaskOrder = null;

        for (OrderParam.SnapshotInfo snapshotInfo : snapshotInfoList) {
            String snapshotId = snapshotInfo.getGoodsSnapshotId();
            Goods goods = goodsMap.get(snapshotId);
            //处理方
            String sendMerchantType = getSendMerchantType(goods);

            if (StringUtils.equals(sendMerchantType, Constants.MerchantType.AUTOASK)) {
                //autoask 发货
                if (null == autoaskOrder) {
                    autoaskOrder = initOnlineOrder(orderParam, Constants.MerchantType.AUTOASK, null, createTime);
                }
                initOnlineOrderGoods(goodsMap, cardMap, createTime, autoaskOrder, snapshotInfo);
            } else if (StringUtils.equals(sendMerchantType, Constants.MerchantType.FACTORY)) {
                String factoryId = goods.getFactoryId();
                OrderInfo orderInfo = f2cMap.get(factoryId);
                if (null == orderInfo) {
                    orderInfo = initOnlineOrder(orderParam, Constants.MerchantType.FACTORY, factoryId, createTime);
                }
                initOnlineOrderGoods(goodsMap, cardMap, createTime, orderInfo, snapshotInfo);
                f2cMap.put(factoryId, orderInfo);
            }
        }

        //重新计算各个orderInfo中的各个价格
        String province = orderParam.getOnline().getAddress().getProvince();
        List<OrderInfo> orderInfoList = new ArrayList<>();
        if (MapUtils.isNotEmpty(f2cMap)) {
            for (Map.Entry<String, OrderInfo> entry : f2cMap.entrySet()) {
                OrderInfo orderInfo = entry.getValue();
                //计算 orderInfo 中的 各个价格
                calcOnlineOrder(goodsMap, isDeliveryFree, orderInfo, province);
                orderInfoList.add(orderInfo);
            }
        }
        if (null != autoaskOrder) {
            calcOnlineOrder(goodsMap, isDeliveryFree, autoaskOrder, province);
            orderInfoList.add(autoaskOrder);
        }

        return orderInfoList;
    }

    private void initOnlineOrderGoods(Map<String, Goods> goodsMap, Map<String, Card> cardMap, Date createTime, OrderInfo order, OrderParam.SnapshotInfo snapshotInfo) {
        List<OrderGoods> orderGoodsList = order.getOrderGoodsList();
        if (CollectionUtils.isEmpty(orderGoodsList)) {
            orderGoodsList = new ArrayList<>();
        }
        OrderGoods orderGoods = initOrderGoods(goodsMap, cardMap, order, createTime, snapshotInfo);
        orderGoodsList.add(orderGoods);
        order.setOrderGoodsList(orderGoodsList);
    }

    private OrderInfo initOnlineOrder(OrderParam orderParam, String merchantType, String merchantId, Date createTime) throws ApiException {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(CodeGenerator.generatorOrderId());
        orderInfo.setSerial(null);
        orderInfo.setUserId(LoginUtil.getSessionInfo().getLoginId());

        orderInfo.setStatus(Constants.OrderStatus.TO_PAY);
        orderInfo.setPayType(orderParam.getPayType());
        orderInfo.setPaySerial(null);
        orderInfo.setPayTime(null);
        orderInfo.setCreateTime(createTime);
        orderInfo.setUserDeleteFlag(false);
        if (StringUtils.isNotEmpty(orderParam.getInvoiceId())) {
            orderInfo.setInvoiceId(orderParam.getInvoiceId());
        }
        orderInfo.setShareStatus(Constants.ShareStatus.NO_SHARE);

        OrderParam.Online online = orderParam.getOnline();
        initOrderDelivery(online, merchantType, merchantId, orderInfo);

        if (StringUtils.equals(merchantType, Constants.MerchantType.AUTOASK)) {
            String serviceProviderId = orderParam.getOnline().getServiceProviderId();
            if (StringUtils.isNotEmpty(serviceProviderId)) {
                orderInfo.setServeType(Constants.OrderServeType.OFFLINE);
                initOrderServe(serviceProviderId, orderInfo);

            } else {
                orderInfo.setServeType(Constants.OrderServeType.ONLINE);
            }
        } else {
            orderInfo.setServeType(Constants.OrderServeType.ONLINE);
        }


        return orderInfo;
    }

    private void initOrderDelivery(OrderParam.Online online, String merchantType, String merchantId, OrderInfo orderInfo) {
        OrderDelivery orderDelivery = new OrderDelivery();
        orderDelivery.setOrderId(orderInfo.getOrderId());
        orderDelivery.setMerchantType(merchantType);
        orderDelivery.setMerchantId(merchantId);
        orderDelivery.setReceiverName(online.getAddress().getName());
        orderDelivery.setReceiverPhone(online.getAddress().getPhone());
        orderDelivery.setProvince(online.getAddress().getProvince());
        orderDelivery.setCity(online.getAddress().getCity());
        orderDelivery.setRegion(online.getAddress().getRegion());
        orderDelivery.setStreet(online.getAddress().getStreet());
        orderDelivery.setDetailAddress(online.getAddress().getDetail());

        orderInfo.setOrderDelivery(orderDelivery);
    }

    private void initOrderServe(String serviceProviderId, OrderInfo orderInfo) {
        OrderServe orderServe = new OrderServe();
        orderServe.setOrderId(orderInfo.getOrderId());
        orderServe.setMechanicId(null);
        orderServe.setServiceProviderId(serviceProviderId);

        orderInfo.setOrderServe(orderServe);
    }

    private void calcOnlineOrder(Map<String, Goods> goodsMap, boolean isDeliveryFree, OrderInfo orderInfo, String province) throws ApiException {
        List<OrderGoods> orderGoodsList = orderInfo.getOrderGoodsList();
        BigDecimal snapshotTotalPrice = new BigDecimal(0);
        BigDecimal discountTotalPrice = new BigDecimal(0);
        BigDecimal totalWeight = new BigDecimal(0);
        List<String> goodsIdList = new ArrayList<>(orderGoodsList.size());
        for (OrderGoods orderGoods : orderGoodsList) {
            snapshotTotalPrice = snapshotTotalPrice.add(orderGoods.getSnapshotPrice());
            discountTotalPrice = discountTotalPrice.add(orderGoods.getDiscountPrice());
            Goods goods = goodsMap.get(orderGoods.getGoodsSnapshotId());
            totalWeight = totalWeight.add(goods.getWeight());

            goodsIdList.add(goods.getGoodsId());
        }
        BigDecimal deliveryFee = new BigDecimal(0);
        if (!isDeliveryFree) {
            deliveryFee = expressTemplateService.getExpressPrice(snapshotTotalPrice.toString(), province, totalWeight.toString());
        }
        orderInfo.setDeliveryFee(deliveryFee);
        orderInfo.setSnapshotPrice(snapshotTotalPrice);
        orderInfo.setDiscountPrice(discountTotalPrice);
        String serveType = orderInfo.getServeType();
        if (StringUtils.equals(serveType, Constants.OrderServeType.ONLINE)) {
            orderInfo.setServiceFee(new BigDecimal(0));
        } else {
            BigDecimal serviceFee = productCategoryService.getServiceFee(goodsIdList);
            orderInfo.setServiceFee(serviceFee);

        }
        orderInfo.setPayPrice(snapshotTotalPrice.add(deliveryFee).add(orderInfo.getServiceFee()).subtract(discountTotalPrice));
    }


    private OrderGoods initOrderGoods(Map<String, Goods> goodsMap, Map<String, Card> cardMap, OrderInfo orderInfo, Date createTime, OrderParam.SnapshotInfo snapshotInfo) {
        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setOrderGoodsId(CodeGenerator.uuid());
        orderGoods.setOrderId(orderInfo.getOrderId());
        orderGoods.setGoodsSnapshotId(snapshotInfo.getGoodsSnapshotId());
        orderGoods.setNum(snapshotInfo.getNum());
        Goods goods = goodsMap.get(snapshotInfo.getGoodsSnapshotId());
        BigDecimal itemPrice;
        if (StringUtils.equals(orderInfo.getServeType(), Constants.OrderServeType.ONLINE)) {
            itemPrice = goods.getOnlinePrice();
        } else {
            itemPrice = goods.getOfflinePrice();
        }
        orderGoods.setSnapshotPrice(itemPrice.multiply(new BigDecimal(snapshotInfo.getNum())));
        ArrayList<String> cardList = snapshotInfo.getCardList();
        int discountNum = CollectionUtils.isEmpty(cardList) ? 0 : cardList.size();
        orderGoods.setDiscountPrice(itemPrice.multiply(new BigDecimal(discountNum)));
        orderGoods.setPayPrice(orderGoods.getSnapshotPrice().subtract(orderGoods.getDiscountPrice()));
        if (CollectionUtils.isNotEmpty(cardList)) {
            StringBuilder cardIdsBuilder = new StringBuilder();
            ArrayList<OrderGoodsCard> orderGoodsCardList = new ArrayList<>();
            for (int index = 0; index < cardList.size(); index++) {
                String cardId = cardList.get(index);
                Card card = cardMap.get(cardId);
                cardIdsBuilder.append(cardList.get(index));
                if (index != cardList.size() - 1) {
                    cardIdsBuilder.append(",");
                }
                OrderGoodsCard orderGoodsCard = new OrderGoodsCard();
                orderGoodsCard.setOrderGoodsId(orderGoods.getOrderGoodsId());
                orderGoodsCard.setCardId(cardId);
                orderGoodsCard.setCardTypeId(card.getCardTypeId());
                orderGoodsCardList.add(orderGoodsCard);
            }
            orderGoods.setOrderGoodsCardList(orderGoodsCardList);
        }
        return orderGoods;
    }


    /**
     * 获取线上发货方类型
     *
     * @param goods
     * @return
     * @throws ApiException
     */
    private String getSendMerchantType(Goods goods) throws ApiException {
        if (StringUtils.equals(goods.getType(), Constants.GoodsType.ALL)) {
            return Constants.MerchantType.AUTOASK;
        } else if (StringUtils.equals(goods.getType(), Constants.GoodsType.ONLINE)) {
            return Constants.MerchantType.FACTORY;
        } else {
            throw new ApiException("数据异常");
        }
    }
}
