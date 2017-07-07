package com.autoask.mapper;

import com.autoask.entity.mysql.OrderShare;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author hyy
 * @create 2016-12-01 14:25
 */
public interface OrderShareMapper extends MyMapper<OrderShare> {

    OrderShare getOrderShareByOrderId(@Param("orderId") String orderId);

    int updateOrderShareNoServiceProvider(@Param("orderId") String orderId);
}
