package com.autoask.filter;

import com.alibaba.fastjson.JSONObject;
import com.autoask.cache.SessionService;
import com.autoask.common.ResponseDo;
import com.autoask.common.util.Constants;
import com.autoask.common.util.CookieUtil;
import com.autoask.common.util.LoginUtil;
import com.autoask.entity.common.SessionInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author hyy
 * @create 2016-10-04 22:41
 */
public class MerchantLoginFilter implements Filter {


    @Autowired
    private SessionService sessionService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String contextPath = request.getContextPath();
        String requestUri = request.getRequestURI();
        String dispatchPath = requestUri.replace(contextPath, "");

        //TODO 特殊情况
        if (dispatchPath.endsWith("product/product/shoppingCart/")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //获取sessionId，优先从参数中获取，如果URL参数中没有的话就从cookie中获取
        String staffSessionId = request.getParameter(Constants.CookieParam.STAFF_SESSION_ID);
        String factorySessionId = request.getParameter(Constants.CookieParam.FACTORY_SESSION_ID);
        String serviceProviderSessionId = request.getParameter(Constants.CookieParam.SERVICE_PROVIDER_SESSION_ID);
        String sessionId = null;
        if (StringUtils.isNotEmpty(staffSessionId)) {
            sessionId = staffSessionId;
        } else if (StringUtils.isNotEmpty(factorySessionId)) {
            sessionId = factorySessionId;
        }  else if (StringUtils.isNotEmpty(serviceProviderSessionId)) {
            sessionId = serviceProviderSessionId;
        }
        //已经登录
        if (StringUtils.isNotEmpty(sessionId)) {
            SessionInfo sessionInfo = sessionService.getMerchantSession(sessionId);
            if (null != sessionInfo) {
                //拥有权限
                if (AuthManager.hasPermission(dispatchPath, sessionInfo)) {
                    //绑定到本地线程
                    LoginUtil.setSessionInfo(sessionInfo);

                    filterChain.doFilter(servletRequest, servletResponse);

                    return;
                } else {
                    //没有权限
                    LoginUtil.dealWithAjaxReturn(response, Constants.ErrCode.NO_PERMISSION, "没有权限");
                }
            }
        }

        //需要登录，但没有登录
        LoginUtil.dealWithAjaxReturn(response, Constants.ErrCode.NO_LOGIN, "需要登录");
    }

    @Override
    public void destroy() {
    }
}
