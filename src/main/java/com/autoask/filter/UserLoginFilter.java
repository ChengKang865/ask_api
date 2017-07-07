package com.autoask.filter;

import com.autoask.cache.SessionService;
import com.autoask.common.util.Constants;
import com.autoask.common.util.CookieUtil;
import com.autoask.common.util.LoginUtil;
import com.autoask.entity.common.SessionInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author hyy
 * @create 2016-09-01 16:01
 */
public class UserLoginFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(UserLoginFilter.class);

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
        //不需要登录
        if (!AuthManager.needUserLogin(dispatchPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //获取sessionId，优先从参数中获取，如果URL参数中没有的话就从cookie中获取
        String sessionId = request.getParameter(Constants.CookieParam.SESSION_ID);
        //需要登录
        if (StringUtils.isNotEmpty(sessionId)) {
            SessionInfo sessionInfo = sessionService.getUserLoginInfo(sessionId);
            if (null != sessionInfo && StringUtils.isNotEmpty(sessionInfo.getLoginId())) {
                //本地线程中设定userId
                LoginUtil.setSessionInfo(sessionInfo);
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        //ajax请求
        if (LoginUtil.isAjaxRequest(request)) {
            LoginUtil.dealWithAjaxReturn(response, Constants.ErrCode.NO_LOGIN, "需要登录");
            return;
        }

        LoginUtil.dealWithAjaxReturn(response, Constants.ErrCode.NO_LOGIN, "需要登录");
    }

    @Override
    public void destroy() {
    }
}
