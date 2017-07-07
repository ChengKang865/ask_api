package com.autoask.mapper;

import com.autoask.entity.mysql.OrderDelivery;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDeliveryMapper extends MyMapper<OrderDelivery> {

    Long updateOrderDeliveryMerchant(@Param("merchantType") String merchantType, @Param("merchantId") String merchantId, @Param("orderId") String orderId);

    Long updateExpressInfo(@Param("orderId") String orderId, @Param("expressCompany") String expressCompany, @Param("deliverySerial") String deliverySerial);
}