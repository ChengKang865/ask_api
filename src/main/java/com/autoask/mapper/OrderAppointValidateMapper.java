package com.autoask.mapper;

import com.autoask.entity.mysql.OrderAppointValidate;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author hyy
 * @create 16/10/23 10:31
 */
public interface OrderAppointValidateMapper extends MyMapper<OrderAppointValidate> {

    OrderAppointValidate getByOrderId(@Param("orderId") String orderId);

    OrderAppointValidate getByCode(@Param("serviceProviderId") String serviceProviderId, @Param("code") String code);

    Integer updateValidate(@Param("orderId") String orderId);
}
