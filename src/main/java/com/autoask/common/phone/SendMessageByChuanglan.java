package com.autoask.common.phone;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.HttpClientUtil;
import com.autoask.common.util.PropertiesUtil;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by licheng on 2016/9/19.
 */
public class SendMessageByChuanglan {

    private static final Logger LOG = LoggerFactory.getLogger(SendMessageByChuanglan.class);

    /**
     * 发送短消息
     *
     * @param phone      手机号
     * @param verifyCode 验证码
     * @throws ApiException 业务异常
     */
    public static void sendCodeMessage(String phone, String verifyCode) throws ApiException {
        LOG.debug("Send message by Chuanglan begin, send phone:{} verifyCode:{}", phone, verifyCode);
        // 调用运营商接口发送验证码短信

        sendMessage(phone, MessageFormat.format(PropertiesUtil.getProperty("message.send.chuanglan.msg", ""), verifyCode));
    }

    public static void sendMessage(String phone, String message) throws ApiException {

        String httpUrl = PropertiesUtil.getProperty("message.send.chuanglan.url", "");

        Map<String, String> params = new HashMap<>(8, 1);
        params.put("account", PropertiesUtil.getProperty("message.send.chuanglan.account", ""));
        params.put("pswd", PropertiesUtil.getProperty("message.send.chuanglan.pswd", ""));
        params.put("mobile", phone);
        params.put("needstatus", PropertiesUtil.getProperty("message.send.chuanglan.needstatus", "true"));
        params.put("msg", message);
        params.put("product", null);
        params.put("extno", null);

        CloseableHttpResponse response = null;
        try {
            response = HttpClientUtil.get(httpUrl, params, new ArrayList<Header>(0), Constants.Charset.UTF8);
            if (HttpClientUtil.isStatusOK(response)) {
                LOG.info("Send verifyCode success");
                LOG.info(HttpClientUtil.parseResponse(response));
            } else {
                LOG.warn("Send verifyCode message failure, phone:{}, message:{}", phone, message);
                LOG.warn(HttpClientUtil.parseResponse(response));
            }
        } finally {
            if (response != null) {
                HttpClientUtil.close(response);
            }
        }
    }
}
