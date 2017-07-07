package com.autoask.service.impl.order;

import com.autoask.cache.SessionService;
import com.autoask.common.exception.ApiException;
import com.autoask.common.phone.MessageService;
import com.autoask.common.util.*;
import com.autoask.dao.comment.GoodsCommentDao;
import com.autoask.dao.comment.OrderCommentDao;
import com.autoask.dao.log.OrderLogDao;
import com.autoask.dao.product.GoodsSnapshotMetaDao;
import com.autoask.dao.user.UserInfoDao;
import com.autoask.entity.common.OrderStatusUtil;
import com.autoask.entity.mongo.comment.GoodsComment;
import com.autoask.entity.mongo.comment.OrderComment;
import com.autoask.entity.mongo.invoice.Invoice;
import com.autoask.entity.mongo.merchant.Mechanic;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.entity.mongo.product.GoodsSnapshotMeta;
import com.autoask.entity.mongo.user.UserInfo;
import com.autoask.entity.mysql.*;
import com.autoask.entity.param.OrderParam;
import com.autoask.mapper.*;
import com.autoask.pay.pingpp.PingppChargeUtil;
import com.autoask.pay.pingpp.PingppTransferUtil;
import com.autoask.pay.pingpp.config.PingppConfig;
import com.autoask.service.card.CardService;
import com.autoask.service.invoice.InvoiceService;
import com.autoask.service.log.PayLogService;
import com.autoask.service.merchant.MechanicService;
import com.autoask.service.merchant.ServiceProviderService;
import com.autoask.service.order.*;
import com.autoask.service.pay.PayService;
import com.autoask.service.product.GoodsService;
import com.autoask.service.user.UserService;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Transfer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author hyy
 * @create 16/10/23 17:45
 */
@Service
public class UOrderServiceImpl implements UOrderService {

    private static Logger LOG = LoggerFactory.getLogger(UOrderService.class);

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderCommentDao orderCommentDao;

    @Autowired
    private GoodsCommentDao goodsCommentDao;

    @Autowired
    private GoodsSnapshotMapper goodsSnapshotMapper;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Autowired
    private OrderParamService orderParamService;

    @Autowired
    private OrderServeMapper orderServeMapper;

    @Autowired
    private OrderDeliveryMapper orderDeliveryMapper;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private OrderShareService orderShareService;

    @Autowired
    private OrderGoodsCardMapper orderGoodsCardMapper;

    @Autowired
    private OrderShareMapper orderShareMapper;

    @Autowired
    private OrderGoodsShareMapper orderGoodsShareMapper;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private PayService payService;

    @Autowired
    private MerchantAssetsMapper merchantAssetsMapper;

    @Autowired
    private MerchantAssetsRecordMapper merchantAssetsRecordMapper;

    @Autowired
    private AssetsRecordItemMapper assetsRecordItemMapper;

    @Autowired
    private OrderLogDao orderLogDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsSnapshotMetaDao goodsSnapshotMetaDao;

    @Autowired
    private OrderAppointValidateMapper orderAppointValidateMapper;

    @Autowired
    private CardService cardService;

    @Autowired
    private InvoiceService invoiceService;

    @Override
    public Object insertOrderPrePay(OrderParam orderParam, String ip) throws ApiException {

        String userId = LoginUtil.getSessionInfo().getLoginId();
        User user = userService.getUser(userId);
        if (user == null) {
            throw new ApiException("用户信息异常，请重新登录");
        }

        //goodsSnapshotId goods
        Map<String, Goods> goodsMap = orderParamService.getGoodsMap(orderParam);
        //cardId card
        Map<String, Card> cardMap = orderParamService.getCardMap(orderParam);
        orderParamService.initOrderParam(orderParam, goodsMap, cardMap);
        orderParamService.checkParam(orderParam, goodsMap, cardMap);
        List<OrderInfo> orderInfoList = new ArrayList<>();
        if (null != orderParam.getOnline()) {
            List<OrderInfo> onlineOrderList = orderParamService.getOnlineOrderList(orderParam, goodsMap, cardMap);
            orderInfoList.addAll(onlineOrderList);
        }
        String serial = CodeGenerator.uuid();
        for (OrderInfo orderInfo : orderInfoList) {
            orderInfo.setSerial(serial);
            orderShareService.initOrderShare(orderInfo, cardMap, goodsMap);
        }
        //insert order_info
        orderInfoMapper.insertList(orderInfoList);
        List<OrderGoods> orderGoodsList = new ArrayList<>();
        List<OrderServe> orderServeList = new ArrayList<>();
        List<OrderDelivery> orderDeliveryList = new ArrayList<>();
        List<OrderGoodsCard> orderGoodsCardList = new ArrayList<>();
        List<OrderShare> orderShareList = new ArrayList<>();
        List<OrderGoodsShare> orderGoodsShareList = new ArrayList<>();
        for (OrderInfo orderInfo : orderInfoList) {
            orderGoodsList.addAll(orderInfo.getOrderGoodsList());
            OrderServe orderServe = orderInfo.getOrderServe();
            if (null != orderServe) {
                orderServeList.add(orderServe);
            }
            OrderDelivery orderDelivery = orderInfo.getOrderDelivery();
            if (null != orderDelivery) {
                orderDeliveryList.add(orderDelivery);
            }
            for (OrderGoods orderGoods : orderGoodsList) {
                if (CollectionUtils.isNotEmpty(orderGoods.getOrderGoodsCardList())) {
                    orderGoodsCardList.addAll(orderGoods.getOrderGoodsCardList());
                }
            }
            orderShareList.add(orderInfo.getOrderShare());
            orderGoodsShareList.addAll(orderInfo.getOrderShare().getOrderGoodsShareList());
        }
        //insert order_goods
        orderGoodsMapper.insertList(orderGoodsList);
        //insert orderServe
        if (CollectionUtils.isNotEmpty(orderServeList)) {
            orderServeMapper.insertList(orderServeList);
        }
        //insert orderDelivery
        if (CollectionUtils.isNotEmpty(orderDeliveryList)) {
            orderDeliveryMapper.insertList(orderDeliveryList);
        }
        //insert order_goods_card
        if (CollectionUtils.isNotEmpty(orderGoodsCardList)) {
            orderGoodsCardMapper.insertList(orderGoodsCardList);
        }
        //insert order_share
        if (CollectionUtils.isNotEmpty(orderShareList)) {
            orderShareMapper.insertList(orderShareList);
        }
        //insert order_goods_share
        if (CollectionUtils.isNotEmpty(orderGoodsShareList)) {
            orderGoodsShareMapper.insertList(orderGoodsShareList);
        }
        //update card info
        if (CollectionUtils.isNotEmpty(orderGoodsCardList)) {
            for (OrderGoodsCard orderGoodsCard : orderGoodsCardList) {
                cardMapper.updateCardToUse(orderGoodsCard.getCardId(), orderGoodsCard.getOrderGoodsId(), Constants.CardStatus.USED);
            }
        }

        Object result = initResult(orderParam, ip, serial);

        //记录日志
        ArrayList<String> orderIdList = new ArrayList<>(orderInfoList.size());
        for (OrderInfo orderInfo : orderInfoList) {
            orderIdList.add(orderInfo.getOrderId());
        }
        orderLogDao.saveOrderLogList(orderIdList, Constants.OrderLogOperatorType.INSERT, null, LoginUtil.getLoginType(), LoginUtil.getLoginId());

        return result;
    }

    private Object initResult(OrderParam orderParam, String ip, String serial) throws ApiException {
        String channel = orderParam.getChannel();
        String payType = orderParam.getPayType();
        BigDecimal payTotalPrice = orderParam.getPayTotalPrice();
        //如果不需要付款,直接返回
        if (payTotalPrice.compareTo(new BigDecimal(0)) == 0) {
            payService.updateOrderPayed(serial);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("pay_success", true);
            return resultMap;
        }

        Object result;
        Charge charge;

        //需要根据浏览器传递的参数 调用相应的返回
        if (StringUtils.equals(Constants.Channel.PC, channel)) {
            //返回的是一个二维码链接信息 以及 对应的 paySerial
            if (StringUtils.equals(payType, Constants.PayType.WE_CHAT)) {
                charge = PingppChargeUtil.createWxQr(serial, BigDecimalUtil.decimal2Int(payTotalPrice), ip);
                HashMap<String, Object> resultMap = new HashMap<>();
                resultMap.put("paySerial", charge.getId());
                resultMap.put("payUrl", charge.getCredential().get("wx_pub_qr"));
                resultMap.put("amount", payTotalPrice);
                result = resultMap;
            } else if (StringUtils.equals(payType, Constants.PayType.ALI)) {
                HashMap<String, String> extraMap = new HashMap<>();
                extraMap.put("success_url", Constants.ALI_PC_SUCCESS_URL);
                charge = PingppChargeUtil.createCharge(BigDecimalUtil.decimal2Int(payTotalPrice), "autoask汽车备品", "autoask汽车备品", ip, serial,
                        PingppConfig.PayChannel.ALIPAY_PC_DIRECT, extraMap);
                result = charge;
            } else {
                //TODO 银联支付
                throw new ApiException("渠道参数非法");
            }

        } else {
            //m
            if (StringUtils.equals(payType, Constants.PayType.ALI)) {
                HashMap<String, String> extraMap = new HashMap<>();
                extraMap.put("success_url", Constants.ALI_M_SUCCESS_URL);
                charge = PingppChargeUtil.createCharge(BigDecimalUtil.decimal2Int(payTotalPrice), "autoask汽车备品", "autoask汽车备品",
                        ip, serial, PingppConfig.PayChannel.ALIPAY_WAP, extraMap
                );
            } else if (StringUtils.equals(payType, Constants.PayType.WE_CHAT)) {
                //微信公众号支付
                String sessionId = LoginUtil.getSessionInfo().getSessionId();
                String openId = sessionService.getSessionOpenId(sessionId);
                if (StringUtils.isEmpty(openId)) {
                    throw new ApiException("微信支付请在微信浏览器中打开");
                }
                charge = PingppChargeUtil.createChargeWithOpenId(openId, serial,
                        BigDecimalUtil.decimal2Int(payTotalPrice), ip, serial, "serial:" + serial);
            } else {
                //TODO 银联的支付暂时没有
                throw new ApiException("支付渠道参数非法");
            }
            if (null == charge) {
                throw new ApiException("支付失败");
            }
            result = charge;
        }
        orderInfoMapper.setPaySerial(serial, charge.getId());
        return result;
    }

    @Override
    public void updateOrderReceived(String orderId) throws ApiException {
        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        //检查参数是否非法
        checkUpdateServeParam(orderInfo);

        orderInfoMapper.updateOrderReceived(orderId, Constants.OrderStatus.RECEIVED);

        //记录订单日志
        orderLogDao.saveOrderLogList(Arrays.asList(orderId), Constants.OrderLogOperatorType.RECEIVE, null, LoginUtil.getLoginType(), LoginUtil.getLoginId());
    }

    private void checkUpdateServeParam(OrderInfo orderInfo) throws ApiException {
        String loginId = LoginUtil.getSessionInfo().getLoginId();
        if (null == orderInfo) {
            throw new ApiException("参数非法");
        }
        User user = orderInfo.getUser();
        if (null == user || !StringUtils.equals(user.getUserId(), loginId)) {
            throw new ApiException("没有权限");
        }
        String status = orderInfo.getStatus();
        if (StringUtils.equals(orderInfo.getServeType(), Constants.OrderServeType.ONLINE)) {
            if (StringUtils.equals(status, Constants.OrderStatus.RECEIVED) || StringUtils.equals(status, Constants.OrderStatus.COMMENT) || StringUtils.equals(status, Constants.OrderStatus.VALIDATED)) {
                throw new ApiException("该订单已经被确认");
            }
        }
        if (!StringUtils.equals(Constants.OrderStatus.CONFIRMED, orderInfo.getStatus())) {
            throw new ApiException("参数非法");
        }
    }


    @Override
    public ListSlice<OrderInfo> getOrderInfoList(String userId, Integer statusCode, String content, int start, int limit) throws ApiException {
        if (statusCode == null) {
            throw new ApiException("状态非法");
        }
        List<String> goodsIds = null;
        if (StringUtils.isNotEmpty(content)) {
            goodsIds = goodsMapper.getGoodsIdList(null, null, null, content);
            if (CollectionUtils.isEmpty(goodsIds)) {
                return new ListSlice<>(new ArrayList<OrderInfo>(), 0L);
            }
        }
        Long totalNum = orderInfoMapper.countUserOrderNum(userId, statusCode, goodsIds);
        List<Long> orderIds = orderInfoMapper.getUserOrderIdList(userId, statusCode, goodsIds, start, limit);
        if (CollectionUtils.isNotEmpty(orderIds)) {
            List<OrderInfo> orderList = orderInfoMapper.getUserOrderList(orderIds);
            if (CollectionUtils.isNotEmpty(orderList)) {
                //设置修理厂的名称
                setServiceProviderName(orderList);
                //设置订单的各个商品的图片列表
                initGoodsSnapshotMeta(orderList);
            }
            return new ListSlice<>(orderList, totalNum);
        } else {
            return new ListSlice<>(new ArrayList<OrderInfo>(), 0L);
        }

    }

    private void initGoodsSnapshotMeta(List<OrderInfo> orderInfoList) {
        if (null == orderInfoList) {
            return;
        }
        ArrayList<String> snapshotIdList = new ArrayList<>();
        for (OrderInfo orderInfo : orderInfoList) {
            List<OrderGoods> orderGoodsList = orderInfo.getOrderGoodsList();
            for (OrderGoods orderGoods : orderGoodsList) {
                snapshotIdList.add(orderGoods.getGoodsSnapshotId());
            }
        }
        HashMap<String, GoodsSnapshotMeta> idMap = new HashMap<>(snapshotIdList.size());
        List<GoodsSnapshotMeta> goodsSnapshotMetaList = goodsSnapshotMetaDao.find(Query.query(Criteria.where("goodsSnapshotId").in(snapshotIdList)));
        if (CollectionUtils.isNotEmpty(goodsSnapshotMetaList)) {
            for (GoodsSnapshotMeta snapshotMeta : goodsSnapshotMetaList) {
                idMap.put(snapshotMeta.getGoodsSnapshotId(), snapshotMeta);
            }
        }

        for (OrderInfo orderInfo : orderInfoList) {
            List<OrderGoods> orderGoodsList = orderInfo.getOrderGoodsList();
            for (OrderGoods orderGoods : orderGoodsList) {
                orderGoods.getGoodsSnapshot().setGoodsSnapshotMeta(idMap.get(orderGoods.getGoodsSnapshotId()));
            }
        }
    }

    /**
     * 设置线下订单的serviceProviderName
     *
     * @param orderList
     */
    private void setServiceProviderName(List<OrderInfo> orderList) {
        Set<String> serviceProviderIdSet = new HashSet<>(orderList.size());
        for (OrderInfo orderInfo : orderList) {
            if (!StringUtils.equals(orderInfo.getServeType(), Constants.OrderServeType.ONLINE)) {
                //线下订单需要初始化 serviceProviderName
                serviceProviderIdSet.add(orderInfo.getOrderServe().getServiceProviderId());
            }
        }
        List<ServiceProvider> serviceProviderList = serviceProviderService.findByIdCollection(serviceProviderIdSet);
        HashMap<String, String> id2NameMap = new HashMap<>(serviceProviderList.size());
        for (ServiceProvider serviceProvider : serviceProviderList) {
            id2NameMap.put(serviceProvider.getId(), serviceProvider.getName());
        }
        for (OrderInfo orderInfo : orderList) {
            if (!StringUtils.equals(orderInfo.getServeType(), Constants.OrderServeType.ONLINE)) {
                OrderServe orderServe = orderInfo.getOrderServe();
                if (null != orderServe) {
                    orderServe.setServiceProviderName(id2NameMap.get(orderServe.getServiceProviderId()));
                }
            }
        }
    }

    @Override
    public boolean queryOrderPaySucceed(String paySerial) throws ApiException {
        String userId = LoginUtil.getSessionInfo().getLoginId();
        Example example = new Example(OrderInfo.class);
        example.createCriteria().andEqualTo("paySerial", paySerial);
        List<OrderInfo> orderInfoList = orderInfoMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(orderInfoList)) {
            throw new ApiException("订单不存在");
        }
        OrderInfo orderInfo = orderInfoList.get(0);
        if (!StringUtils.equals(orderInfo.getUserId(), userId)) {
            throw new ApiException("没有权限");
        }
        if (StringUtils.equals(Constants.OrderStatus.TO_PAY, orderInfo.getStatus())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void insertOrderComment(OrderComment orderComment) throws ApiException {
        String orderId = orderComment.getOrderId();
        //校验参数是否合法
        OrderInfo orderInfo = checkOrderComment(orderComment, orderId);
        //更新订单为已评论
        orderInfoMapper.updateOrderInfoComment(orderId, Constants.OrderStatus.COMMENT, orderComment.getRate());
        //如果是线下服务需要分成给修理工
        if (!StringUtils.equals(Constants.OrderServeType.ONLINE, orderInfo.getServeType())) {
            OrderServe orderServe = orderInfo.getOrderServe();
            String mechanicId = orderServe.getMechanicId();
            if (StringUtils.isEmpty(mechanicId)) {
                //预约订单，修理工id是前台传送过来的
                mechanicId = orderComment.getMechanicId();
            }
            Mechanic mechanic = mechanicService.findById(mechanicId);
            if (StringUtils.isEmpty(orderServe.getMechanicId())) {
                if (null == mechanic) {
                    throw new ApiException("维修工不存在");
                }
                orderServeMapper.updateMechanicId(orderId, mechanicId);
            }
            //分成修理工红包
            if (null != mechanic && mechanic.getActivated()) {
                OrderShare orderShare = orderShareMapper.getOrderShareByOrderId(orderId);
                BigDecimal handleFee = orderShare.getHandleFee();
                if (handleFee.compareTo(new BigDecimal(0)) != 0) {

                    Integer rate = orderComment.getRate();
                    if (null != rate && rate > 1) {
                        if (orderComment.getRate() == 2 || orderComment.getRate() == 3) {
                            handleFee = handleFee.divide(new BigDecimal(2));
                        }
                        MerchantAssetsRecord record = new MerchantAssetsRecord();
                        record.setRecordId(CodeGenerator.uuid());
                        record.setMerchantType(Constants.MerchantType.MECHANIC);
                        record.setMerchantId(mechanicId);
                        record.setChangeAmount(handleFee);
                        record.setRelatedType(Constants.RecordRelatedType.ORDER);
                        record.setRelatedId(orderId);
                        record.setCreateTime(DateUtil.getDate());
                        merchantAssetsRecordMapper.insert(record);
                        AssetsRecordItem recordItem = new AssetsRecordItem();
                        recordItem.setRecordItemId(CodeGenerator.uuid());
                        recordItem.setRecordId(record.getRecordId());
                        recordItem.setAmount(handleFee);
                        recordItem.setIncomeType(Constants.IncomeType.HANDLE_FEE);
                        assetsRecordItemMapper.insert(recordItem);
                        //更新商户的收入资产 需要累加总资产
                        merchantAssetsMapper.incomeBalance(Constants.MerchantType.MECHANIC, mechanicId, handleFee);
                        //通知修理工 收到评价 收到分成
                        MerchantAssets assets = merchantAssetsMapper.selectForLock(Constants.MerchantType.MECHANIC, mechanicId);

                        MessageService.sendMechanicShareMessage(mechanic.getPhone(), handleFee, assets.getBalance(), Constants.INCOME_REFUND_TIME);
                    }
                }
            }
        }

        //保存评价到mongodb中
        orderCommentDao.save(orderComment);
        //保存goodsComment
        List<GoodsComment> goodsCommentList = orderComment.getGoodsCommentList();
        if (CollectionUtils.isNotEmpty(goodsCommentList)) {
            goodsCommentDao.saveList(goodsCommentList);
        }
        //记录日志
        orderLogDao.saveOrderLogList(Arrays.asList(orderId), Constants.OrderLogOperatorType.COMMENT, null, LoginUtil.getLoginType(), LoginUtil.getLoginId());
    }

    private OrderInfo checkOrderComment(OrderComment orderComment, String orderId) throws ApiException {
        if (!Constants.CommentRate.COMMENT_LIST.contains(orderComment.getRate())) {
            throw new ApiException("评价等级参数非法");
        }
        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        if (null == orderInfo) {
            throw new ApiException("订单不存在");
        }
        if (StringUtils.equals(orderInfo.getServeType(), Constants.OrderServeType.ONLINE)) {
            if (!StringUtils.equals(orderInfo.getStatus(), Constants.OrderStatus.RECEIVED)) {
                throw new ApiException("状态非法");
            }
        } else {
            if (!StringUtils.equals(orderInfo.getStatus(), Constants.OrderStatus.COMPLETE_S)) {
                throw new ApiException("状态非法");
            }
        }
        //检查修理工信息
        if (!StringUtils.equals(orderInfo.getServeType(), Constants.OrderServeType.ONLINE)) {
            //线下订单需要校验修理工信息
            OrderServe orderServe = orderInfo.getOrderServe();
            if (null == orderServe) {
                throw new ApiException("数据异常");
            }
            if (StringUtils.isNotEmpty(orderServe.getMechanicId())) {
                orderComment.setMechanicId(orderServe.getMechanicId());
            }
        }
        String userId = LoginUtil.getLoginId();
        User user = userService.getUser(userId);
        if (null == user) {
            throw new ApiException("数据异常");
        }
        UserInfo userInfo = userInfoDao.findOne(Query.query(Criteria.where("userId").is(userId)));

        orderComment.setId(null);
        orderComment.setCreateTime(DateUtil.getDate());
        orderComment.setUserId(userId);
        OrderComment preComment = orderCommentDao.findOne(Query.query(Criteria.where("orderId").is(orderComment.getOrderId())));
        if (null != preComment) {
            throw new ApiException("订单已经评论");
        }
        //检查所有的商是否都评论了
        List<GoodsComment> goodsCommentList = orderComment.getGoodsCommentList();
        if (CollectionUtils.isEmpty(goodsCommentList)) {
            throw new ApiException("请评论商品");
        }
        List<OrderGoods> orderGoodsList = orderInfo.getOrderGoodsList();
        HashMap<String, GoodsSnapshot> orderGoodsIdMap = new HashMap<>();
        for (OrderGoods orderGoods : orderGoodsList) {
            orderGoodsIdMap.put(orderGoods.getOrderGoodsId(), orderGoods.getGoodsSnapshot());
        }
        for (GoodsComment goodsComment : goodsCommentList) {
            GoodsSnapshot goodsSnapshot = orderGoodsIdMap.get(goodsComment.getOrderGoodsId());
            if (null == goodsSnapshot) {
                throw new ApiException("请提交本订单中的物品评价");
            }
            if (StringUtils.isEmpty(goodsComment.getComment())) {
                throw new ApiException("订单中有未评价的商品");
            }

            goodsComment.setOrderId(orderComment.getOrderId());
            goodsComment.setUserId(LoginUtil.getLoginId());
            goodsComment.setNickname(userInfo.getNickname());
            goodsComment.setUserPhone(user.getPhone());
            goodsComment.setGoodsSnapshotId(goodsSnapshot.getGoodsSnapshotId());
            goodsComment.setGoodsId(goodsSnapshot.getGoodsId());
            goodsComment.setGoodsName(goodsSnapshot.getName());
            goodsComment.setProductId(goodsSnapshot.getProductId());
            goodsComment.setCreateTime(DateUtil.getDate());
        }
        return orderInfo;
    }

    @Override
    public void insertGoodsComment(GoodsComment goodsComment) throws ApiException {
        if (StringUtils.isEmpty(goodsComment.getOrderGoodsId())) {
            throw new ApiException("订单商品id不能为空");
        }
        //判断是否已经评价过
        String orderGoodsId = goodsComment.getOrderGoodsId();
        GoodsComment preGoodsComment = goodsCommentDao.findOne(Query.query(Criteria.where("orderGoodsId").is(goodsComment.getOrderGoodsId())));
        if (null != preGoodsComment) {
            throw new ApiException("该商品已经评价过");
        }
        OrderGoods qry = new OrderGoods();
        qry.setOrderGoodsId(goodsComment.getOrderGoodsId());
        OrderGoods orderGoods = orderGoodsMapper.selectOne(qry);
        if (null == orderGoods) {
            throw new ApiException("订单商品不存在");
        }

        String userId = LoginUtil.getSessionInfo().getLoginId();
        User user = userService.getUser(userId);
        if (null == user) {
            throw new ApiException("数据异常");
        }
        UserInfo userInfo = userInfoDao.findOne(Query.query(Criteria.where("userId").is(userId)));

        GoodsSnapshot snapshotQry = new GoodsSnapshot();
        snapshotQry.setGoodsSnapshotId(orderGoods.getGoodsSnapshotId());
        GoodsSnapshot goodsSnapshot = goodsSnapshotMapper.selectOne(snapshotQry);

        goodsComment.setId(null);
        goodsComment.setOrderGoodsId(orderGoodsId);
        goodsComment.setOrderId(orderGoods.getOrderId());
        goodsComment.setUserId(user.getUserId());
        if (null != userInfo) {
            goodsComment.setNickname(userInfo.getNickname());
        }
        goodsComment.setUserPhone(user.getPhone());
        goodsComment.setGoodsSnapshotId(orderGoods.getGoodsSnapshotId());
        goodsComment.setGoodsId(goodsSnapshot.getGoodsId());
        goodsComment.setGoodsName(goodsSnapshot.getName());
        goodsComment.setProductId(goodsSnapshot.getProductId());
        goodsComment.setCreateTime(DateUtil.getDate());
        //如果没有好评 默认好评
        if (null == goodsComment.getRate()) {
            goodsComment.setRate(Constants.CommentRate.FIVE);
        }
        goodsComment.setCreateTime(DateUtil.getDate());

        goodsCommentDao.save(goodsComment);
    }

    @Override
    public GoodsComment getGoodsComment(String orderGoodsId) throws ApiException {
        return goodsCommentDao.findOne(Query.query(Criteria.where("orderGoodsId").is(orderGoodsId)));
    }

    @Override
    public Map<String, Object> getOrderDetail(String orderId) throws ApiException {
        HashMap<String, Object> resultMapJson = new HashMap<>();

        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        if (null == orderInfo) {
            throw new ApiException("订单不存在");
        }

        initGoodsSnapshotMeta(Arrays.asList(orderInfo));

        List<OrderGoods> orderGoodsList = orderInfo.getOrderGoodsList();
        //获取各个订单产品的评价

        ArrayList<Map<String, Object>> containerListJson = new ArrayList<>();
        //false 是 是否是快递
        buildOrderGoodsContainerJson(orderGoodsList, containerListJson, false);
        //快递费用 
        resultMapJson.put("deliveryFee", orderInfo.getDeliveryFee());
        //折扣费用
        resultMapJson.put("discountPrice", orderInfo.getDiscountPrice());
        //工时费用
        resultMapJson.put("serviceFee", orderInfo.getServiceFee());
        resultMapJson.put("containerList", containerListJson);
        //收货人信息
        resultMapJson.put("orderDelivery", orderInfo.getOrderDelivery());
        //服务店验证码
        String code = "";
        if (!StringUtils.isEmpty(orderInfo.getInvoiceId())) {
            OrderAppointValidate orderAppointValidate = orderAppointValidateMapper.getByOrderId(orderId);
            if (orderAppointValidate != null) {
                code = orderAppointValidate.getCode();
            }
        }
        resultMapJson.put("code", code);
        //优惠卡编码
        Map<String, String> cardMap = cardService.getCarIdList(orderId);
        resultMapJson.put("cardMap", cardMap);
        //发票抬头
        if (!StringUtils.isEmpty(orderInfo.getInvoiceId())) {
            Invoice invoice = invoiceService.getById(orderInfo.getInvoiceId());
            if (invoice != null && invoice.getCommonInvoice() != null) {
                resultMapJson.put("header", invoice.getCommonInvoice().getHeader());
            }
        }
        OrderComment orderComment = orderCommentDao.findOne(Query.query(Criteria.where("orderId").is(orderId)));
        HashMap<String, Object> orderCommentJson = buildOrderCommentJson(orderComment);
        resultMapJson.put("orderComment", orderCommentJson);
        resultMapJson.put("orderId", orderId);
        //服务店信息
        if (null != orderInfo.getOrderServe()) {
            OrderServe orderServe = orderInfo.getOrderServe();
            String serviceProviderId = orderServe.getServiceProviderId();
            ServiceProvider serviceProvider = serviceProviderService.findById(serviceProviderId);
            resultMapJson.put("serviceProviderId", orderServe.getServiceProviderId());
            if (null != serviceProvider) {
                resultMapJson.put("serviceProviderName", serviceProvider.getName());
                resultMapJson.put("serviceProviderPhone", serviceProvider.getPhone());
                resultMapJson.put("serviceAddress", serviceProvider.getAddress());
                //营业时间
                resultMapJson.put("serviceWorkingHours", serviceProvider.getStartTime() + "-" + serviceProvider.getEndTime());
            }
            String mechanicId = orderServe.getMechanicId();
            if (StringUtils.isNotEmpty(mechanicId)) {
                Mechanic mechanic = mechanicService.findById(mechanicId);
                if (null != mechanic) {
                    resultMapJson.put("mechanicId", mechanicId);
                    resultMapJson.put("mechanicName", mechanic.getName());
                }
            }
        }
        return resultMapJson;
    }

    private void buildOrderGoodsContainerJson(List<OrderGoods> orderGoodsList, ArrayList<Map<String, Object>> containerListJson, boolean isDelivery) throws ApiException {
        for (OrderGoods orderGoods : orderGoodsList) {
            HashMap<String, Object> containerItemJson = new HashMap<>();

            GoodsSnapshotMeta goodsSnapshotMeta = orderGoods.getGoodsSnapshot().getGoodsSnapshotMeta();
            if (null != goodsSnapshotMeta && CollectionUtils.isNotEmpty(goodsSnapshotMeta.getPicUrlList())) {
                containerItemJson.put("logoUrl", goodsSnapshotMeta.getPicUrlList().get(0));
            } else {
                containerItemJson.put("logoUrl", null);
            }
            containerItemJson.put("num", orderGoods.getNum());
            containerItemJson.put("productId", orderGoods.getGoodsSnapshot().getProductId());
            containerItemJson.put("goodsName", orderGoods.getGoodsSnapshot().getName());
            containerItemJson.put("goodsNameEn", orderGoods.getGoodsSnapshot().getNameEn());
            containerItemJson.put("orderGoodsId", orderGoods.getOrderGoodsId());
            if (isDelivery) {
                containerItemJson.put("goodsPrice", orderGoods.getGoodsSnapshot().getOnlinePrice());
            } else {
                containerItemJson.put("goodsPrice", orderGoods.getGoodsSnapshot().getOfflinePrice());
            }

            GoodsComment goodsComment = getGoodsComment(orderGoods.getOrderGoodsId());
            if (null != goodsComment) {
                HashMap<String, Object> goodsCommentJson = new HashMap<>();
                goodsCommentJson.put("rate", goodsComment.getRate());
                goodsCommentJson.put("comment", goodsComment.getComment());
                goodsCommentJson.put("picUrlList", goodsComment.getPicUrlList());

                containerItemJson.put("goodsComment", goodsCommentJson);
            } else {
                containerItemJson.put("goodsComment", null);
            }

            containerListJson.add(containerItemJson);
        }
    }

    private HashMap<String, Object> buildOrderCommentJson(OrderComment orderComment) {
        if (null != orderComment) {
            HashMap<String, Object> commentJson = new HashMap<>();
            commentJson.put("comment", orderComment.getComment());
            commentJson.put("rate", orderComment.getRate());
            commentJson.put("picUrlList", orderComment.getPicUrlList());
            return commentJson;
        }
        return null;
    }

    @Override
    public void deleteUserOrder(String orderId) throws ApiException {
        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        if (null == orderInfo) {
            throw new ApiException("订单不存在");
        }
        if (!StringUtils.equals(orderInfo.getUserId(), LoginUtil.getSessionInfo().getLoginId())) {
            throw new ApiException("没有权限");
        }
        OrderInfo updateEntity = new OrderInfo();
        updateEntity.setUserDeleteFlag(true);
        Example queryExp = new Example(OrderInfo.class);
        queryExp.createCriteria().andEqualTo("orderId", orderId);
        orderInfoMapper.updateByExampleSelective(updateEntity, queryExp);
    }


    @Override
    public Object getOrderRePay(String orderId, String channel, String payType, String ip) throws ApiException {
        OrderInfo orderInfo = checkRePayParam(orderId, channel, payType);
        return initRePay(channel, payType, orderInfo, ip);
    }

    private OrderInfo checkRePayParam(String orderId, String channel, String payType) throws ApiException {
        if (StringUtils.isEmpty(orderId)) {
            throw new ApiException("订单号不能为空");
        }
        if (StringUtils.isEmpty(channel)) {
            throw new ApiException("支付渠道不能为空");
        }
        if (!Constants.PayType.NO_CASH_TYPES.contains(payType)) {
            throw new ApiException("支付方法非法");
        }
        OrderInfo order = orderInfoMapper.getDetail(orderId);

        String userId = LoginUtil.getSessionInfo().getLoginId();
        User user = userService.getUser(userId);
        if (user == null) {
            throw new ApiException("用户信息异常，请重新登录");
        }
        if (null == order) {
            throw new ApiException("订单不存在");
        }
        if (!StringUtils.equals(userId, order.getUserId())) {
            throw new ApiException("没有权限");
        }

        return order;
    }

    private Object initRePay(String channel, String payType, OrderInfo orderInfo, String ip) throws ApiException {
        Object result;
        Charge charge;

        List<OrderInfo> orderList = orderInfoMapper.getOrderListByPaySerial(orderInfo.getPaySerial(), Constants.OrderStatus.TO_PAY);
        if (CollectionUtils.isEmpty(orderList)) {
            throw new ApiException("订单状态异常");
        }
        String nowSerial = CodeGenerator.uuid();

        //需要根据浏览器传递的参数 调用相应的返回
        if (StringUtils.equals(Constants.Channel.PC, channel)) {
            //返回的是一个二维码链接信息 以及 对应的 paySerial
            if (StringUtils.equals(payType, Constants.PayType.WE_CHAT)) {
                charge = PingppChargeUtil.createWxQr(nowSerial, BigDecimalUtil.decimal2Int(orderInfo.getPayPrice()), ip);
                HashMap<String, Object> resultMap = new HashMap<>();
                resultMap.put("paySerial", charge.getId());
                resultMap.put("payUrl", (String) charge.getCredential().get("wx_pub_qr"));
                resultMap.put("amount", orderInfo.getPayPrice());
                result = resultMap;
            } else if (StringUtils.equals(payType, Constants.PayType.ALI)) {
                HashMap<String, String> extraMap = new HashMap<>();
                extraMap.put("success_url", Constants.ALI_PC_SUCCESS_URL);
                charge = PingppChargeUtil.createCharge(BigDecimalUtil.decimal2Int(orderInfo.getPayPrice()), "autoask汽车备品", "autoask汽车备品", ip, nowSerial,
                        PingppConfig.PayChannel.ALIPAY_PC_DIRECT, extraMap);
                result = charge;
            } else {
                //TODO 银联支付
                throw new ApiException("渠道参数非法");
            }

        } else {
            //m
            if (StringUtils.equals(payType, Constants.PayType.ALI)) {
                HashMap<String, String> extraMap = new HashMap<>();
                extraMap.put("success_url", Constants.ALI_M_SUCCESS_URL);
                charge = PingppChargeUtil.createCharge(BigDecimalUtil.decimal2Int(orderInfo.getPayPrice()), "autoask汽车备品", "autoask汽车备品",
                        ip, nowSerial, PingppConfig.PayChannel.ALIPAY_WAP, extraMap
                );
            } else if (StringUtils.equals(payType, Constants.PayType.WE_CHAT)) {
                //微信公众号支付
                String sessionId = LoginUtil.getSessionInfo().getSessionId();
                String openId = sessionService.getSessionOpenId(sessionId);
                if (StringUtils.isEmpty(openId)) {
                    throw new ApiException("微信支付请在微信浏览器中打开");
                }
                charge = PingppChargeUtil.createChargeWithOpenId(openId, nowSerial,
                        BigDecimalUtil.decimal2Int(orderInfo.getPayPrice()), ip, nowSerial, "serial:" + nowSerial);
            } else {
                //TODO 银联的支付暂时没有
                throw new ApiException("支付渠道参数非法");
            }
            if (null == charge) {
                throw new ApiException("支付失败");
            }
            result = charge;
        }

        OrderInfo updateEntity = new OrderInfo();
        updateEntity.setId(orderInfo.getId());
        updateEntity.setSerial(nowSerial);
        updateEntity.setPaySerial(charge.getId());
        orderInfoMapper.updateByPrimaryKeySelective(updateEntity);

        return result;
    }

    @Override
    public void updateOrderCompleteS(String orderId) throws ApiException {
        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        if (null == orderInfo) {
            throw new ApiException("订单不存在");
        }
        User user = orderInfo.getUser();
        if (null == user) {
            throw new ApiException("订单异常");
        }
        if (!StringUtils.equals(Constants.OrderServeType.OFFLINE, orderInfo.getServeType())) {
            throw new ApiException("参数非法");
        }
        if (!StringUtils.equals(Constants.OrderStatus.VALIDATED, orderInfo.getStatus())) {
            throw new ApiException("请刷新订单");
        }
        if (!StringUtils.equals(user.getUserId(), LoginUtil.getLoginId())) {
            throw new ApiException("没有权限");
        }

        orderInfoMapper.updateOrderInfoStatus(orderId, Constants.OrderStatus.COMPLETE_S);
        //记录日志
        orderLogDao.saveOrderLog(orderId, Constants.OrderLogOperatorType.COMPLETE_S, null, LoginUtil.getLoginType(), LoginUtil.getLoginId());
    }
}