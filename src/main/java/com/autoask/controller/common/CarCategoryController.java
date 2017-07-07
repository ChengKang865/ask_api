package com.autoask.controller.common;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.entity.mysql.CarCategory;
import com.autoask.service.common.CarCategoryService;
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

/**
 * 获取车型信息
 */
@Controller
@RequestMapping("car/")
public class CarCategoryController {

    private static final Logger LOG = LoggerFactory.getLogger(CarCategoryController.class);

    @Autowired
    private CarCategoryService carCategoryService;

    @RequestMapping(value = "brands/", method = RequestMethod.GET)
    @ResponseBody
    ResponseDo getBrandList() {
        try {
            List<CarCategory> carCategories = carCategoryService.queryCarCategories(null);
            ArrayList<HashMap<String, String>> resultList = new ArrayList<>();
            for (CarCategory carCategory : carCategories) {
                HashMap<String, String> itemMap = new HashMap<>();
                itemMap.put("name", carCategory.getName());
                itemMap.put("brand", carCategory.getSlug());
                resultList.add(itemMap);
            }
            return ResponseDo.buildSuccess(resultList);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "models/", method = RequestMethod.GET)
    @ResponseBody
    ResponseDo getModelList(@RequestParam("brand") String brand) {
        try {
            List<CarCategory> carCategories = carCategoryService.queryCarCategories(brand);
            ArrayList<HashMap<String, String>> resultList = new ArrayList<>();
            for (CarCategory carCategory : carCategories) {
                HashMap<String, String> itemMap = new HashMap<>();
                itemMap.put("name", carCategory.getName());
                itemMap.put("model", carCategory.getSlug());
                resultList.add(itemMap);
            }
            return ResponseDo.buildSuccess(resultList);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }


    @RequestMapping(value = "years/", method = RequestMethod.GET)
    @ResponseBody
    ResponseDo getYearList(@RequestParam("model") String model) {
        try {
            List<String> yearList = carCategoryService.getYearList(model);
            return ResponseDo.buildSuccess(yearList);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "details/", method = RequestMethod.GET)
    @ResponseBody
    ResponseDo getDetailList(@RequestParam("model") String model, @RequestParam("year") String year) {
        try {
            List<String> detailList = carCategoryService.getDetailList(model, year);
            return ResponseDo.buildSuccess(detailList);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
