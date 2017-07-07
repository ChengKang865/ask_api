package com.autoask.controller.statistics;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.mapper.OrderInfoMapper;
import com.autoask.service.statistics.StatisticsService;
import com.autoask.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author hyy
 * @create 16/11/7 01:55
 */
@Controller
@RequestMapping("/statistics/")
public class StatisticsController {

    private static final Logger LOG = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private StatisticsService statisticsService;


    @RequestMapping(value = "/user/list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo listUser(@RequestParam(value = "phone", required = false) String phone,
                               @RequestParam(value = "startTime", required = false) String startTime,
                               @RequestParam(value = "endTime", required = false) String entTime,
                               @RequestParam(value = "page") int page,
                               @RequestParam(value = "count") int count) {
        LOG.info("enter into statistics user controller user list");
        try {
            int start = CommonUtil.pageToSkipStart(page, count);
            int limit = CommonUtil.cleanCount(count);
            ListSlice userList = userService.getUserList(phone, startTime, entTime, start, limit);
            return ResponseDo.buildSuccess(userList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "index/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo index() {
        LOG.info("enter into statistics user controller user list");
        try {
            Map<String, Object> resultMap = statisticsService.getAutoASKStatisticsIndex();
            return ResponseDo.buildSuccess(resultMap);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "serviceProvider/index/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo serviceProviderIndex() {
        try {
            Map<String, Object> resultMap = statisticsService.getServiceProviderIndex();
            return ResponseDo.buildSuccess(resultMap);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "factory/index/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo factoryIndex() {
        try {
            Map<String, Object> resultMap = statisticsService.getFactoryIndex();
            return ResponseDo.buildSuccess(resultMap);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
