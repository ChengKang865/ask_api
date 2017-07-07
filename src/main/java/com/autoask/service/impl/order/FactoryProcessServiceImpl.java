package com.autoask.service.impl.order;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.log.OrderLogDao;
import com.autoask.entity.mysql.OrderInfo;
import com.autoask.mapper.OrderDeliveryMapper;
import com.autoask.mapper.OrderInfoMapper;
import com.autoask.service.order.FactoryProcessService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hyy
 * @create 2016-12-12 16:26
 */
@Service
public class FactoryProcessServiceImpl implements FactoryProcessService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderDeliveryMapper orderDeliveryMapper;

    @Autowired
    private OrderLogDao orderLogDao;

    @Override
    public void updateOnlineConfirm(String orderId, String expressCompany, String deliverySerial) throws ApiException {
        if (StringUtils.isEmpty(expressCompany) || StringUtils.isEmpty(deliverySerial)) {
            throw new ApiException("快递公司/快递单号不能为空");
        }
        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        if (null == orderInfo || null == orderInfo.getOrderDelivery()) {
            throw new ApiException("参数非法");
        }
        if (!StringUtils.equals(Constants.OrderStatus.PAYED, orderInfo.getStatus())) {
            throw new ApiException("参数非法");
        }
        //更新快递公司/快递单号
        orderDeliveryMapper.updateExpressInfo(orderId, expressCompany, deliverySerial);
        //更改订单状态为发货中
        orderInfoMapper.updateOrderInfoStatus(orderId, Constants.OrderStatus.CONFIRMED);
        //记录日志
        orderLogDao.saveOrderLog(orderId, Constants.OrderLogOperatorType.CONFIRM, null, LoginUtil.getLoginType(), LoginUtil.getLoginId());
    }
}
