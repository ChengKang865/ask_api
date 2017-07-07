package com.autoask.service.pay;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.exception.ApiException;

/**
 * Created by hyy on 2016/12/14.
 */
public interface PayService {

    /**
     * ping++ 付款成功回调接口
     *
     * @param jsonObject
     * @return
     * @throws ApiException
     */
    void updateOrderPayed(JSONObject jsonObject) throws ApiException;

    /**
     * 同时支付的多个订单拥有同一个serial，主要是为了卡兑换掉了所有的商品的时候调用该方法
     *
     * @param serial
     * @return
     * @throws ApiException
     */
    void updateOrderPayed(String serial) throws ApiException;
}
