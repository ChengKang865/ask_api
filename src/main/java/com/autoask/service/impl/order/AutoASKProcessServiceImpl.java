package com.autoask.service.impl.order;

import com.autoask.common.exception.ApiException;
import com.autoask.common.phone.MessageService;
import com.autoask.common.util.BigDecimalUtil;
import com.autoask.common.util.Constants;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.log.OrderLogDao;
import com.autoask.entity.mysql.OrderDelivery;
import com.autoask.entity.mysql.OrderInfo;
import com.autoask.entity.mysql.OrderServe;
import com.autoask.mapper.OrderDeliveryMapper;
import com.autoask.mapper.OrderInfoMapper;
import com.autoask.pay.pingpp.PingppRefundsUtil;
import com.autoask.service.card.CardService;
import com.autoask.service.order.AutoASKProcessService;
import com.autoask.service.order.OrderShareService;
import com.autoask.service.product.GoodsNumService;
import com.autoask.service.refund.RefundService;
import com.pingplusplus.model.Refund;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hyy on 2016/12/12.
 */
@Service
public class AutoASKProcessServiceImpl implements AutoASKProcessService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderDeliveryMapper orderDeliveryMapper;

    @Autowired
    private RefundService refundService;

    @Autowired
    private OrderLogDao orderLogDao;

    @Autowired
    private OrderShareService orderShareService;

    @Override
    public void updateAutoASKConfirm(String orderId, String expressCompany, String deliverySerial) throws ApiException {
        String loginType = LoginUtil.getSessionInfo().getLoginType();
        if (!StringUtils.equals(loginType, Constants.LoginType.STAFF)) {
            throw new ApiException("没有权限");
        }
        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        if (null == orderInfo) {
            throw new ApiException("订单不存在");
        }
        OrderDelivery orderDelivery = orderInfo.getOrderDelivery();
        if (null == orderDelivery) {
            throw new ApiException("参数非法");
        }
        if (!StringUtils.equals(orderDelivery.getMerchantType(), Constants.MerchantType.AUTOASK)) {
            throw new ApiException("没有权限");
        }
        if (StringUtils.isEmpty(expressCompany) || StringUtils.isEmpty(deliverySerial)) {
            throw new ApiException("快递公司/快递单号不能为空");
        }
        //订单状态
        if (StringUtils.equals(orderInfo.getServeType(), Constants.OrderServeType.ONLINE)) {
            if (!StringUtils.equals(orderInfo.getStatus(), Constants.OrderStatus.PAYED)) {
                throw new ApiException("状态非法");
            }
        } else {
            if (!StringUtils.equals(orderInfo.getStatus(), Constants.OrderStatus.CONFIRM_SP)) {
                throw new ApiException("状态非法");
            }
        }

        orderDeliveryMapper.updateExpressInfo(orderId, expressCompany, deliverySerial);
        //线上的confirmed 即是发货中
        orderInfoMapper.updateOrderInfoStatus(orderId, Constants.OrderStatus.CONFIRMED);

        //记录日志
        orderLogDao.saveOrderLog(orderId, Constants.OrderLogOperatorType.CONFIRM, null, LoginUtil.getLoginType(), LoginUtil.getLoginId());
    }

    @Override
    public void updateRefuse(String orderId) throws ApiException {
        String loginType = LoginUtil.getSessionInfo().getLoginType();
        if (!StringUtils.equals(Constants.LoginType.STAFF, loginType)) {
            throw new ApiException("没有权限");
        }
        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        if (null == orderInfo) {
            throw new ApiException("订单不存在");
        }

        if (!StringUtils.equals(orderInfo.getStatus(), Constants.OrderStatus.PAYED)) {
            throw new ApiException("状态非法");
        }


        //更新订单状态为refused
        refundService.updateOrderRefused(orderInfo);

        //订单的日志记录在refundService中已经记录
    }


    @Override
    public void updateAppointRefuse(String orderId) throws ApiException {
        OrderInfo orderInfo = orderInfoMapper.getDetail(orderId);
        //检查参数
        checkParam(orderInfo, Constants.OrderServeType.OFFLINE, Arrays.asList(Constants.OrderStatus.PAYED));

        //更新订单为线上以及为payed
        Example example = new Example(OrderInfo.class);
        example.createCriteria().andEqualTo("orderId", orderId);
        OrderInfo updateEntity = new OrderInfo();
        updateEntity.setServeType(Constants.OrderServeType.ONLINE);
        updateEntity.setStatus(Constants.OrderStatus.PAYED);
        orderInfoMapper.updateByExampleSelective(updateEntity, example);

        //删除order_share 以及 order_goods_share 中的 service_provider中的信息
        orderShareService.updateOrderShareNoServiceProvider(orderId);

        //保存日志
        orderLogDao.saveOrderLog(orderId, Constants.OrderLogOperatorType.REFUSE_SP, null, LoginUtil.getLoginType(), LoginUtil.getLoginId());
    }

    private void checkParam(OrderInfo orderInfo, String allowServeType, List<String> allowStatusList) throws ApiException {
        if (null == orderInfo || null == orderInfo.getOrderServe() || !StringUtils.equals(allowServeType, orderInfo.getServeType())) {
            throw new ApiException("参数非法");
        }
        if (!allowStatusList.contains(orderInfo.getStatus())) {
            throw new ApiException("参数非法");
        }
        if (!StringUtils.equals(Constants.LoginType.STAFF, LoginUtil.getLoginType())) {
            throw new ApiException("没有权限");
        }
    }
}
