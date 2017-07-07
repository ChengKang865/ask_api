package com.autoask.service.impl.refund;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.exception.ApiException;
import com.autoask.common.phone.MessageService;
import com.autoask.common.util.BigDecimalUtil;
import com.autoask.common.util.Constants;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.log.OrderLogDao;
import com.autoask.entity.mysql.OrderDelivery;
import com.autoask.entity.mysql.OrderInfo;
import com.autoask.entity.mysql.OrderServe;
import com.autoask.mapper.OrderInfoMapper;
import com.autoask.pay.pingpp.PingppRefundsUtil;
import com.autoask.service.card.CardService;
import com.autoask.service.product.GoodsNumService;
import com.autoask.service.refund.RefundService;
import com.pingplusplus.model.Refund;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author hyy
 * @create 2016-12-13 14:49
 */
@Service
public class RefundServiceImpl implements RefundService {

    private static final Logger LOG = LoggerFactory.getLogger(RefundServiceImpl.class);

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private GoodsNumService goodsNumService;

    @Autowired
    private CardService cardService;

    @Autowired
    private OrderLogDao orderLogDao;

    @Override
    public void updateOrderRefused(String orderId) throws ApiException {
        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        updateOrderRefused(orderInfo);
    }

    @Override
    public void updateOrderRefused(OrderInfo orderInfo) throws ApiException {
        if (null == orderInfo) {
            throw new ApiException("参数异常");
        }
        String loginType = LoginUtil.getLoginType();
        if (!StringUtils.equals(Constants.LoginType.STAFF, loginType)) {
            throw new ApiException("没有权限");
        }
        //checkParam

        //退款 退库存 退卡
        refuseCommon(orderInfo);

        //记录日志
        orderLogDao.saveOrderLog(orderInfo.getOrderId(), Constants.OrderLogOperatorType.REFUSE, null, LoginUtil.getLoginType(), LoginUtil.getLoginId());
    }

    private void refuseCommon(OrderInfo orderInfo) throws ApiException {
        //判断是否是所有的商品通过卡退款
        if (orderInfo.getPayPrice().compareTo(BigDecimal.ZERO) == 0) {
            //退款成功了
            // 退库存
//            goodsNumService.updateGoodsNumBack(orderInfo);
            //重置卡的状态
            cardService.updateCardRestByOrderId(orderInfo);
            //更新订单的状态为refunded
            orderInfoMapper.updateOrderRefunded(orderInfo.getOrderId(), Constants.OrderStatus.REFUNDED);
        } else {
            //调用ping++退款
            Refund refund = PingppRefundsUtil.refund(orderInfo.getPaySerial(), BigDecimalUtil.decimal2Int(orderInfo.getPayPrice()), "AutoASK退款");
            if (null == refund) {
                throw new ApiException("退款失败");
            }

            // 退库存
//            goodsNumService.updateGoodsNumBack(orderInfo);
            //重置卡的状态
            cardService.updateCardRestByOrderId(orderInfo);
            //更新订单状态为refused
            orderInfoMapper.updateOrderInfoStatus(orderInfo.getOrderId(), Constants.OrderStatus.REFUSED);

            //设置退款refund_id
            orderInfoMapper.setRefundSerial(refund.getId(), orderInfo.getOrderId(), LoginUtil.getSessionInfo().getLoginType(), LoginUtil.getSessionInfo().getLoginId());
        }
        //发送退款短信
        MessageService.sendRefundMsg(orderInfo.getUser().getPhone(), orderInfo.getOrderId());
    }

    @Override
    public void updateOrderRefund(JSONObject jsonObject) throws ApiException {
        LOG.info("update order refund {}", jsonObject.toJSONString());
        JSONObject refundObj = jsonObject.getJSONObject("data").getJSONObject("object");
        String refundSerial = refundObj.getString("id");
        OrderInfo orderInfo = orderInfoMapper.getDetailByRefundSerial(refundSerial);
        if (null == orderInfo) {
            LOG.error("refund serial not found {}", refundSerial);
            return;
        }
        if (StringUtils.equals(Constants.OrderStatus.REFUSED, orderInfo.getStatus())) {

            //更新订单为已经退款
            orderInfoMapper.updateOrderRefunded(orderInfo.getOrderId(), Constants.OrderStatus.REFUNDED);

            //记录日志
            orderLogDao.saveOrderLog(orderInfo.getOrderId(), Constants.OrderLogOperatorType.REFUND, null, Constants.LoginType.SYSTEM, null);
        }
    }
}
