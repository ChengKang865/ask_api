package com.autoask.cache.impl;

import com.autoask.cache.CacheService;
import com.autoask.cache.PhoneService;
import com.autoask.common.exception.ApiException;
import com.autoask.common.phone.MessageService;
import com.autoask.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hyy
 * @create 2016-09-07 13:16
 */
@Service("phoneService")
public class PhoneServiceImpl implements PhoneService {

    private static final String PHONE_VERIFY_CODE_PREFIX = "phone-verify:";

    private static final String PHONE_LAST_SEND_TIME = "phone-last-send:";

    private static final Long PHONE_GAP_TIME = 60000L;

    //手机验证码的默认的失效时间为60秒
    private static final int EXPIRE_TIME = 600;

    @Autowired
    private CacheService cacheService;


    @Override
    public void sendPhoneVerifyCode(String phone, String verifyCode) throws ApiException {

        String lastTimeStr = cacheService.get(buildPhoneLastSendTime(phone));
        if (StringUtils.isNotEmpty(lastTimeStr)) {
            Long lastTime = Long.parseLong(lastTimeStr);
            if (DateUtil.getDate().getTime() - lastTime < PHONE_GAP_TIME) {
                throw new ApiException("请30秒后重发");
            }
        }

        if (MessageService.sendCode(phone, verifyCode)) {
            cacheService.set(buildPhoneKey(phone), verifyCode, EXPIRE_TIME);
            cacheService.set(buildPhoneLastSendTime(phone), DateUtil.getDate().getTime(), EXPIRE_TIME);
        } else {
            throw new ApiException("发送短信失败,请重新尝试");
        }
    }

    @Override
    public String getVerifyCode(String phone) {
        return cacheService.get(buildPhoneKey(phone));
    }

    private static String buildPhoneKey(String phone) {
        return PHONE_VERIFY_CODE_PREFIX + phone;
    }

    private static String buildPhoneLastSendTime(String phone) {
        return PHONE_LAST_SEND_TIME + phone;
    }
}
