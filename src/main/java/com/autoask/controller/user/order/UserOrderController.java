package com.autoask.controller.user.order;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.IPFetchUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.LoginUtil;
import com.autoask.controller.common.BaseController;
import com.autoask.entity.param.OrderParam;
import com.autoask.service.order.UOrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author hyy
 * @create 2016-09-09 20:33
 */
@Controller
@RequestMapping("/user/order/")
public class UserOrderController extends BaseController {

    private static Logger LOG = LoggerFactory.getLogger(UserOrderController.class);

    @Autowired
    private UOrderService uOrderService;


    /**
     * 查询一次支付是否成功
     * 客户端如果接收到为false应该一直转圈轮询直到获取结果成功为止；
     *
     * @param paySerial 支付序列号
     * @return
     */
    @RequestMapping(value = "/hasPayed/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo queryPaySucceed(@RequestParam("paySerial") String paySerial) {
        try {
            boolean succeed = uOrderService.queryOrderPaySucceed(paySerial);
            return ResponseDo.buildSuccess(succeed);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 获取订单列表
     *
     * @param statusCode
     * @param start
     * @param limit
     * @return
     */
    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo list(@RequestParam(value = "statusCode", required = false) Integer statusCode,
                           @RequestParam(value = "content", required = false) String content,
                           @RequestParam("start") int start, @RequestParam("limit") int limit) {
        LOG.info("enter into user/order/list");

        try {
            ListSlice orderList = uOrderService.getOrderInfoList(LoginUtil.getSessionInfo().getLoginId(), statusCode, content, start, limit);
            return ResponseDo.buildSuccess(orderList);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 生成预支付订单
     *
     * @param orderParam
     * @return
     */
    @RequestMapping(value = "prePay/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo prePay(@RequestBody OrderParam orderParam, HttpServletRequest request) {
        String ip = IPFetchUtil.getIPAddress(request);
        try {
            Object charge = uOrderService.insertOrderPrePay(orderParam, ip);
            return ResponseDo.buildSuccess(charge);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }


    @RequestMapping(value = "rePay/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo rePay(@RequestParam("orderId") String orderId, @RequestParam("channel") String channel, @RequestParam("payType") String payType, HttpServletRequest request) {

        try {
            String ip = IPFetchUtil.getIPAddress(request);
            Object charge = uOrderService.getOrderRePay(orderId, channel, payType, ip);
            return ResponseDo.buildSuccess(charge);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 更新订单为received
     *
     * @param paraJson
     * @return
     */
    @RequestMapping(value = "received/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo setReceived(@RequestBody JSONObject paraJson) {
        try {
            String orderId = paraJson.getString("orderId");
            if (StringUtils.isEmpty(orderId)) {
                throw new ApiException("订单号不能为空");
            }
            uOrderService.updateOrderReceived(orderId);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "complete/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo setCompleteS(@RequestBody JSONObject paramJson) {
        try {
            String orderId = paramJson.getString("orderId");
            if (StringUtils.isEmpty(orderId)) {
                throw new ApiException("订单号不能为空");
            }
            uOrderService.updateOrderCompleteS(orderId);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "detail/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getOrderDetail(@RequestParam("orderId") String orderId) {
        try {
            Map<String, Object> orderDetail = uOrderService.getOrderDetail(orderId);
            return ResponseDo.buildSuccess(orderDetail);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "delete/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo deleteOrder(@RequestBody JSONObject jsonParam) {
        try {
            String orderId = jsonParam.getString("orderId");
            uOrderService.deleteUserOrder(orderId);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}