package com.autoask.controller.common;

import com.autoask.cache.PhoneService;
import com.autoask.cache.SessionService;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CodeGenerator;
import com.autoask.common.util.Constants;
import com.autoask.common.util.CookieUtil;
import com.autoask.entity.common.SessionInfo;
import com.autoask.entity.mongo.Staff;
import com.autoask.entity.mongo.merchant.Factory;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.entity.mysql.User;
import com.autoask.service.StaffService;
import com.autoask.service.merchant.FactoryService;
import com.autoask.service.merchant.ServiceProviderService;
import com.autoask.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hyy
 * @create 16/8/4 16:21
 */
@RequestMapping(value = "login/")
@Controller
public class LoginController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private FactoryService factoryService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private StaffService staffService;


    /**
     * 用户登录
     *
     * @param phone
     * @param code
     * @param rememberFlag
     * @return
     */
    @RequestMapping(value = "user/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo user(@RequestParam(value = "phone") String phone, @RequestParam(value = "code") String code,
                           @RequestParam(value = "rememberFlag", defaultValue = "false", required = false) boolean rememberFlag) {
        //判断用户是否已经注册
        User user = userService.getUserByPhone(phone);
        if (null == user) {
            return ResponseDo.buildError("当前手机号未注册,请先注册再登录");
        }
        //校验手机验证码 或者密码
        String verifyCode = phoneService.getVerifyCode(phone);
        if (!StringUtils.equals(verifyCode, code) && !StringUtils.equals(code, user.getPassword())) {
            return ResponseDo.buildError("验证码/密码错误");
        }


        //缓存用户信息到redis中
        String sessionId = CodeGenerator.uuid();
        int expireTime = rememberFlag ? Constants.CookieParam.REMEMBER_COOKIE_EXPIRE_SECOND : Constants.CookieParam.DEFAULT_COOKIE_EXPIRE_SECOND;
        sessionService.setUserSession(sessionId, user.getUserId(), expireTime);

        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put(Constants.CookieParam.SESSION_ID, sessionId);
        resultMap.put("expire", expireTime);
        return ResponseDo.buildSuccess(resultMap);
    }

    @RequestMapping(value = "logout/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo userLoginOut(HttpServletRequest request, HttpServletResponse response) {
        String userSessionId = request.getParameter(Constants.CookieParam.SESSION_ID);
        String staffSessionId = request.getParameter(Constants.CookieParam.STAFF_SESSION_ID);
        String factorySessionId = request.getParameter(Constants.CookieParam.FACTORY_SESSION_ID);
        String serviceProviderSessionId = request.getParameter(Constants.CookieParam.SERVICE_PROVIDER_SESSION_ID);

        if (StringUtils.isNotEmpty(userSessionId)) {
            sessionService.removeUserSession(userSessionId);
        } else {
            if (StringUtils.isNotEmpty(staffSessionId)) {
                sessionService.removeMerchantSession(staffSessionId);
            } else if (StringUtils.isNotEmpty(factorySessionId)) {
                sessionService.removeMerchantSession(factorySessionId);
            }  else if (StringUtils.isNotEmpty(serviceProviderSessionId)) {
                sessionService.removeMerchantSession(serviceProviderSessionId);
            }
        }
        return ResponseDo.buildSuccess("");
    }

    /**
     * 商户登录
     *
     * @return
     */
    @RequestMapping(value = "merchant/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo merchantPhone(@RequestParam(value = "phone") String phone,
                                    @RequestParam(value = "loginType") String loginType,
                                    @RequestParam(value = "code") String code,
                                    @RequestParam(value = "rememberFlag", defaultValue = "false", required = false) boolean rememberFlag,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        try {
            return loginWithPhone(phone, code, rememberFlag, request, response, loginType);
        } catch (ApiException e) {
            return ResponseDo.buildError(e.getMessage());
        }
    }

    public ResponseDo loginWithPhone(String phone, String code, boolean rememberFlag, HttpServletRequest request, HttpServletResponse response, String loginType) throws ApiException {
        String loginId;
        String permission = null;
        String password;
        String name = null;
        String logoUrl = null;
        if (StringUtils.equals(loginType, Constants.LoginType.STAFF)) {
            Staff staff = staffService.getStaffByPhone(phone);
            if (null == staff) {
                throw new ApiException("员工不存在");
            }
            password = staff.getPassword();
            loginId = staff.getId();
            permission = staff.getRole();
            name = staff.getName();
        } else if (StringUtils.equals(loginType, Constants.LoginType.FACTORY)) {
            Factory factory = factoryService.getFactoryByPhone(phone);
            if (null == factory) {
                throw new ApiException("帐号不存在");
            }
            password = factory.getPassword();
            loginId = factory.getId();
            name = factory.getName();
            logoUrl = factory.getLogoUrl();
        }  else if (StringUtils.equals(loginType, Constants.LoginType.SERVICE_PROVIDER)) {
            ServiceProvider serviceProvider = serviceProviderService.getServiceProviderByPhone(phone);
            if (null == serviceProvider) {
                throw new ApiException("帐号不存在");
            }
            password = serviceProvider.getPassword();
            loginId = serviceProvider.getId();
            name = serviceProvider.getName();
            logoUrl = serviceProvider.getLogoUrl();
        } else {
            throw new ApiException("登录类型非法");
        }

        if (!StringUtils.equals(phoneService.getVerifyCode(phone), code)
                && !StringUtils.equals(code, password)) {
            throw new ApiException("验证码/密码 错误");
        }

        //缓存用户信息到redis中
        String sessionId = CodeGenerator.uuid();
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setSessionId(sessionId);
        sessionInfo.setLoginId(loginId);
        sessionInfo.setLoginType(loginType);
        sessionInfo.setPermission(permission);
        int expireTime = rememberFlag ? Constants.CookieParam.REMEMBER_COOKIE_EXPIRE_SECOND : Constants.CookieParam.DEFAULT_COOKIE_EXPIRE_SECOND;
        sessionService.setMerchantSession(sessionId, sessionInfo, expireTime);

        String role = permission == null ? loginType : permission;

        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.equals(Constants.LoginType.USER, loginType)) {
            resultMap.put(Constants.CookieParam.SESSION_ID, sessionId);
        } else if (StringUtils.equals(Constants.LoginType.STAFF, loginType)) {
            resultMap.put(Constants.CookieParam.STAFF_SESSION_ID, sessionId);
        } else if (StringUtils.equals(Constants.LoginType.FACTORY, loginType)) {
            resultMap.put(Constants.CookieParam.FACTORY_SESSION_ID, sessionId);
        } else if (StringUtils.equals(Constants.LoginType.SERVICE_PROVIDER, loginType)) {
            resultMap.put(Constants.CookieParam.SERVICE_PROVIDER_SESSION_ID, sessionId);
        }
        resultMap.put("expire", expireTime);
        resultMap.put("role", role);
        resultMap.put("name", name);
        resultMap.put("logoUrl", logoUrl);

        return ResponseDo.buildSuccess(resultMap);
    }
}