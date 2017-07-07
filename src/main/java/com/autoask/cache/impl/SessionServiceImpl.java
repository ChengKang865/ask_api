package com.autoask.cache.impl;

import com.autoask.cache.CacheService;
import com.autoask.cache.SessionService;
import com.autoask.common.util.Constants;
import com.autoask.entity.common.SessionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hyy
 * @create 2016-09-08 16:26
 */
@Service("sessionService")
public class SessionServiceImpl implements SessionService {

    private static final Logger LOG = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Autowired
    private CacheService cacheService;


    @Override
    public void setUserSession(String sessionId, String userId, int seconds) {
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setSessionId(sessionId);
        sessionInfo.setLoginId(userId);
        sessionInfo.setLoginType(Constants.LoginType.USER);
        cacheService.set(buildUserSessionId(sessionId), sessionInfo, seconds);
    }

    @Override
    public SessionInfo getUserLoginInfo(String sessionId) {
        return cacheService.get(buildUserSessionId(sessionId), SessionInfo.class);
    }

    @Override
    public void setMerchantSession(String sessionId, SessionInfo sessionInfo, int seconds) {
        cacheService.set(buildMerchantSessionId(sessionId), sessionInfo, seconds);
    }

    @Override
    public SessionInfo getMerchantSession(String sessionId) {
        return cacheService.get(buildMerchantSessionId(sessionId), SessionInfo.class);
    }

    @Override
    public void removeUserSession(String sessionId) {
        cacheService.del(buildUserSessionId(sessionId));
    }

    @Override
    public void removeMerchantSession(String sessionId) {
        cacheService.del(buildMerchantSessionId(sessionId));
    }

    @Override
    public void setSessionOpenId(String sessionId, String openId) {
        cacheService.set(buildWxSessionId(sessionId), openId, Constants.WX_OPEN_ID_EXPIRE_SECONDS);
    }

    @Override
    public String getSessionOpenId(String sessionId) {
        return cacheService.get(buildWxSessionId(sessionId));
    }

    private static String buildUserSessionId(String sessionId) {
        return "u_session_id:" + sessionId;
    }

    private static String buildMerchantSessionId(String sessionId) {
        return "u_merchant_session_id:" + sessionId;
    }

    private static String buildWxSessionId(String sessionId) {
        return "wx_session:" + sessionId;
    }

    @Override
    public void setActiveSessionId(String sessionId, String openId) {
        cacheService.set(buildActiveSessionId(sessionId), openId);
    }

    @Override
    public String getActiveSessionId(String sessionId) {
        return cacheService.get(buildActiveSessionId(sessionId));
    }

    private static String buildActiveSessionId(String sessionId) {
        return "active_session:" + sessionId;
    }
}
