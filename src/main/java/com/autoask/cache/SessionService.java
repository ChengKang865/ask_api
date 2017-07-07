package com.autoask.cache;

import com.autoask.entity.common.SessionInfo;

/**
 * @author hyy
 * @create 2016-09-08 16:26
 */
public interface SessionService {

    /**
     * 设置用户的session
     *
     * @param userId
     * @param sessionId
     */
    void setUserSession(String sessionId, String userId, int seconds);

    void removeUserSession(String sessionId);

    /**
     * 获取用户的session
     *
     * @param sessionId
     * @return
     */
    SessionInfo getUserLoginInfo(String sessionId);

    void setMerchantSession(String sessionId, SessionInfo sessionInfo, int seconds);

    SessionInfo getMerchantSession(String sessionId);

    void removeMerchantSession(String sessionId);

    void setSessionOpenId(String sessionId, String openId);

    String getSessionOpenId(String sessionId);

    void setActiveSessionId(String sessionId,String openId);

    String getActiveSessionId(String sessionId);
}
