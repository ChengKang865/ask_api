package com.autoask.service.common;

import com.autoask.common.exception.ApiException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by hp on 16-10-30.
 * <p>
 * 支付宝支付 Service.
 */
public interface AlipayService {

    /**
     * 更新用户申请信息以及阿里支付信息申请参数构造
     *
     * @param applyIds
     * @return
     * @throws ApiException
     */
    String updateMerchantAppliesAndAlipayApply(List<String> applyIds) throws ApiException;

    /**
     * 支付宝回调，更新用户的申请记录
     *
     * @param servletRequest
     * @throws ApiException
     */
    void updateSubmitPayment(HttpServletRequest servletRequest) throws ApiException;


}
