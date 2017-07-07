package com.autoask.controller.user.order;

import com.autoask.cache.SessionService;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.LoginUtil;
import com.autoask.entity.mongo.merchant.Mechanic;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.entity.mysql.User;
import com.autoask.pay.wechat.ConfigUtil;
import com.autoask.service.config.ConfigService;
import com.autoask.service.merchant.MechanicService;
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
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hyy
 * @create 16/10/23 19:56
 */
@Controller
@RequestMapping("user/order/")
public class UserPreOrderController {

    private static Logger LOG = LoggerFactory.getLogger(UserPreOrderController.class);

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;


    @RequestMapping(value = "pre/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getPreInfo(HttpServletRequest request, @RequestParam(value = "mechanicId", required = false) String mechanicId) {
        try {

            Map<String, Object> resultMap = new HashMap(3, 1);

            String userId = LoginUtil.getSessionInfo().getLoginId();

            User user = userService.getUser(userId);
            if (user == null) {
                throw new ApiException("账号不存在");
            }
            //判断是否是微信浏览器
            String userAgent = request.getHeader("user-agent").toLowerCase();
            if (userAgent.indexOf("micromessenger") > 0) {
                //微信浏览器,需要获取用户的open_id
                //记录这一次session的openId
                String sessionId = LoginUtil.getSessionInfo().getSessionId();
                if (StringUtils.isEmpty(sessionService.getSessionOpenId(sessionId))) {
                    String wxUrl = MessageFormat.format(Constants.REDIRECT_URL, ConfigUtil.APPID, sessionId);
                    resultMap.put("redirect", wxUrl);
                }
            }
            if (StringUtils.isNotEmpty(mechanicId)) {
                Mechanic mechanic = mechanicService.findById(mechanicId);
                if (null == mechanic) {
                    throw new ApiException("修理工不存在");
                }
                ServiceProvider serviceProvider = serviceProviderService.findById(mechanic.getServiceProviderId());

                Map<String, Object> serviceProviderMap = new HashMap(2, 1);
                serviceProviderMap.put("address", serviceProvider.getAddress());
                serviceProviderMap.put("name", serviceProvider.getName());
                serviceProviderMap.put("landmark", serviceProvider.getLandmark());
                serviceProviderMap.put("id", serviceProvider.getId());
                resultMap.put("serviceProvider", serviceProviderMap);
            } else {
                //获取上一次的serviceProvider
                ServiceProvider lastServiceProvider = serviceProviderService.getUserLastServiceProvider(userId);
                if (null == lastServiceProvider) {
                    resultMap.put("lastServiceProvider", null);
                } else {
                    Map<String, Object> serviceProviderMap = new HashMap(2, 1);
                    serviceProviderMap.put("address", lastServiceProvider.getAddress());
                    serviceProviderMap.put("name", lastServiceProvider.getName());
                    serviceProviderMap.put("landmark", lastServiceProvider.getLandmark());
                    serviceProviderMap.put("id", lastServiceProvider.getId());
                    resultMap.put("lastServiceProvider", serviceProviderMap);
                }
            }
            long deliveryFreeAmount = configService.getDeliveryFreeAmount();
            resultMap.put("freeAmount", deliveryFreeAmount);
            return ResponseDo.buildSuccess(resultMap);
        } catch (ApiException e) {
            return ResponseDo.buildError(e.getMessage());
        }
    }


    @RequestMapping(value = "re-pay-pre/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo preRePay(HttpServletRequest request) {
        //判断是否是微信浏览器
        try {
            Map<String, Object> resultMap = new HashMap();

            String userAgent = request.getHeader("user-agent").toLowerCase();
            if (userAgent.indexOf("micromessenger") > 0) {
                //微信浏览器,需要获取用户的open_id
                //记录这一次session的openId
                String sessionId = LoginUtil.getSessionInfo().getSessionId();
                if (StringUtils.isEmpty(sessionService.getSessionOpenId(sessionId))) {
                    String wxUrl = MessageFormat.format(Constants.RE_PAY_REDIRECT_URL, ConfigUtil.APPID, sessionId);
                    resultMap.put("redirect", wxUrl);
                }
            }
            return ResponseDo.buildSuccess(resultMap);
        } catch (ApiException e) {
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
