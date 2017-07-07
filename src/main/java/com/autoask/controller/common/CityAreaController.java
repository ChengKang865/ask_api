package com.autoask.controller.common;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.entity.mysql.CityArea;
import com.autoask.service.common.CityAreaService;
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
 * Created by hp on 16-8-8.
 */
@Controller
@RequestMapping(value = "/area")
public class CityAreaController {
    private static final Logger LOG = LoggerFactory.getLogger(CityAreaController.class);

    @Autowired
    private CityAreaService cityAreaService;


    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    ResponseDo list(@RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        LOG.info("get city by parent's id");

        try {
            List<CityArea> cityAreaList = cityAreaService.getCityAreaByParentId(parentId);
            ArrayList<Map<String, Object>> resultList = new ArrayList<>();
            for (CityArea cityArea : cityAreaList) {
                HashMap<String, Object> itemMap = new HashMap<>(2, 1);
                itemMap.put("id", cityArea.getId());
                itemMap.put("name", cityArea.getAreaName());

                resultList.add(itemMap);
            }
            return ResponseDo.buildSuccess(resultList);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }


    @RequestMapping(value = "city/list/", method = RequestMethod.GET)
    @ResponseBody
    ResponseDo getCityAreaListByParentId(@RequestParam("parentId") Integer parentId) {
        LOG.info("get city by parent's id");

        try {
            List<CityArea> cityAreas = cityAreaService.getCityAreaByParentId(parentId);
            return ResponseDo.buildSuccess(cityAreas);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "street/list/", method = RequestMethod.GET)
    @ResponseBody
    ResponseDo getStreetList(@RequestParam("province") String province,
                             @RequestParam("city") String city,
                             @RequestParam("region") String region) {
        LOG.info("get city by parent's id");

        try {
            List<String> cityAreas = cityAreaService.getStreets(province, city, region);
            return ResponseDo.buildSuccess(cityAreas);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
