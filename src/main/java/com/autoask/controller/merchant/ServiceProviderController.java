package com.autoask.controller.merchant;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.Constants;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.LoginUtil;
import com.autoask.controller.common.BaseController;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.service.merchant.ServiceProviderService;
import com.github.pagehelper.StringUtil;
import org.apache.commons.lang3.StringUtils;
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
 * Created by weid on 16-8-8.
 */
@Controller
@RequestMapping(value = "/merchant/serviceProvider/")
public class ServiceProviderController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(ServiceProviderController.class);

    @Autowired
    private ServiceProviderService serviceProviderService;

    @RequestMapping(value = "get/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getServiceProviderById(@RequestParam(value = "id", required = false) String serviceProviderId) {

        LOG.info("get service provider");

        try {
            if (StringUtil.isEmpty(serviceProviderId)) {
                if (StringUtils.equals(LoginUtil.getLoginType(), Constants.LoginType.SERVICE_PROVIDER)) {
                    serviceProviderId = LoginUtil.getLoginId();
                } else {
                    throw new ApiException("没有权限");
                }
            }
            ServiceProvider serviceProvider = serviceProviderService.findById(serviceProviderId);
            return ResponseDo.buildSuccess(serviceProvider);
        } catch (ApiException e) {
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getServiceProviderList(@RequestParam(value = "page") Integer page,
                                             @RequestParam(value = "count") Integer count,
                                             @RequestParam(value = "phone", required = false) String phone,
                                             @RequestParam(value = "name", required = false) String name) {

        try {
            int start = CommonUtil.pageToSkipStart(page, count);
            int limit = CommonUtil.cleanCount(count);
            ListSlice result = serviceProviderService.getServiceProviderList( phone, name, start, limit);
            return ResponseDo.buildSuccess(result);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "add/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo addServiceProvider(ServiceProvider serviceProvider) {

        LOG.info("add service provider");

        try {
            if (null != serviceProvider) {
                serviceProvider.setPayType(Constants.PayType.ALI);
            }
            serviceProviderService.create(serviceProvider);
            return ResponseDo.buildSuccess("添加服务点成功!");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updateServiceProvider(ServiceProvider serviceProvider) {

        LOG.info("update service provider");

        if (serviceProvider == null || StringUtil.isEmpty(serviceProvider.getId())) {
            return ResponseDo.buildError("id不能为空");
        }

        try {
            serviceProviderService.updateSelective(serviceProvider.getId(), serviceProvider);
            return ResponseDo.buildSuccess("更新服务点成功!");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "delete/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo deleteServiceProvider(@RequestParam(value = "id") String serviceProviderId) {

        LOG.info("delete service provider");

        try {
            serviceProviderService.deleteById(serviceProviderId);
            return ResponseDo.buildSuccess("删除服务点成功!");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
