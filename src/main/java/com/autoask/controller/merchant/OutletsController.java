package com.autoask.controller.merchant;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.Constants;
import com.autoask.common.util.ListSlice;
import com.autoask.controller.common.BaseController;
import com.autoask.entity.mongo.merchant.Outlets;
import com.autoask.service.merchant.OutletsService;
import com.github.pagehelper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by weid on 16-8-14.
 */
@Controller
@RequestMapping(value = "/merchant/outlets/")
public class OutletsController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(MechanicController.class);

    @Autowired
    private OutletsService outletsService;

    @RequestMapping(value = "get/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getOutletsById(@RequestParam(value = "id") String outletsId) {
        LOG.info("get outlets by id.");

        if (StringUtil.isEmpty(outletsId)) {
            return ResponseDo.buildError("id不能为空");
        }

        Outlets outlets = outletsService.findById(outletsId);
        return ResponseDo.buildSuccess(outlets);
    }

    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getOutletsList(@RequestParam(value = "page") Integer page,
                                     @RequestParam(value = "count") Integer count,
                                     @RequestParam(value = "phone", required = false) String phone,
                                     @RequestParam(value = "name", required = false) String name) {
        LOG.info("get outlets list.");
        try {
            int start = CommonUtil.pageToSkipStart(page, count);
            count = CommonUtil.cleanCount(count);
            ListSlice outletsList = outletsService.getOutletsList(phone, name, start, count);
            return ResponseDo.buildSuccess(outletsList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "add/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo addOutlets(Outlets outlets) {
        LOG.info("add outlets.");
        try {
            if (null != outlets) {
                outlets.setPayType(Constants.PayType.WE_CHAT);
            }
            outletsService.create(outlets);
            return ResponseDo.buildSuccess("添加分销点成功!");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updateOutlets(Outlets outlets) {
        LOG.info("update outlets.");

        if (outlets == null || StringUtil.isEmpty(outlets.getId())) {
            return ResponseDo.buildError("id不能为空");
        }

        try {
            outletsService.updateSelective(outlets.getId(), outlets);
            return ResponseDo.buildSuccess("更新分销点成功");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "delete/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo deleteOutlets(@RequestParam(value = "id") String outletsId) {
        LOG.info("delete outlets.");

        try {
            outletsService.deleteById(outletsId);
            return ResponseDo.buildSuccess("删除分销点成功!");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

}
