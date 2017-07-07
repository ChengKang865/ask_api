package com.autoask.service.impl.pay;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.exception.ApiException;
import com.autoask.common.phone.MessageService;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.log.OrderLogDao;
import com.autoask.dao.merchant.ServiceProviderDao;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.entity.mysql.OrderAppointValidate;
import com.autoask.entity.mysql.OrderInfo;
import com.autoask.entity.mysql.OrderServe;
import com.autoask.mapper.OrderAppointValidateMapper;
import com.autoask.mapper.OrderInfoMapper;
import com.autoask.service.card.CardService;
import com.autoask.service.pay.PayService;
import com.autoask.service.product.GoodsNumService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyy on 2016/12/14.
 */
@Service
public class PayServiceImpl implements PayService {

    private static final Logger LOG = LoggerFactory.getLogger(PayServiceImpl.class);

//    @Autowired
//    private GoodsNumService goodsNumService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private CardService cardService;

    @Autowired
    private OrderLogDao orderLogDao;

    @Autowired
    private ServiceProviderDao serviceProviderDao;
    
    @Autowired
    OrderAppointValidateMapper orderAppointValidateMapper;

    @Override
    public void updateOrderPayed(JSONObject jsonObject) throws ApiException {
        LOG.info("update order payed:{}", jsonObject.toJSONString());
        JSONObject chargeObject = jsonObject.getJSONObject("data").getJSONObject("object");
        String chargeId = chargeObject.getString("id");
        List<OrderInfo> orderInfoList = orderInfoMapper.getOrderListByPaySerial(chargeId, Constants.OrderStatus.TO_PAY);
        if (CollectionUtils.isNotEmpty(orderInfoList)) {
            //库存 减少
//            goodsNumService.reduceGoodsNumByOrderList(orderInfoList);
            //card 状态更新为 used
            cardService.updateCardUsed(orderInfoList);
            //订单状态更新为已经付款
            orderInfoMapper.updateOrderPayedByPaySerial(chargeId, Constants.OrderStatus.PAYED);
            //记录日志
            ArrayList<String> orderIds = new ArrayList<>();
            for (OrderInfo orderInfo : orderInfoList) {
                orderIds.add(orderInfo.getOrderId());
            }
            orderLogDao.saveOrderLogList(orderIds, Constants.OrderLogOperatorType.PAY, null, Constants.LoginType.SYSTEM, null);
            sendNoticeMsg(orderInfoList);
        }

    }

    @Override
    public void updateOrderPayed(String serial) throws ApiException {
        List<OrderInfo> orderInfoList = orderInfoMapper.getOrderListBySerial(serial);
        if (CollectionUtils.isNotEmpty(orderInfoList)) {
            //库存 减少
//            goodsNumService.reduceGoodsNumByOrderList(orderInfoList);
            //card 状态更新为 used
            cardService.updateCardUsed(orderInfoList);
            //订单状态更新为已经付款
            orderInfoMapper.updateOrderPayedBySerial(serial, Constants.OrderStatus.PAYED);

            //记录日志
            ArrayList<String> orderIds = new ArrayList<>();
            for (OrderInfo orderInfo : orderInfoList) {
                orderIds.add(orderInfo.getOrderId());
            }
            orderLogDao.saveOrderLogList(orderIds, Constants.OrderLogOperatorType.PAY, null, Constants.LoginType.SYSTEM, null);
            sendNoticeMsg(orderInfoList);
        }

    }

    private void sendNoticeMsg(List<OrderInfo> orderInfoList) throws ApiException {
        for (OrderInfo orderInfo : orderInfoList) {
            if (StringUtils.equals(orderInfo.getServeType(), Constants.OrderServeType.OFFLINE)) {
                OrderServe orderServe = orderInfo.getOrderServe();
                if (null == orderServe) {
                    return;
                }
                String serviceProviderId = orderServe.getServiceProviderId();
                ServiceProvider serviceProvider = serviceProviderDao.findById(serviceProviderId);
                if (null == serviceProvider) {
                    return;
                }
               String phone = serviceProvider.getPhone();
                MessageService.sendNoticeMsg(phone);
            }
        }
        
    }
}
