package com.autoask.controller.order;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.Constants;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.common.OrderStatusUtil;
import com.autoask.entity.mysql.OrderInfo;
import com.autoask.service.order.OrderService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 订单
 */
@Controller
@RequestMapping("/order/")
public class OrderController {

    public static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;


    /**
     * 获取订单详情
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "detail/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo detail(@RequestParam("orderId") String orderId) {
        LOG.info("get order detail");
        try {
            OrderInfo orderInfo = orderService.getOrderDetailWithShare(orderId);
            return ResponseDo.buildSuccess(orderInfo);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }


    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo list(@RequestParam(value = "serveTypeList", required = false) ArrayList<String> serveTypeList,
                           @RequestParam(value = "startTime", required = false) String startTime,
                           @RequestParam(value = "endTime", required = false) String endTime,
                           @RequestParam(value = "phone", required = false) String phone,
                           @RequestParam(value = "orderId", required = false) String orderId,
                           @RequestParam(value = "statusList", required = false) List<String> statusList,
                           @RequestParam(value = "page") Integer page, @RequestParam("count") int count) {

        int start = CommonUtil.pageToSkipStart(page, count);
        int limit = CommonUtil.cleanCount(count);
        try {
            ListSlice<OrderInfo> orderInfoList = orderService.getOrderInfoList(serveTypeList, statusList, orderId, startTime, endTime, phone, start, limit);
            return ResponseDo.buildSuccess(orderInfoList);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "serviceProvider/list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo serviceProviderList(@RequestParam(value = "page") Integer page, @RequestParam("count") Integer count) {

        try {
            int start = CommonUtil.pageToSkipStart(page, count);
            int limit = CommonUtil.cleanCount(count);
            ListSlice<OrderInfo> orderInfoList = orderService.getServiceProviderNewOrderList(start, limit);
            return ResponseDo.buildSuccess(orderInfoList);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }


    @RequestMapping(value = "online/list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo onlineOrderList(@RequestParam(value = "startTime", required = false) String startTime,
                                      @RequestParam(value = "endTime", required = false) String endTime,
                                      @RequestParam(value = "productCategoryId", required = false) String productCategoryId,
                                      @RequestParam(value = "productId", required = false) String productId,
                                      @RequestParam(value = "goodsId", required = false) String goodsId,
                                      @RequestParam(value = "goodsName", required = false) String goodsName,
                                      @RequestParam(value = "orderId", required = false) String orderId,
                                      @RequestParam(value = "statusCode", required = false) Integer statusCode,
                                      @RequestParam(value = "phone", required = false) String phone,
                                      @RequestParam(value = "merchantType", required = false) String merchantType,
                                      @RequestParam(value = "merchantId", required = false) String merchantId,
                                      @RequestParam(value = "page") Integer page, @RequestParam("count") int count) {
        try {
            int start = CommonUtil.pageToSkipStart(page, count);
            int limit = CommonUtil.cleanCount(count);
            String serveType = Constants.OrderServeType.ONLINE;
            List<String> statusList = OrderStatusUtil.getOnlineStatusList(statusCode);
            ListSlice<OrderInfo> orderList = orderService.getOnlineOrderList(serveType, startTime, endTime, productCategoryId, productId, goodsId, goodsName, orderId, statusList, phone, merchantType, merchantId, start, limit);
            return ResponseDo.buildSuccess(orderList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "offline/list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo offlineOrderList(@RequestParam(value = "serveTypeList", required = false) List<String> serveTypeList,
                                       @RequestParam(value = "startTime", required = false) String startTime,
                                       @RequestParam(value = "endTime", required = false) String endTime,
                                       @RequestParam(value = "productCategoryId", required = false) String productCategoryId,
                                       @RequestParam(value = "productId", required = false) String productId,
                                       @RequestParam(value = "goodsId", required = false) String goodsId,
                                       @RequestParam(value = "goodsName", required = false) String goodsName,
                                       @RequestParam(value = "orderId", required = false) String orderId,
                                       @RequestParam(value = "statusList", required = false) List<String> statusList,
                                       @RequestParam(value = "phone", required = false) String phone,
                                       @RequestParam(value = "serviceProviderId", required = false) String serviceProviderId,
                                       @RequestParam(value = "mechanicId", required = false) String mechanicId,
                                       @RequestParam(value = "page") Integer page, @RequestParam("count") int count) {
        try {
            int start = CommonUtil.pageToSkipStart(page, count);
            int limit = CommonUtil.cleanCount(count);
            ListSlice<OrderInfo> orderList = orderService.getOfflineOrderList(startTime, endTime, productCategoryId, productId, goodsId, goodsName, orderId, statusList, phone, serviceProviderId, mechanicId, start, limit);
            return ResponseDo.buildSuccess(orderList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }


    @RequestMapping(value = "share/check/list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo listOrderShareApply(@RequestParam(value = "orderId", required = false) String orderId,
                                          @RequestParam(value = "startTime", required = false) String startTime,
                                          @RequestParam(value = "endTime", required = false) String endTime,
                                          @RequestParam(value = "shareStatus", required = false) String shareStatus,
                                          @RequestParam(value = "page") int page, @RequestParam("count") int count) {


        try {
            int start = CommonUtil.pageToSkipStart(page, count);
            int limit = CommonUtil.cleanCount(count);
            ListSlice orderInfoList = orderService.getOrderShareCheckList(orderId, startTime, endTime, shareStatus, start, limit);
            return ResponseDo.buildSuccess(orderInfoList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "share/check/")
    @ResponseBody
    public ResponseDo updateOrderShareApplyStatus(@Param("orderId") String orderId,
                                                  @RequestParam("shareStatus") String shareStatus) {
        try {
            orderService.updateOrderShareStatus(orderId, shareStatus);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }


    @RequestMapping(value = "express/update/",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updateOrderExpress(@Param("orderId") String orderId, @Param("expressCompany") String expressCompany, @Param("expressSerial") String expressSerial) {
        try {
            orderService.updateExpress(orderId, expressCompany, expressSerial);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}