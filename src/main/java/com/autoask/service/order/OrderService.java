package com.autoask.service.order;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mysql.*;

import java.util.List;

/**
 * @author hyy
 * @create 2016-09-10 12:53
 */
public interface OrderService {

    /**
     * 获取所有的订单列表
     *
     * @param serveTypes
     * @param statusList
     * @param orderId
     * @param startTime
     * @param endTime
     * @param phone
     * @param start
     * @param end
     * @return
     * @throws ApiException
     */
    ListSlice<OrderInfo> getOrderInfoList(List<String> serveTypes, List<String> statusList, String orderId, String startTime, String endTime, String phone, int start, int end) throws ApiException;


    ListSlice<OrderInfo> getServiceProviderNewOrderList(int start, int limit) throws ApiException;

    /**
     * 获取线下订单列表
     *
     * @param startTime
     * @param endTime
     * @param productCategoryId
     * @param productId
     * @param goodsId
     * @param goodsName
     * @param orderId
     * @param statusList
     * @param phone
     * @param serviceProviderId
     * @param mechanicId
     * @param start
     * @param limit
     * @return
     * @throws ApiException
     */
    ListSlice<OrderInfo> getOfflineOrderList(String startTime, String endTime, String productCategoryId, String productId, String goodsId, String goodsName, String orderId, List<String> statusList, String phone, String serviceProviderId, String mechanicId, int start, int limit) throws ApiException;

    /**
     * 获取线上订单列表
     *
     * @param serveType
     * @param startTime
     * @param endTime
     * @param productCategoryId
     * @param productId         产品id
     * @param goodsId
     * @param goodsName
     * @param orderId
     * @param statusList
     * @param phone
     * @param checkMerchantType
     * @param checkMerchantId
     * @param start
     * @param limit
     * @return
     * @throws ApiException
     */
    ListSlice<OrderInfo> getOnlineOrderList(String serveType, String startTime, String endTime, String productCategoryId, String productId, String goodsId, String goodsName,
                                            String orderId, List<String> statusList, String phone, String checkMerchantType, String checkMerchantId, int start, int limit) throws ApiException;


    /**
     * 获取订单详情，包含了
     * user 用户信息
     * orderServe
     * orderDelivery
     * orderGoodsList
     * goodsSnapshot
     * goods
     * product
     *
     * @param orderId
     * @return
     * @throws ApiException
     */
    OrderInfo getOrderDetailWithShare(String orderId) throws ApiException;

    /**
     * 获取审核分成列表
     *
     * @param orderId
     * @param startTime
     * @param endTime
     * @param shareStatus
     * @param start
     * @param limit
     * @return
     * @throws ApiException
     */
    ListSlice getOrderShareCheckList(String orderId, String startTime, String endTime, String shareStatus, int start, int limit) throws ApiException;

    /**
     * 更新订单分成审核的状态
     * 如果通过了审核需要
     * 1.factory partner serviceProvider增加余额
     * 2.如果是outlet引流的，需要分发给outlets微信红包
     *
     * @param orderId
     * @param shareStatus
     * @throws ApiException
     */
    void updateOrderShareStatus(String orderId, String shareStatus) throws ApiException;

    void updateRollBACKOrderInfo() throws ApiException;

    void updateExpress(String orderId, String expressCompany, String expressSerial) throws ApiException;
}