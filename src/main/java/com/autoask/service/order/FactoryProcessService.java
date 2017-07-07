package com.autoask.service.order;

import com.autoask.common.exception.ApiException;

/**
 * @author hyy
 * @create 2016-12-12 16:25
 */
public interface FactoryProcessService {

    void updateOnlineConfirm(String orderId, String expressCompany, String deliverySerial) throws ApiException;
}
