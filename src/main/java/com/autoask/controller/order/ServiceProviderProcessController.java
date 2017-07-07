package com.autoask.controller.order;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.service.order.ServiceProviderProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author hyy
 * @create 2016-12-12 15:31
 */
@Controller
@RequestMapping("order/serviceProvider/")
public class ServiceProviderProcessController {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceProviderProcessController.class);

    @Autowired
    private ServiceProviderProcessService serviceProviderProcessService;

    /**
     * 接受订单
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "appoint/confirm/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo confirmAppoint(@RequestParam("orderId") String orderId) {
        try {
            serviceProviderProcessService.updateAppointConfirm(orderId);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 验证验证码
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "appoint/validate/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo validateAppoint(@RequestParam("code") String code) {
        try {
            String orderId = serviceProviderProcessService.updateAppointValidate(code);
            return ResponseDo.buildSuccess(orderId);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
