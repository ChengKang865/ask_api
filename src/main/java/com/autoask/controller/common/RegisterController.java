package com.autoask.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.autoask.cache.CacheService;
import com.autoask.cache.PhoneService;
import com.autoask.cache.SessionService;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CodeGenerator;
import com.autoask.common.util.Constants;
import com.autoask.common.util.CookieUtil;
import com.autoask.entity.mysql.User;
import com.autoask.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hyy
 * @create 2016-10-04 19:24
 */
@Controller
@RequestMapping("register/")
public class RegisterController {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo user(@RequestBody JSONObject paramJson,
                           HttpServletRequest request, HttpServletResponse response) {

        String phone = paramJson.getString("phone");
        String code = paramJson.getString("code");
        String recommendPhone = null;
        JSONObject qrJson = null;
        if (paramJson.containsKey("recommendPhone")) {
            recommendPhone = paramJson.getString("recommendPhone");
        }
        if (paramJson.containsKey("qrInfo")) {
            qrJson = paramJson.getJSONObject("qrInfo");
        }
        try {
            //校验手机验证码是否正确
            String verifyCode = phoneService.getVerifyCode(phone);
            if (!StringUtils.equals(verifyCode, code)) {
                return ResponseDo.buildError("验证码错误");
            }

            // 数据库插入user信息,以及user_asserts信息
            User user = userService.registerUser(phone, recommendPhone, qrJson);

            //redis中缓存session内容
            String sessionId = CodeGenerator.uuid();
            sessionService.setUserSession(sessionId, user.getUserId(), Constants.CookieParam.DEFAULT_COOKIE_EXPIRE_SECOND);

            //添加cookie
            CookieUtil.addSessionCookie(false, request, response, sessionId);

            Map<String, Object> resultMap = new HashMap<>(2);
            resultMap.put(Constants.CookieParam.SESSION_ID, sessionId);
            resultMap.put("expire", Constants.CookieParam.DEFAULT_COOKIE_EXPIRE_SECOND);
            return ResponseDo.buildSuccess(resultMap);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
