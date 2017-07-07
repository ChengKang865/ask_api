package com.autoask.mapper;

import com.autoask.entity.mysql.OrderGoodsShare;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface OrderGoodsShareMapper extends MyMapper<OrderGoodsShare> {

    Long updateNoServiceProvider(@Param("orderId") String orderId);
}