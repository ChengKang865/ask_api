package com.autoask.service.order;

import com.autoask.common.exception.ApiException;

/**
 * @author hyy
 * @create 2016-12-12 16:59
 */
public interface ServiceProviderProcessService {

    void updateAppointConfirm(String orderId) throws ApiException;

    String updateAppointValidate(String code) throws ApiException;
}
