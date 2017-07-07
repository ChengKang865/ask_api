package com.autoask.controller.common;

import com.autoask.cache.SessionService;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CodeGenerator;
import com.autoask.common.util.Constants;
import com.autoask.pay.wechat.ConfigUtil;
import com.autoask.service.common.WeChatService;
import com.autoask.service.merchant.MerchantService;
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
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 16-10-23.
 */
@RequestMapping(value = "/wechat/")
@Controller
public class WeChatController {

    private static final Logger LOG = LoggerFactory.getLogger(WeChatController.class);

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private MerchantService merchantService;


    /**
     * 商城用户端访问
     *
     * @param code      根据code获取openId，属于微信回传的信息
     * @param sessionId sessionId
     * @param response
     */
    @RequestMapping(value = "/redirect/", method = {RequestMethod.GET})
    public void userWeChatRedirect(@RequestParam("code") String code,
                                   @RequestParam(value = "state") String sessionId,
                                   HttpServletResponse response) {

        LOG.info("GET method. Input param, code:{}, userId: {}", code, sessionId);

        try {
            Map<String, Object> token = weChatService.updateUserOpenId(code, sessionId);

            //重定向到购物车页面
            response.sendRedirect(Constants.REDIRECT_SERVER + "mobile/index.html#/m/shopping_cart");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
        } catch (IOException err) {
            LOG.error(err.getMessage());
        }
    }

    /**
     * 接受微信的open_id
     *
     * @param code
     * @param sessionId
     * @param response
     * @return
     */
    @RequestMapping(value = "/redirect/merchant/", method = {RequestMethod.GET})
    public void merchantWeChatRedirect(@RequestParam("code") String code,
                                       @RequestParam(value = "state") String sessionId,
                                       HttpServletResponse response) {
        try {
            String openId = weChatService.getOpenId(code);
            sessionService.setActiveSessionId(sessionId, openId);
            response.sendRedirect(Constants.REDIRECT_SERVER + "mobile/index.html#/m/active_form");
        } catch (IOException e) {
            LOG.error(e.getMessage());
        } catch (ApiException e) {
            LOG.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/redirect/re-pay/", method = {RequestMethod.GET})
    @ResponseBody
    public void preRePay(@RequestParam("code") String code, @RequestParam("state") String sessionId, HttpServletResponse response) {
        try {
            Map<String, Object> token = weChatService.updateUserOpenId(code, sessionId);

            //TODO定向到m站的重新支付页面
            response.sendRedirect(Constants.REDIRECT_SERVER + "mobile/index.html#/m/re-pay");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
        } catch (IOException err) {
            LOG.error(err.getMessage());
        }
    }

    @RequestMapping(value = "/preActive/", method = {RequestMethod.GET})
    @ResponseBody
    public ResponseDo preActive(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.indexOf("micromessenger") > 0) {
            //微信浏览器,需要获取用户的open_id
            Map<String, Object> resultMap = new HashMap<>();
            String activeSessionId = request.getParameter("activeSessionId");
            if (StringUtils.isEmpty(activeSessionId) || StringUtils.isEmpty(sessionService.getActiveSessionId(activeSessionId))) {
                activeSessionId = CodeGenerator.uuid();
                String wxUrl = MessageFormat.format(Constants.MERCHANT_OPEN_ID_URL, ConfigUtil.APPID, activeSessionId);
                resultMap.put("redirect", wxUrl);
                resultMap.put("activeSessionId", activeSessionId);
                return ResponseDo.buildSuccess(resultMap);
            }
            resultMap.put("pass", true);
            return ResponseDo.buildSuccess(resultMap);
        } else {
            return ResponseDo.buildError("请在微信中打开激活");
        }
    }

    @RequestMapping(value = "/active/", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseDo active(@RequestParam("phone") String phone, @RequestParam("code") String code, @RequestParam("activeSessionId") String activeSessionId) {
        try {
            merchantService.activeMerchant(phone, code, activeSessionId);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
