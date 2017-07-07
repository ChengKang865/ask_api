package com.autoask.service.common;

import com.autoask.common.exception.ApiException;

import java.util.Map;

/**
 * Created by hp on 16-10-23.
 */
public interface WeChatService {

    /**
     * 获取用户的openId信息
     *
     * @param code
     * @param sessionId
     * @return
     * @throws ApiException
     */
    Map<String, Object> updateUserOpenId(String code, String sessionId) throws ApiException;


    String getOpenId(String code) throws ApiException;
}
