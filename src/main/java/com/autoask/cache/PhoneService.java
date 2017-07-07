package com.autoask.cache;

import com.autoask.common.exception.ApiException;

/**
 * @author hyy
 * @create 2016-09-07 13:16
 */
public interface PhoneService {

    /**
     * 发送短信验证码并将验证码缓存到redis中
     * @param phone
     * @param verifyCode
     * @throws ApiException
     */
    void sendPhoneVerifyCode(String phone, String verifyCode) throws ApiException;

    String getVerifyCode(String phone);
}