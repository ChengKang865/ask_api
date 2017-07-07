package com.autoask.service.impl.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.autoask.cache.PhoneService;
import com.autoask.cache.SessionService;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.HttpClientUtil;
import com.autoask.pay.wechat.ConfigUtil;
import com.autoask.service.common.WeChatService;
import com.autoask.service.merchant.MerchantService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;

/**
 * Created by hp on 16-10-23.
 * 获取微信 openId 的 service.
 */
@Service("WeChatService")
public class WeChatServiceImpl implements WeChatService {

    private static final Logger LOG = LoggerFactory.getLogger(WeChatServiceImpl.class);

    @Autowired
    private SessionService sessionService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private PhoneService phoneService;

    /**
     * 获取 access_token
     *
     * @param code      code 从微信端获取.
     * @param sessionId sessionId
     * @return Map
     * @throws ApiException
     */
    @Override
    public Map<String, Object> updateUserOpenId(String code, String sessionId) throws ApiException {
        JSONObject json = queryUserWeChatInfo(code);
        if (!json.containsKey("openid") || StringUtils.isEmpty(json.getString("openid"))) {
            throw new ApiException("获取用户openId失败");
        }

        String openId = json.getString("openid");
        sessionService.setSessionOpenId(sessionId, openId);
        return json;
    }

    @Override
    public String getOpenId(String code) throws ApiException {
        JSONObject json = queryUserWeChatInfo(code);
        if (!json.containsKey("openid") || StringUtils.isEmpty(json.getString("openid"))) {
            throw new ApiException("获取用户openId失败");
        }
        return json.getString("openid");
    }

    /**
     * 调用微信的接口获取用户的openId的信息
     *
     * @param code
     * @return
     * @throws ApiException
     */
    private JSONObject queryUserWeChatInfo(String code) throws ApiException {
        String oauth2Url = MessageFormat.format(ConfigUtil.OAUTH2_URL, ConfigUtil.APPID, ConfigUtil.APP_SECRET, code);
        CloseableHttpResponse response = null;
        InputStream content = null;
        try {
            response = HttpClientUtil.get(oauth2Url, Collections.EMPTY_MAP, Collections.EMPTY_LIST, "utf-8");
            content = response.getEntity().getContent();
            JSONObject json = JSON.parseObject(IOUtils.toString(content));
            return json;
        } catch (Exception e) {
            LOG.warn("get user openId info failure", e);
            throw new ApiException(e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                    response = null;
                }
            } catch (IOException e) {
                LOG.error("Close response error.");
            }
            try {
                if (null != content) {
                    content.close();
                    content = null;
                }
            } catch (IOException e) {
                LOG.error("Close inputStream error.");
            }
        }
    }

}