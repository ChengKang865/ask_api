package com.autoask.service.order;

import com.autoask.entity.mysql.Goods;
import com.autoask.entity.mysql.OrderInfo;
import com.autoask.entity.param.OrderParam;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author hyy
 * @create 2017-04-11 20:01
 */
public interface OrderCommonService {

    BigDecimal calcOnlinePrice(OrderInfo orderInfo, Map<String, Goods> goodsMap, OrderParam orderParam);
}
