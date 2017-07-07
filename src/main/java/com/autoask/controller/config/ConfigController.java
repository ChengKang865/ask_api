package com.autoask.controller.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.config.Banner;
import com.autoask.service.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hyy
 * @create 16/10/19 16:31
 */
@Controller
@RequestMapping("config/")
public class ConfigController {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    private ConfigService configService;

    @RequestMapping("/banner/list/")
    @ResponseBody
    public ResponseDo getBannerList(@RequestParam("channel") String channel) {
        try {
            List<Banner> bannerList = configService.getBannerList(channel);
            return ResponseDo.buildSuccess(bannerList);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/banner/update/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo updateBannerList(@RequestBody JSONObject paramJson) {
        String channel = paramJson.getString("channel");
        JSONArray bannerArr = paramJson.getJSONArray("bannerList");
        List<Banner> bannerList = new ArrayList<>(bannerArr.size());
        for (int i = 0; i < bannerArr.size(); i++) {
            Banner banner = new Banner();
            JSONObject jsonObject = bannerArr.getJSONObject(i);
            banner.setPicUrl(jsonObject.getString("picUrl"));
            banner.setSort(jsonObject.getInteger("sort"));
            banner.setLinkUrl(jsonObject.getString("linkUrl"));
            banner.setChannel(jsonObject.getString("channel"));

            bannerList.add(banner);
        }

        try {
            List<Banner> updateBannerList = configService.insertOrUpdateBannerList(bannerList, channel);
            return ResponseDo.buildSuccess(updateBannerList);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/deliveryFree/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getDeliveryFreeAmount() {
        long amount = configService.getDeliveryFreeAmount();
        return ResponseDo.buildSuccess(amount);
    }

    @RequestMapping(value = "/deliveryFree/update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updateDeliverFreeAmount(@RequestParam("amount") Long amount,@RequestParam("province") String province) {
        try {
            configService.updateDeliveryFreeAmount(amount,province);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
    
    /**
     * 封装的checkBox数据
     * @return
     */
    @RequestMapping(value = "/getProvinceList/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getProvinceList(){
    	 try {
             return ResponseDo.buildSuccess(configService.provinceList());
         } catch (ApiException e) {
             LOG.warn(e.getMessage());
             return ResponseDo.buildError(e.getMessage());
         }
    }
}
