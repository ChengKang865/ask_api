package com.autoask.controller.order;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.service.order.FactoryProcessService;
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
 * @create 2016-12-12 15:30
 */
@Controller
@RequestMapping("order/factory/")
public class FactoryProcessController {

    private static final Logger LOG = LoggerFactory.getLogger(FactoryProcessController.class);

    @Autowired
    private FactoryProcessService factoryProcessService;

    @RequestMapping(value = "online/confirm/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo onlineConfirm(@RequestParam("orderId") String orderId,
                                    @RequestParam("expressCompany") String expressCompany,
                                    @RequestParam("deliverySerial") String deliverySerial) {
        try {
            factoryProcessService.updateOnlineConfirm(orderId, expressCompany, deliverySerial);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
