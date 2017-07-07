package com.autoask.controller.merchant;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.controller.common.BaseController;
import com.autoask.entity.mongo.merchant.Factory;
import com.autoask.service.merchant.FactoryService;
import com.autoask.service.order.OrderService;
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
 * Created by weid on 16-8-14.
 */
@Controller
@RequestMapping(value = "/merchant/factory/")
public class FactoryController extends BaseController {

    public static final Logger LOG = LoggerFactory.getLogger(FactoryController.class);

    @Autowired
    private FactoryService factoryService;

    @RequestMapping(value = "get/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo get(@RequestParam(value = "id",required = false) String id) {
        LOG.info("get factory");
        try {
            Factory factory  = factoryService.findById(id);
            return ResponseDo.buildSuccess(factory);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo list(@RequestParam(value = "page") Integer page,
                           @RequestParam(value = "count") Integer count,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "phone", required = false) String phone) {

        LOG.info("get factory list");

        int start = CommonUtil.pageToSkipStart(page, count);
        Integer limit = CommonUtil.cleanCount(count);

        ListSlice result = factoryService.getFactoryList(phone, name, start, limit);
        return ResponseDo.buildSuccess(result);
    }

    @RequestMapping(value = "add/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo add(Factory factory) {
        LOG.info("add factory");

        try {
            factoryService.create(factory);
            return ResponseDo.buildSuccess("创建工厂成功!");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo update(Factory factory) {
        LOG.info("update factory {} ", factory.getId());

        try {
            factoryService.updateSelective(factory.getId(), factory);
            return ResponseDo.buildSuccess("更新工厂成功");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "delete/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo delete(@RequestParam(value = "id") String factoryId) {
        LOG.info("delete factory {} ", factoryId);

        try {
            factoryService.deleteById(factoryId);
            return ResponseDo.buildSuccess("删除工厂成功!");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
