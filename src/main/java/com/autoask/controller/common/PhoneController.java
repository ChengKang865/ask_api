package com.autoask.controller.common;

import com.autoask.cache.PhoneService;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CodeGenerator;
import com.autoask.common.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author hyy
 * @create 2016-10-04 18:34
 */
@Controller
@RequestMapping("phone/")
public class PhoneController {

    private static final Logger LOG = LoggerFactory.getLogger(PhoneController.class);

    @Autowired
    private PhoneService phoneService;

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @RequestMapping("/code/")
    @ResponseBody
    public ResponseDo getCode(@RequestParam("phone") String phone) {
        try {
            //检查phone是否非法
            if (CommonUtil.isPhoneNumber(phone)) {
                phoneService.sendPhoneVerifyCode(phone, CodeGenerator.generatorVerifyCode());
                return ResponseDo.buildSuccess("发送验证码成功");
            } else {
                return ResponseDo.buildSuccess("输入合法的手机号码");
            }
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
