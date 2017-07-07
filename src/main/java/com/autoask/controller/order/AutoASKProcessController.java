package com.autoask.controller.order;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.service.order.AutoASKProcessService;
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
@RequestMapping("order/autoask/")
public class AutoASKProcessController {

    private static final Logger LOG = LoggerFactory.getLogger(AutoASKProcessController.class);

    @Autowired
    private AutoASKProcessService autoASKProcessService;

    @RequestMapping(value = "online/confirm/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo onlineConfirm(@RequestParam("orderId") String orderId,
                                    @RequestParam("expressCompany") String expressCompany,
                                    @RequestParam("deliverySerial") String deliverySerial) {
        try {
            autoASKProcessService.updateAutoASKConfirm(orderId, expressCompany, deliverySerial);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }


    @RequestMapping(value = "appoint/refuse/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo pendingAppoint(@RequestParam("orderId") String orderId) {
        try {
            autoASKProcessService.updateAppointRefuse(orderId);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "refuse/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo refuse(@RequestParam("orderId") String orderId) {
        try {
            autoASKProcessService.updateRefuse(orderId);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
