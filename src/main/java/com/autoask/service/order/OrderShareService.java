package com.autoask.service.order;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mysql.*;

import java.util.List;
import java.util.Map;

/**
 * @author hyy
 * @create 2016-12-04 14:52
 */
public interface OrderShareService {

    OrderShare initOrderShare(OrderInfo orderInfo, Map<String, Card> cardMap, Map<String, Goods> goodsMap) throws ApiException;

    void initOrderShareMerchantName(List<OrderShare> orderShareList) throws ApiException;

    void updateOrderShareNoServiceProvider(String orderId) throws ApiException;
}
