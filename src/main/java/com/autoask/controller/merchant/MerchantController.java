package com.autoask.controller.merchant;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.LoginUtil;
import com.autoask.entity.mongo.invoice.VatInvoice;
import com.autoask.service.merchant.MerchantService;
import com.autoask.service.qr.QRCodeService;
import com.github.pagehelper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by weid on 16-11-5.
 */
@Controller
@RequestMapping(value = "/merchant/")
public class MerchantController {
    public static final Logger LOG = LoggerFactory.getLogger(MechanicController.class);

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private QRCodeService qrCodeService;


    /**
     * @param amount 申请的总金额
     * @param fee    手续费
     * @return
     */
    @RequestMapping(value = "share/apply", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo shareApply(@RequestParam("amount") BigDecimal amount,
                                 @RequestParam("fee") BigDecimal fee) {

        try {
            String merchantType = LoginUtil.getSessionInfo().getLoginType();
            String merchantId = LoginUtil.getSessionInfo().getLoginId();
            merchantService.saveShareApply(merchantId, merchantType, amount, fee);
            return ResponseDo.buildSuccess("申请成功");
        } catch (Exception e) {
            LOG.error("Merchant share apply failure", e);
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "bindQRCode/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo bindQRCode(@RequestParam("code") String code, @RequestParam("merchantType") String merchantType, @RequestParam("merchantId") String merchantId) {
        try {
            qrCodeService.bindQRCode(code, merchantType, merchantId);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "account/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getMerchantAccount() {
        try {
            Map<String, Object> resultMap = merchantService.getMerchantAccount();
            return ResponseDo.buildSuccess(resultMap);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "account/update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updateMerchantAccount(@RequestParam("aliPayAccount") String aliPayAccount, @RequestParam("aliPayUserName") String aliPayUserName) {
        try {
            merchantService.updateMerchantAliAccount(aliPayAccount, aliPayUserName);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "password/update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updatePassword(@RequestParam("password") String password) {
        try {
            merchantService.updateMerchantPassword(password);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "vatInvoice/get/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getVatInvoice() {
        try {
            VatInvoice vatInvoice = merchantService.getMerchantVatInvoice();
            return ResponseDo.buildSuccess(vatInvoice);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "vatInvoice/update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updateInvoice(@RequestBody VatInvoice vatInvoice) {
        try {
            merchantService.updateMerchantVatInvoice(vatInvoice);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "qrCode/self/get/",method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getOwnQRCode() {
        try {
            String qrCode = merchantService.getSelfQRCode();
            return ResponseDo.buildSuccess(qrCode);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "qrCode/self/bind/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo bindSelfQRCode(@RequestParam("code") String code) {
        try {
            merchantService.updateSelfQRCode(code);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}