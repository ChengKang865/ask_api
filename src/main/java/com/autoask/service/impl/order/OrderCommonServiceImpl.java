package com.autoask.service.impl.order;

import com.autoask.entity.mysql.Goods;
import com.autoask.entity.mysql.OrderInfo;
import com.autoask.entity.param.OrderParam;
import com.autoask.service.order.OrderCommonService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author hyy
 * @create 2017-04-11 20:01
 */
@Service("orderCommonService")
public class OrderCommonServiceImpl implements OrderCommonService {

    @Override
    public BigDecimal calcOnlinePrice(OrderInfo orderInfo, Map<String, Goods> goodsMap, OrderParam orderParam) {

        return null;
    }
}
