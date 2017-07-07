package com.autoask.controller.user;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.service.merchant.ServiceProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hyy
 * @create 2016-10-23 23:12
 */
@Controller
@RequestMapping("/serviceProvider/")
public class UServiceProviderController {

    private static Logger LOG = LoggerFactory.getLogger(UServiceProviderController.class);

    @Autowired
    private ServiceProviderService serviceProviderService;

    /**
     * 获取附近的修理厂
     *
     * @param province
     * @param city
     * @param region
     * @return
     */
    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo list(@RequestParam("province") String province, @RequestParam("city") String city, @RequestParam("region") String region,
                           @RequestParam(value = "latitude", required = false) Double latitude,
                           @RequestParam(value = "longitude", required = false) Double longitude) {
        try {
            List<ServiceProvider> areaServiceProviderList = serviceProviderService.getAreaServiceProviderList(province, city, region,
                    latitude, longitude);
            ArrayList<Map<String, Object>> resultList = new ArrayList<>(areaServiceProviderList.size());
            for (ServiceProvider serviceProvider : areaServiceProviderList) {
                Map<String, Object> itemMap = new HashMap<>(6, 1);
                itemMap.put("address", serviceProvider.getAddress());
                itemMap.put("id", serviceProvider.getId());
                itemMap.put("name", serviceProvider.getName());
                itemMap.put("landmark", serviceProvider.getLandmark());
                itemMap.put("phone", serviceProvider.getPhone());
                itemMap.put("workingHours", serviceProvider.getStartTime()+"-"+serviceProvider.getEndTime());
                resultList.add(itemMap);
            }
            return ResponseDo.buildSuccess(resultList);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
