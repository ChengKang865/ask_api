package com.autoask.controller.user.info;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.invoice.Invoice;
import com.autoask.service.invoice.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author hyy
 * @create 16/11/19 20:02
 */
@Controller
@RequestMapping("user/invoice/")
public class InvoiceController {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private InvoiceService invoiceService;


    @RequestMapping(value = "update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo insertOrUpdate(@RequestBody Invoice invoice) {
        try {
            invoiceService.updateOrInsertInvoice(invoice);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "view/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getUserInvoice() {
        try {
            Invoice userInvoice = invoiceService.getUserInvoice();
            return ResponseDo.buildSuccess(userInvoice);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
