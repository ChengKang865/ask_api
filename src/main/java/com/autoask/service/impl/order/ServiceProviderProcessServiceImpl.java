package com.autoask.service.impl.order;

import com.autoask.common.exception.ApiException;
import com.autoask.common.phone.MessageService;
import com.autoask.common.util.*;
import com.autoask.dao.log.OrderLogDao;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.entity.mysql.*;
import com.autoask.mapper.OrderAppointValidateMapper;
import com.autoask.mapper.OrderInfoMapper;
import com.autoask.mapper.OrderServeMapper;
import com.autoask.service.merchant.ServiceProviderService;
import com.autoask.service.order.OrderShareService;
import com.autoask.service.order.ServiceProviderProcessService;
import com.autoask.service.product.GoodsNumService;
import com.autoask.service.refund.RefundService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @author hyy
 * @create 2016-12-12 16:59
 */
@Service
public class ServiceProviderProcessServiceImpl implements ServiceProviderProcessService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderAppointValidateMapper orderAppointValidateMapper;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private OrderLogDao orderLogDao;

    @Autowired
    private OrderShareService orderShareService;

    @Override
    public void updateAppointConfirm(String orderId) throws ApiException {
        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        checkParam(orderInfo, Constants.OrderServeType.OFFLINE, Arrays.asList(Constants.OrderStatus.PAYED));

        //更新订单状态为 confirmed_sp
        orderInfoMapper.updateOrderInfoStatus(orderId, Constants.OrderStatus.CONFIRM_SP);

        //插入 validate
        OrderServe orderServe = orderInfo.getOrderServe();
        String code = tryGetCode(orderServe.getServiceProviderId());
        User user = orderInfo.getUser();
        OrderAppointValidate orderAppointValidate = new OrderAppointValidate();
        orderAppointValidate.setOrderId(orderId);
        orderAppointValidate.setServiceProviderId(orderServe.getServiceProviderId());
        orderAppointValidate.setUserId(orderInfo.getUserId());
        orderAppointValidate.setUserPhone(user.getPhone());
        orderAppointValidate.setCode(code);
        orderAppointValidate.setCreateTime(DateUtil.getDate());
        orderAppointValidate.setValidateFlag(false);
        orderAppointValidateMapper.insert(orderAppointValidate);

        //发送到短信通知来取货
        ServiceProvider serviceProvider = serviceProviderService.findById(orderServe.getServiceProviderId());
        MessageService.sendValidateMsg(user.getPhone(), serviceProvider.getName(), code);
        //保存日志
        orderLogDao.saveOrderLog(orderId, Constants.OrderLogOperatorType.CONFIRM, null, LoginUtil.getLoginType(), LoginUtil.getLoginId());
    }

    private String tryGetCode(String serviceProviderId) {
        String code = null;
        while (null == code) {
            code = CodeGenerator.generateValidateCode();
            Example example = new Example(OrderAppointValidate.class);
            example.createCriteria().andEqualTo("serviceProviderId", serviceProviderId)
                    .andEqualTo("validateFlag", Constants.ValidateFlag.NOT_VALIDATE)
                    .andEqualTo("code", code);
            int count = orderAppointValidateMapper.selectCountByExample(example);
            if (count != 0) {
                code = null;
            }
        }
        return code;
    }

    @Override
    public String updateAppointValidate(String code) throws ApiException {
        String loginType = LoginUtil.getSessionInfo().getLoginType();
        String serviceProviderId = LoginUtil.getSessionInfo().getLoginId();
        if (!StringUtils.equals(loginType, Constants.MerchantType.SERVICE_PROVIDER)) {
            throw new ApiException("没有权限");
        }
        OrderAppointValidate orderAppointValidate = orderAppointValidateMapper.getByCode(serviceProviderId, code);
        if (null == orderAppointValidate) {
            throw new ApiException("提货码不存在");
        }
        if (orderAppointValidate.getValidateFlag()) {
            throw new ApiException("提货码已经验证过");
        }
        String orderId = orderAppointValidate.getOrderId();
        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        if (null == orderInfo) {
            throw new ApiException("订单异常");
        }
        if (!StringUtils.equals(orderInfo.getStatus(), Constants.OrderStatus.CONFIRMED) && !StringUtils.equals(orderInfo.getStatus(), Constants.OrderStatus.RECEIVED)) {
            throw new ApiException("订单异常");
        }
        //更新为已经验证
        orderAppointValidateMapper.updateValidate(orderId);
        //更新订单的状态
        orderInfoMapper.updateOrderInfoStatus(orderId, Constants.OrderStatus.VALIDATED);

        //保存日志
        orderLogDao.saveOrderLog(orderId, Constants.OrderLogOperatorType.VALIDATE, null, LoginUtil.getLoginType(), LoginUtil.getLoginId());
        return orderAppointValidate.getOrderId();
    }

    private void checkParam(OrderInfo orderInfo, String allowServeType, List<String> allowStatusList) throws ApiException {
        if (null == orderInfo || null == orderInfo.getOrderServe() || !StringUtils.equals(allowServeType, orderInfo.getServeType())) {
            throw new ApiException("参数非法");
        }
        if (!allowStatusList.contains(orderInfo.getStatus())) {
            throw new ApiException("参数非法");
        }
        LoginUtil.checkAccessPermission(Constants.LoginType.SERVICE_PROVIDER, orderInfo.getOrderServe().getServiceProviderId());
    }
}
