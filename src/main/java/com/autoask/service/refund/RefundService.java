package com.autoask.service.refund;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.exception.ApiException;
import com.autoask.entity.mysql.OrderInfo;

/**
 * @author hyy
 * @create 2016-12-13 14:48
 */
public interface RefundService {

    void updateOrderRefused(String orderId) throws ApiException;

    void updateOrderRefused(OrderInfo orderInfo) throws ApiException;

    /**
     * ping++ 回掉的退款成功通知
     *
     * @param jsonObject
     * @return
     * @throws ApiException
     */
    void updateOrderRefund(JSONObject jsonObject) throws ApiException;
}
