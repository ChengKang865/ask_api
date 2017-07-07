package com.autoask.controller.merchant;


import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.Constants;
import com.autoask.common.util.ListSlice;
import com.autoask.controller.common.BaseController;
import com.autoask.entity.mongo.merchant.Mechanic;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.service.merchant.MechanicService;
import com.autoask.service.merchant.ServiceProviderService;
import com.github.pagehelper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by weid on 16-8-13.
 */
@Controller
@RequestMapping(value = "/merchant/mechanic/")
public class MechanicController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(MechanicController.class);

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @RequestMapping(value = "get/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getMechanicById(@RequestParam(value = "id") String mechanicId) {


        try {
            LOG.info("get mechanic");

            if (StringUtil.isEmpty(mechanicId)) {
                return ResponseDo.buildError("id不能为空");
            }
            Mechanic mechanic = mechanicService.findById(mechanicId);
            ServiceProvider serviceProvider = serviceProviderService.findById(mechanic.getServiceProviderId());
            return ResponseDo.buildSuccess(mechanic);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getMechanicList(@RequestParam(value = "page") Integer page,
                                      @RequestParam(value = "count") Integer count,
                                      @RequestParam(value = "serviceProviderId", required = false) String serviceProviderId,
                                      @RequestParam(value = "phone", required = false) String phone,
                                      @RequestParam(value = "name", required = false) String name) {
        LOG.info("get mechanic list");
        try {
            int start = CommonUtil.pageToSkipStart(page, count);
            int limit = CommonUtil.cleanCount(count);
            ListSlice mechanicList = mechanicService.getMechanicList(serviceProviderId, phone, name, start, limit);
            return ResponseDo.buildSuccess(mechanicList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "add/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo addMechanic(Mechanic mechanic) {
        try {
            if (null != mechanic) {
                mechanic.setPayType(Constants.PayType.WE_CHAT);
            }
            mechanicService.create(mechanic);
            return ResponseDo.buildSuccess("创建修理工成功!");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updateMechanic(Mechanic mechanic) {
        if (mechanic == null || StringUtil.isEmpty(mechanic.getId())) {
            return ResponseDo.buildError("id不能为空");
        }

        try {
            mechanicService.updateSelective(mechanic);
            return ResponseDo.buildSuccess("更新修理工成功");
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "delete/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo deleteMechanic(@RequestParam(value = "id") String mechanicId) {
        try {
            mechanicService.deleteById(mechanicId);
            return ResponseDo.buildSuccess("删除修理工成功!");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

}
