package com.autoask.service.order;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mysql.Card;
import com.autoask.entity.mysql.Goods;
import com.autoask.entity.mysql.OrderInfo;
import com.autoask.entity.param.OrderParam;

import java.util.List;
import java.util.Map;

/**
 * @author hyy
 * @create 2016-12-01 16:26
 */
public interface OrderParamService {

    Map<String, Goods> getGoodsMap(OrderParam orderParam) throws ApiException;

    Map<String, Card> getCardMap(OrderParam orderParam) throws ApiException;

    void initOrderParam(OrderParam orderParam, Map<String, Goods> goodsMap, Map<String, Card> cardMap) throws ApiException;

    void checkParam(OrderParam orderParam, Map<String, Goods> goodsMap, Map<String, Card> cardMap) throws ApiException;

    List<OrderInfo> getOnlineOrderList(OrderParam orderParam, Map<String, Goods> goodsMap, Map<String, Card> cardMap) throws ApiException;
}