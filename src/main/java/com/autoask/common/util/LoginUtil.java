package com.autoask.common.util;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.entity.common.SessionInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hyy
 * @create 2016-09-08 16:55
 */
public class LoginUtil {

    private static final Logger LOG = LoggerFactory.getLogger(LoginUtil.class);


    private static ThreadLocal<SessionInfo> threadHolder = new ThreadLocal<>();

    /**
     * 获取当前线程的登录信息
     *
     * @return
     * @throws ApiException
     */
    public static SessionInfo getSessionInfo() throws ApiException {
        SessionInfo sessionInfo = threadHolder.get();
        if (null == sessionInfo) {
            LOG.error("threadLocal not has sessionInfo");
            throw new ApiException("threadLocal not has sessionInfo");
        }
        return sessionInfo;
    }

    public static String getLoginType() throws ApiException {
        SessionInfo sessionInfo = getSessionInfo();
        return sessionInfo.getLoginType();
    }

    public static String getLoginId() throws ApiException {
        return getSessionInfo().getLoginId();
    }

    /**
     * 设置当前线程的登录信息
     *
     * @param sessionInfo
     * @throws ApiException
     */
    public static void setSessionInfo(SessionInfo sessionInfo) {
        threadHolder.set(sessionInfo);
    }

    /**
     * 判断用户ajax登录，提示需要用户登录
     *
     * @param response
     * @return
     */
    public static void dealWithAjaxReturn(HttpServletResponse response, int errorCode, String errMsg) {

        ResponseDo responseDo = ResponseDo.buildError(errorCode, errMsg);
        String responseStr = JSONObject.toJSONString(responseDo);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(responseStr);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }

    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        if (request.getHeader("X-Requested-With") != null && "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()))
            return true;
        return false;
    }

    public static void checkAccessPermission(String merchantType, String merchantId) throws ApiException {
        String loginType = getSessionInfo().getLoginType();
        String loginId = getSessionInfo().getLoginId();
        if (StringUtils.equals(loginType, Constants.LoginType.STAFF)) {
            return;
        }
        if (!StringUtils.equals(loginType, merchantType) || !StringUtils.equals(loginId, merchantId)) {
            throw new ApiException("没有权限");
        }
    }

}
