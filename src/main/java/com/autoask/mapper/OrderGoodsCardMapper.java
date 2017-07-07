package com.autoask.mapper;

import com.autoask.entity.mysql.OrderGoodsCard;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by hyy on 2016/12/6.
 */
public interface OrderGoodsCardMapper extends MyMapper<OrderGoodsCard> {
    List<String> getCardIdListByOrderGoodsIdList(@Param("orderGoodsIdList") List<String> orderGoodsIdList);
}