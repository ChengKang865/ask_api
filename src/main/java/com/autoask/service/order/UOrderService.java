package com.autoask.service.order;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mongo.comment.GoodsComment;
import com.autoask.entity.mongo.comment.OrderComment;
import com.autoask.entity.mysql.OrderInfo;
import com.autoask.entity.param.OrderParam;
import com.pingplusplus.model.Charge;

import java.util.List;
import java.util.Map;

/**
 * @author hyy
 * @create 16/10/23 17:45
 */
public interface UOrderService {

    /**
     * 生成预先支付订单
     *
     * @param orderParam
     * @param ip
     * @return
     * @throws ApiException
     */
    Object insertOrderPrePay(OrderParam orderParam, String ip) throws ApiException;

    /**
     * 确定收货/服务
     *
     * @param orderId
     * @throws ApiException
     */
    void updateOrderReceived(String orderId) throws ApiException;


    /**
     * 获取订单列表
     *
     * @param userId
     * @param statusCode
     * @param content
     * @param start
     * @param limit
     * @return
     * @throws ApiException
     */
    ListSlice<OrderInfo> getOrderInfoList(String userId, Integer statusCode, String content, int start, int limit) throws ApiException;

    /**
     * 查询支付序列号是否支付成功
     *
     * @param paySerial
     * @return
     * @throws ApiException
     */
    boolean queryOrderPaySucceed(String paySerial) throws ApiException;


    /**
     * 评价订单
     *
     * @param orderComment
     * @throws ApiException
     */
    void insertOrderComment(OrderComment orderComment) throws ApiException;


    void insertGoodsComment(GoodsComment goodsComment) throws ApiException;

    GoodsComment getGoodsComment(String orderGoodsContainerId) throws ApiException;

    Map<String, Object> getOrderDetail(String orderId) throws ApiException;

    void deleteUserOrder(String orderId) throws ApiException;

    Object getOrderRePay(String orderId, String channel, String payType, String ip) throws ApiException;

    void updateOrderCompleteS(String orderId) throws ApiException;
}