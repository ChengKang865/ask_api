package com.autoask.service.impl.order;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.*;
import com.autoask.dao.comment.GoodsCommentDao;
import com.autoask.dao.comment.OrderCommentDao;
import com.autoask.dao.invoice.InvoiceDao;
import com.autoask.dao.log.OrderLogDao;
import com.autoask.entity.mongo.comment.GoodsComment;
import com.autoask.entity.mongo.comment.OrderComment;
import com.autoask.entity.mongo.invoice.Invoice;
import com.autoask.entity.mongo.log.OrderLog;
import com.autoask.entity.mongo.user.UserInfo;
import com.autoask.entity.mysql.*;
import com.autoask.mapper.*;
import com.autoask.service.assets.MerchantAssetsRecordService;
import com.autoask.service.card.CardService;
import com.autoask.service.merchant.MerchantService;
import com.autoask.service.order.OrderService;
import com.autoask.service.order.OrderShareService;
import com.autoask.service.user.UserInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @author hyy
 * @create 2016-09-10 12:54
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MerchantAssetsRecordService merchantAssetsRecordService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private OrderLogDao orderLogDao;

    @Autowired
    private OrderCommentDao orderCommentDao;

    @Autowired
    private GoodsCommentDao goodsCommentDao;

    @Autowired
    private OrderShareService orderShareService;

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private CardService cardService;

    @Autowired
    private OrderDeliveryMapper orderDeliveryMapper;


    @Override
    public ListSlice<OrderInfo> getOrderInfoList(List<String> serveTypeList, List<String> statusList, String orderId, String startTime,
                                                 String endTime, String phone, int start, int end) throws ApiException {
        //权限设定
        Long totalCount = orderInfoMapper.countOrderInfoNum(serveTypeList, statusList, orderId, startTime, endTime, phone);
        List<Long> orderIds = orderInfoMapper.getOrderInfoIds(serveTypeList, statusList, orderId, startTime, endTime, phone, start, end);
        List<OrderInfo> orderList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderIds)) {
            orderList = orderInfoMapper.selectOrderInfoList(orderIds);
        }
        if (CollectionUtils.isNotEmpty(orderList)) {
            merchantService.initOrderMerchantName(orderList);
        }
        return new ListSlice<>(orderList, totalCount);
    }

    @Override
    public ListSlice<OrderInfo> getServiceProviderNewOrderList(int start, int limit) throws ApiException {
        String loginId = LoginUtil.getLoginId();
        String loginType = LoginUtil.getLoginType();
        if (!StringUtils.equals(loginType, Constants.LoginType.SERVICE_PROVIDER)) {
            throw new ApiException("没有权限");
        }

        Long totalNum = orderInfoMapper.countServiceProviderNewOrderNum(loginId, Constants.OrderStatus.PAYED);
        List<Long> orderIds = orderInfoMapper.getServiceProviderNewOrderIds(loginId, Constants.OrderStatus.PAYED, start, limit);
        List<OrderInfo> orderInfoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderIds)) {
            orderInfoList = orderInfoMapper.getServiceProviderNewOrderList(orderIds);
        }
        if (CollectionUtils.isNotEmpty(orderInfoList)) {
            merchantService.initOrderMerchantName(orderInfoList);

        }
        return new ListSlice<>(orderInfoList, totalNum);
    }

    @Override
    public ListSlice<OrderInfo> getOfflineOrderList(String startTime, String endTime,
                                                    String productCategoryId, String productId, String goodsId, String goodsName,
                                                    String orderId, List<String> statusList, String phone,
                                                    String serviceProviderId, String mechanicId,
                                                    int start, int limit) throws ApiException {
        //限制查询权限
        if (StringUtils.equals(LoginUtil.getLoginType(), Constants.LoginType.SERVICE_PROVIDER)) {
            serviceProviderId = LoginUtil.getLoginId();
        }

        List<String> goodsIds;
        if (StringUtils.isNotEmpty(productCategoryId) || StringUtils.isNotEmpty(productId)
                || StringUtils.isNotEmpty(goodsId) || StringUtils.isNotEmpty(goodsName)) {
            goodsIds = goodsMapper.getGoodsIdList(productCategoryId, productId, goodsId, goodsName);
            if (CollectionUtils.isEmpty(goodsIds)) {
                return null;
            }
        } else {
            goodsIds = null;
        }

        Long totalNum = orderInfoMapper.countOfflineOrderNum(null, startTime, endTime, goodsIds, orderId, statusList, phone, serviceProviderId, mechanicId);
        List<Long> orderIds = orderInfoMapper.getOfflineOrderIds(null, startTime, endTime, goodsIds, orderId, statusList, phone, serviceProviderId, mechanicId, start, limit);
        List<OrderInfo> orderList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderIds)) {
            orderList = orderInfoMapper.getOfflineOrderList(orderIds);
        }
        //获取维修工以及 服务商以及修理厂的名称
        merchantService.initOrderMerchantName(orderList);
        return new ListSlice<>(orderList, totalNum);
    }

    @Override
    public ListSlice<OrderInfo> getOnlineOrderList(String serveType, String startTime, String endTime, String productCategoryId, String productId, String goodsId, String goodsName,
                                                   String orderId, List<String> statusList,
                                                   String phone, String merchantType, String merchantId,
                                                   int start, int limit) throws ApiException {

        //权限判断
        String loginType = LoginUtil.getSessionInfo().getLoginType();
        String loginId = LoginUtil.getSessionInfo().getLoginId();
        if (!StringUtils.equals(loginType, Constants.LoginType.STAFF)) {
            merchantType = loginType;
            merchantId = loginId;
        }
        List<String> goodsIds = goodsMapper.getGoodsIdList(productCategoryId, productId, goodsId, goodsName);
        Long totalNum = orderInfoMapper.countOnlineOrderNum(serveType, startTime, endTime, goodsIds, orderId, statusList, phone, merchantType, merchantId);
        List<Long> orderIds = orderInfoMapper.getOnlineOrderIds(serveType, startTime, endTime, goodsIds, orderId, statusList, phone, merchantType, merchantId, start, limit);
        List<OrderInfo> orderList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderIds)) {
            orderList = orderInfoMapper.getOnlineOrderList(orderIds);
        }
        merchantService.initOrderMerchantName(orderList);
        return new ListSlice<>(orderList, totalNum);
    }

    @Override
    public OrderInfo getOrderDetailWithShare(String orderId) throws ApiException {
        OrderInfo orderInfo = orderInfoMapper.getDetailWithShare(orderId);
        if (null == orderInfo) {
            throw new ApiException("订单不存在");
        }
        if (null != orderInfo.getOrderServe()) {
            merchantService.initOrderMerchantName(Arrays.asList(orderInfo));
        }
        orderShareService.initOrderShareMerchantName(Arrays.asList(orderInfo.getOrderShare()));
        //初始化 cardIdList
        List<OrderGoods> orderGoodsList = orderInfo.getOrderGoodsList();
        ArrayList<String> cardIdList = new ArrayList<>();
        for (OrderGoods orderGoods : orderGoodsList) {
            List<OrderGoodsCard> orderGoodsCardList = orderGoods.getOrderGoodsCardList();
            if (CollectionUtils.isNotEmpty(orderGoodsCardList)) {
                for (OrderGoodsCard orderGoodsCard : orderGoodsCardList) {
                    cardIdList.add(orderGoodsCard.getCardId());
                }
            }
        }
        orderInfo.setCardIdList(cardIdList);
        //初始化用户的车型资料
        String userId = orderInfo.getUser().getUserId();
        UserInfo userInfo = userInfoService.getByUserId(userId);
        orderInfo.getUser().setUserInfo(userInfo);
        //初始化订单相关的日志
        List<OrderLog> orderLogList = orderLogDao.getOrderLogList(orderId);
        orderInfo.setOrderLogList(orderLogList);
        //初始化用户相关的评论信息
        OrderComment orderComment = orderCommentDao.getOrderCommentByOrderId(orderId);
        if (null != orderComment) {
            List<GoodsComment> goodsCommentList = goodsCommentDao.getGoodsCommentListByOrderId(orderId);
            orderComment.setGoodsCommentList(goodsCommentList);
            orderInfo.setOrderComment(orderComment);
        }
        //订单发票信息
        if (StringUtils.isNotEmpty(orderInfo.getInvoiceId())) {
            Invoice invoice = invoiceDao.findById(orderInfo.getInvoiceId());
            orderInfo.setInvoice(invoice);
        }


        return orderInfo;
    }

    @Override
    public ListSlice getOrderShareCheckList(String orderId, String startTime, String endTime, String shareStatus, int start, int limit) throws ApiException {
        Long totalNum = orderInfoMapper.selectOrderShareNum(orderId, startTime, endTime, shareStatus);
        List<OrderInfo> orderInfoList = orderInfoMapper.selectOrderShareCheckList(orderId, startTime, endTime, shareStatus, start, limit);
        return new ListSlice(orderInfoList, totalNum);
    }

    @Override
    public void updateOrderShareStatus(String orderId, String shareStatus) throws ApiException {
        //checkParam
        if (!StringUtils.equals(shareStatus, Constants.ShareStatus.SHARED) && !StringUtils.equals(shareStatus, Constants.ShareStatus.REFUSED)) {
            throw new ApiException("参数非法");
        }
        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        if (null == orderInfo) {
            throw new ApiException("订单不存在");
        }
        if (!StringUtils.equals(orderInfo.getShareStatus(), Constants.ShareStatus.NO_SHARE)) {
            throw new ApiException("分成已处理");
        }

        //给订单分成
        if (StringUtils.equals(shareStatus, Constants.ShareStatus.SHARED)) {
            //分成订单
            merchantAssetsRecordService.updateMerchantAssetsByOrder(orderId);
        }
        //更新订单已经处理过分成信息
        orderInfoMapper.updateOrderInfoShareStatus(orderId, shareStatus);
    }

    @Override
    public void updateRollBACKOrderInfo() throws ApiException {
        try {
            LOG.info("定时器开始时间========================" + DateUtil.getDate());
            // 查询订单2小时之前的OrderInfo信息
            List<OrderInfo> orderInfoList = orderInfoMapper.getRollBackOrderInfoList();
            // 查询订单2小时之前的OrderInfo信息下面所属的card信息
            List<Card> cardIdList = cardService.getBackCarIdList();
            if (orderInfoList.size() <= 0 || orderInfoList == null) {
                throw new ApiException("未查询到订单信息");
            } else {
                for (OrderInfo item : orderInfoList) {
                    LOG.info("Order信息列表order_id========================" + item.getOrderId());
                    // 修改订单状态为 EXPIRED
                    orderInfoMapper.updateOrderInfoStatus(item.getOrderId(), Constants.OrderStatus.EXPIRED);
                }
                if (cardIdList.size() >= 0 || cardIdList != null) {
                    for (Card card : cardIdList) {
                        LOG.info("CARD信息列表card_id========================" + card.getCardId());
                        cardService.updateCardToUseAndStatus(card.getCardId(), Constants.CardStatus.CHECKED);
                    }
                }
            }
            LOG.info("定时器结束时间========================" + DateUtil.getDate());
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }


    @Override
    public void updateExpress(String orderId, String expressCompany, String expressSerial) throws ApiException {
        if (!StringUtils.equals(LoginUtil.getLoginType(), Constants.LoginType.STAFF)) {
            throw new ApiException("权限非法");
        }
        if (StringUtils.isEmpty(expressCompany)) {
            throw new ApiException("快递公司不能为空");
        }
        if (StringUtils.isEmpty(expressSerial)) {
            throw new ApiException("快递单号不能为空");
        }
        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        if (null == orderInfo) {
            throw new ApiException("订单不存在");
        }
        List<String> statusList = Arrays.asList(Constants.OrderStatus.CONFIRMED);
        if (!statusList.contains(orderInfo.getStatus())) {
            throw new ApiException("状态非法");
        }
        Example example = new Example(OrderDelivery.class);
        example.createCriteria().andEqualTo("orderId", orderId);

        OrderDelivery updateEntity = new OrderDelivery();
        updateEntity.setExpressCompany(expressCompany);
        updateEntity.setDeliverySerial(expressSerial);

        orderDeliveryMapper.updateByExampleSelective(updateEntity, example);
    }
}