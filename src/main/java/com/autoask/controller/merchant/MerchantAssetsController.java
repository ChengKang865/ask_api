package com.autoask.controller.merchant;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.service.assets.MerchantAssetsRecordService;
import com.autoask.service.assets.MerchantAssetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by weid on 16-11-5.
 */
@Controller
@RequestMapping("/merchantAssets/")
public class MerchantAssetsController {

    private static final Logger LOG = LoggerFactory.getLogger(MerchantAssetsController.class);

    @Autowired
    private MerchantAssetsService merchantAssetsService;

    @Autowired
    private MerchantAssetsRecordService merchantAssetsRecordService;

    //商户资产列表
    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo list(@RequestParam(value = "merchantType", required = false) String merchantType,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam("page") int page, @RequestParam("count") int count) {
        int start = CommonUtil.pageToSkipStart(page, count);
        int limit = CommonUtil.cleanCount(count);
        try {
            ListSlice merchantAssetsList = merchantAssetsService.selectMerchantAssetsList(merchantType, name, start, limit);
            return ResponseDo.buildSuccess(merchantAssetsList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "record/list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo listRecord(@RequestParam(value = "merchantType", required = false) String merchantType,
                                 @RequestParam(value = "factoryId", required = false) String factoryId,
                                 @RequestParam(value = "serviceProviderId", required = false) String serviceProviderId,
                                 @RequestParam(value = "outletsId", required = false) String outletsId,
                                 @RequestParam(value = "mechanicId", required = false) String mechanicId,
                                 @RequestParam(value = "startTime", required = false) String startTime,
                                 @RequestParam(value = "endTime", required = false) String endTime,
                                 @RequestParam(value = "orderId", required = false) String orderId,
                                 @RequestParam("page") int page, @RequestParam("count") int count) {
        try {
            int start = CommonUtil.pageToSkipStart(page, count);
            int limit = CommonUtil.cleanCount(count);
            ListSlice merchantAssetsList = merchantAssetsRecordService.selectMerchantAssetsRecordList(merchantType, factoryId, serviceProviderId, outletsId, mechanicId, orderId, startTime, endTime, start, limit);
            return ResponseDo.buildSuccess(merchantAssetsList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
