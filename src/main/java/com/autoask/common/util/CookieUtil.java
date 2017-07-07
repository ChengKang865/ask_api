package com.autoask.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hyy
 * @create 2016-09-07 20:02
 */
public class CookieUtil {

    private static final Logger LOG = LoggerFactory.getLogger(CookieUtil.class);

    public static Map<String, String> getCookieMap(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            LOG.debug("cookie is null");
            return null;
        }
        Map<String, String> cookieMap = new HashMap<>(cookies.length);
        for (Cookie cookie : cookies) {
            cookieMap.put(cookie.getName(), cookie.getValue());
        }
        return cookieMap;
    }

    /**
     * 添加自定义的sessionId到cookie中
     *
     * @param rememberFlag
     * @param request
     * @param response
     * @param sessionId
     */
    public static void addSessionCookie(boolean rememberFlag, HttpServletRequest request, HttpServletResponse response, String sessionId) {
        Cookie cookie = new Cookie(Constants.CookieParam.SESSION_ID, sessionId);
        cookie.setDomain(request.getServerName());
        cookie.setPath("/");
        int expireTime = rememberFlag ? Constants.CookieParam.REMEMBER_COOKIE_EXPIRE_SECOND : Constants.CookieParam.DEFAULT_COOKIE_EXPIRE_SECOND;
        cookie.setMaxAge(expireTime);
        response.addCookie(cookie);
    }

    public static void removeSession(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(Constants.CookieParam.SESSION_ID, "");
        cookie.setDomain(request.getServerName());
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}