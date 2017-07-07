package com.autoask.controller.merchant;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.controller.common.BaseController;
import com.autoask.service.merchant.MerchantShareApplyService;
import com.github.pagehelper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author hyy
 * @create 16/11/6 15:43
 */
@Controller
@RequestMapping(value = "/merchant/apply/")
public class MerchantShareApplyController {

    private static final Logger LOG = LoggerFactory.getLogger(MerchantShareApplyController.class);

    @Autowired
    private MerchantShareApplyService merchantShareApplyService;


    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo shareApply(@RequestParam(value = "merchantId", required = false) String merchantId,
                                 @RequestParam(value = "merchantType", required = false) String merchantType,
                                 @RequestParam(value = "status", required = false) String status,
                                 @RequestParam(value = "startTime", required = false) String startTime,
                                 @RequestParam(value = "endTime", required = false) String endTime,
                                 @RequestParam(value = "page") Integer page,
                                 @RequestParam(value = "count") Integer count) {
        LOG.info("merchant share apply, merchantId:{}, merchantType:{}", merchantId, merchantType);

        try {
            int start = CommonUtil.pageToSkipStart(page, count);
            int limit = CommonUtil.cleanCount(count);
            ListSlice applies = merchantShareApplyService.selectMerchantApplies(merchantType, merchantId, status, startTime, endTime, start, limit);
            return ResponseDo.buildSuccess(applies);
        } catch (Exception e) {
            LOG.error("Merchant share apply failure", e);
            return ResponseDo.buildError(e.getMessage());
        }
    }
}