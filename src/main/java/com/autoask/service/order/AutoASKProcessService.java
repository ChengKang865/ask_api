package com.autoask.service.order;

import com.autoask.common.exception.ApiException;

/**
 * Created by hyy on 2016/12/12.
 */
public interface AutoASKProcessService {

    void updateAutoASKConfirm(String orderId, String expressCompany, String deliverySerial) throws ApiException;

    //退款 线上 线下订单都可以退款
    void updateRefuse(String orderId) throws ApiException;

    void updateAppointRefuse(String orderId) throws ApiException;
}
