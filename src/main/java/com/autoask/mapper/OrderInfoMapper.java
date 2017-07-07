package com.autoask.mapper;

import com.autoask.entity.mysql.OrderInfo;
import com.autoask.entity.mysql.OrderShare;
import com.autoask.entity.mysql.User;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderInfoMapper extends MyMapper<OrderInfo> {


    /**
     * 用户前台
     * <p>
     * 获取用户查询条件的订单总数
     * userId不能为空,serveType跟status可以为空
     *
     * @param userId
     * @return
     */
    Long countUserOrderNum(@Param("userId") String userId, @Param("statusCode") Integer statusCode, @Param("goodsIds") List<String> goodsIds);


    /**
     * 用户前台
     *
     * @param userId
     * @param goodsIds
     * @param start
     * @param limit
     * @return
     */
    List<Long> getUserOrderIdList(@Param("userId") String userId, @Param("statusCode") Integer statusCode, @Param("goodsIds") List<String> goodsIds, @Param("start") Integer start, @Param("limit") Integer limit);

    /**
     * 用户前台
     *
     * @param ids
     * @return
     */
    List<OrderInfo> getUserOrderList(@Param("ids") List<Long> ids);

    /**
     * 获取用户最近时间上一次的线下修理厂id
     *
     * @param userId
     * @return
     */
    String getUserLastServiceProvider(@Param("userId") String userId);

    /**
     * 用户前台 获取订单详情
     *
     * @param orderId
     * @return
     */
    OrderInfo getDetail(@Param("orderId") String orderId);


    OrderInfo getDetailByRefundSerial(@Param("refundSerial") String refundSerial);

    /**
     * 获取订单信息以及对应的分成信息
     *
     * @param orderId
     * @return
     */
    OrderInfo getDetailWithShare(@Param("orderId") String orderId);

    List<OrderInfo> getOrderListBySerial(@Param("serial") String serial);

    List<OrderInfo> getOrderListByPaySerial(@Param("paySerial") String paySerial, @Param("status") String status);

    /**
     * 更新订单的状态
     *
     * @param orderId
     * @param toStatus
     * @return
     */
    int updateOrderInfoStatus(@Param("orderId") String orderId, @Param("toStatus") String toStatus);

    int updateOrderPayedByPaySerial(@Param("paySerial") String paySerial, @Param("status") String status);

    int updateOrderPayedBySerial(@Param("serial") String serial, @Param("status") String status);

    /**
     * 更新用户的评论
     *
     * @param orderId
     * @param status
     * @param commentRate
     * @return
     */
    int updateOrderInfoComment(@Param("orderId") String orderId, @Param("status") String status, @Param("commentRate") int commentRate);


    /**
     * 设置付款序列号
     *
     * @param serial
     * @param paySerial
     * @return
     */
    int setPaySerial(@Param("serial") String serial, @Param("paySerial") String paySerial);

    /**
     * 设置退款序列号
     *
     * @param refundSerial
     * @param orderId
     * @return
     */
    int setRefundSerial(@Param("refundSerial") String refundSerial, @Param("orderId") String orderId, @Param("refundType") String refundType, @Param("refundId") String refundId);

    /**
     * autoask 获取订单列表
     *
     * @param serveTypes
     * @param statusList
     * @param orderId
     * @param startTime
     * @param endTime
     * @param phone
     * @return
     */
    Long countOrderInfoNum(@Param("serveTypes") List<String> serveTypes, @Param("statusList") List<String> statusList, @Param("orderId") String orderId,
                           @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("phone") String phone);


    List<Long> getOrderInfoIds(@Param("serveTypes") List<String> serveTypes, @Param("statusList") List<String> statusList, @Param("orderId") String orderId,
                               @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("phone") String phone,
                               @Param("start") int start, @Param("limit") int limit);


    List selectOrderInfoList(@Param("ids") List<Long> ids);

    /**
     * 获取修理厂未处理订单列表
     *
     * @param serviceProviderId
     * @param payedStatus
     * @return
     */
    Long countServiceProviderNewOrderNum(@Param("serviceProviderId") String serviceProviderId, @Param("payedStatus") String payedStatus);

    List<Long> getServiceProviderNewOrderIds(@Param("serviceProviderId") String serviceProviderId, @Param("payedStatus") String payedStatus,
                                             @Param("start") int start, @Param("limit") int limit);

    List<OrderInfo> getServiceProviderNewOrderList(@Param("ids") List<Long> ids);


    /**
     * 线下订单
     *
     * @param serveTypeList
     * @param startTime
     * @param endTime
     * @param goodsIds
     * @param orderId
     * @param statusList
     * @param phone
     * @param serviceProviderId
     * @param mechanicId
     * @return
     */
    Long countOfflineOrderNum(@Param("serveTypeList") List<String> serveTypeList, @Param("startTime") String startTime, @Param("endTime") String endTime,
                              @Param("goodsIds") List<String> goodsIds, @Param("orderId") String orderId,
                              @Param("statusList") List<String> statusList, @Param("phone") String phone,
                              @Param("serviceProviderId") String serviceProviderId,
                              @Param("mechanicId") String mechanicId);

    List<Long> getOfflineOrderIds(@Param("serveTypeList") List<String> serveTypeList, @Param("startTime") String startTime, @Param("endTime") String endTime,
                                  @Param("goodsIds") List<String> goodsIds, @Param("orderId") String orderId,
                                  @Param("statusList") List<String> statusList, @Param("phone") String phone,
                                  @Param("serviceProviderId") String serviceProviderId,
                                  @Param("mechanicId") String mechanicId,
                                  @Param("start") int start, @Param("limit") int limit);


    List<OrderInfo> getOfflineOrderList(@Param("ids") List<Long> ids);


    /**
     * 线上订单
     *
     * @param serveType
     * @param startTime
     * @param endTime
     * @param goodsIds
     * @param orderId
     * @param statusList
     * @param phone
     * @param merchantType
     * @param merchantId
     * @return
     */
    Long countOnlineOrderNum(@Param("serveType") String serveType,
                             @Param("startTime") String startTime, @Param("endTime") String endTime,
                             @Param("goodsIds") List<String> goodsIds,
                             @Param("orderId") String orderId,
                             @Param("statusList") List<String> statusList,
                             @Param("phone") String phone,
                             @Param("merchantType") String merchantType,
                             @Param("merchantId") String merchantId);

    List<Long> getOnlineOrderIds(@Param("serveType") String serveType,
                                 @Param("startTime") String startTime, @Param("endTime") String endTime,
                                 @Param("goodsIds") List<String> goodsIds,
                                 @Param("orderId") String orderId,
                                 @Param("statusList") List<String> statusList,
                                 @Param("phone") String phone,
                                 @Param("merchantType") String merchantType,
                                 @Param("merchantId") String merchantId,
                                 @Param("start") int start,
                                 @Param("limit") int limit);

    List<OrderInfo> getOnlineOrderList(@Param("ids") List<Long> ids);


    /**
     * 订单分成
     *
     * @param orderId
     * @param startTime
     * @param endTime
     * @param shareStatus
     * @return
     */
    Long selectOrderShareNum(@Param("orderId") String orderId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("shareStatus") String shareStatus);

    List<OrderInfo> selectOrderShareCheckList(@Param("orderId") String orderId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("shareStatus") String shareStatus,
                                              @Param("start") Integer start, @Param("limit") Integer limit);

    Long updateOrderInfoShareStatus(@Param("orderId") String orderId, @Param("shareStatus") String shareStatus);

    int updateOrderRefunded(@Param("orderId") String orderId, @Param("status") String status);

    int updateOrderReceived(@Param("orderId") String orderId, @Param("status") String status);


    //统计信息
    //autoask当天的销售额
    BigDecimal getTodaySellAmount(@Param("statusList") List<String> notInStatusList);

    //autoask总销售额
    BigDecimal getTotalSellAmount(@Param("statusList") List<String> notInStatusList);

    Long getTodaySellGoodsNum(@Param("statusList") List<String> notInStatusList);

    Long getTotalSellGoodsNum(@Param("statusList") List<String> notInStatusList);

    Long getTodayOrderNum(@Param("statusList") List<String> notInStatusList);

    Long getTotalOrderNum(@Param("statusList") List<String> notInStatusList);

    //合作商统计业绩
    BigDecimal getPartnerOrderSell(@Param("partnerType") String partnerType, @Param("partnerId") String partnerId,
                                   @Param("starTime") Date startTime, @Param("endTime") Date endTime,
                                   @Param("onlineStatusList") List<String> onlineStatusList, @Param("offlineStatusList") List<String> offlineStatusList);

    //修理厂统计业绩
    BigDecimal getServiceProviderSell(@Param("serviceProviderId") String serviceProviderId, @Param("startTime") Date startTime, @Param("endTime") Date endTime,
                                      @Param("offlineStatusList") List<String> offlineStatusList);

    //工厂销售信息
    BigDecimal getFactorySell(@Param("factoryMerchantType") String factoryMerchantType, @Param("factoryId") String factoryId,
                              @Param("onlineStatusList") List<String> onlineStatusList,
                              @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<OrderInfo> getRollBackOrderInfoList();
}