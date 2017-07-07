package com.autoask.controller.callback;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.pay.pingpp.config.PingppConfig;
import com.autoask.service.merchant.MerchantAssetsService;
import com.autoask.service.pay.PayService;
import com.autoask.service.refund.RefundService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

/**
 * @author hyy
 * @create 2016-10-31 11:45
 */
@Controller
@RequestMapping("/callback")
public class CallBackController {

    private static final Logger LOG = LoggerFactory.getLogger(CallBackController.class);

    @Autowired
    private PayService payService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private MerchantAssetsService merchantAssetsService;

    @RequestMapping(value = "/pingpp", method = RequestMethod.POST)
    public void pingppCallback(@RequestBody JSONObject json, HttpServletResponse response) {
        LOG.info("pingpp call back:{}", json.toJSONString());
        //成功返回200 不成功返回500
        if (StringUtils.equals(json.getString("type"), PingppConfig.EventType.CHARGE_SUCCEEDED)) {
            //付款成功
            try {
                payService.updateOrderPayed(json);
                response.setStatus(200);
            } catch (ApiException e) {
                LOG.error(e.getMessage());
                response.setStatus(500);
            }
        } else if (StringUtils.equals(json.getString("type"), PingppConfig.EventType.REFUND_SUCCEEDED)) {
            try {
                refundService.updateOrderRefund(json);
                response.setStatus(200);
            } catch (ApiException e) {
                LOG.error(e.getMessage());
                response.setStatus(500);
            }
        } else if (StringUtils.equals(json.getString("type"), PingppConfig.EventType.TRANSFER_SUCCEEDED)) {
            try {
                merchantAssetsService.updateMerchantBonusRecord(json);
                response.setStatus(200);
            } catch (ApiException e) {
                LOG.error(e.getMessage());
                response.setStatus(500);
            }
        }
    }

    @RequestMapping(value = "/ali/{channel}", method = RequestMethod.GET)
    public String aliSuccess(@PathVariable("channel") String channel) {

        if (StringUtils.equals(channel, "m")) {
            return "redirect:" + Constants.REDIRECT_SERVER + "mobile/index.html#/m/user/order";
        } else {
            return "redirect:" + Constants.REDIRECT_SERVER + "pc/index.html#/pc/user/order";
        }
    }
}
