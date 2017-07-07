package com.autoask.mapper;

import com.autoask.entity.mysql.OrderServe;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OrderServeMapper extends MyMapper<OrderServe> {

    Integer updateMechanicId(@Param("orderId") String orderId, @Param("mechanicId") String mechanicId);

    Integer updateOrderServeMerchant(@Param("checkMerchantType") String checkMerchantType, @Param("orderId") String orderId);

    Integer updateOrderServeNoPartner(@Param("orderId") String orderId, @Param("checkMerchantType") String checkMerchantType, @Param("partnerPreShare")BigDecimal partnerPreShare);
}