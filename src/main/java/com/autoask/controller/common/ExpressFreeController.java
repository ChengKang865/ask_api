package com.autoask.controller.common;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.autoask.cache.ExpressService;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.controller.express.ExpressTemplateController;
import com.autoask.entity.common.Express;
import com.autoask.service.config.ConfigService;
import com.autoask.service.express.ExpressTemplateService;

@Controller
@RequestMapping("expressFree/")
public class ExpressFreeController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExpressTemplateController.class);
	
	@Autowired
	private ExpressTemplateService  expressTemplateService;
	
	@Autowired
	private ExpressService expressService;
	
	@Autowired
	private ConfigService configService;
	
	@RequestMapping(value = "/getExpressPrice/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo getExpressPrice(@RequestBody JSONObject paramJson,
                           HttpServletRequest request, HttpServletResponse response) {
		//价格
        String price = paramJson.getString("price");
        //地区
        String province = paramJson.getString("province");
        //重量
        String totalWeight = paramJson.getString("totalWeight");
        try {
        	BigDecimal freePrice=expressTemplateService.getExpressPrice(price, province, totalWeight);
            return ResponseDo.buildSuccess(freePrice);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
	
	/**
	 * 获取实际和原始快递价格
	 * @param paramJson
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getOriginalPrice/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo getOriginalPrice(@RequestBody JSONObject paramJson,
                           HttpServletRequest request, HttpServletResponse response) {
		//价格
        String price = paramJson.getString("price");
        //地区
        String province = paramJson.getString("province");
        //重量
        String totalWeight = paramJson.getString("totalWeight");
        try {
        	Map<String, BigDecimal> freePrice=expressTemplateService.getOriginalPrice(price, province, totalWeight);
            return ResponseDo.buildSuccess(freePrice);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
	/**
	 * 根据快递类型、单号插叙快递物流信息
	 * @param paramJson
	 * @param request 
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getExpressInfo/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo getExpressInfo(@RequestBody JSONObject paramJson,
                           HttpServletRequest request, HttpServletResponse response) {
		//价格
        String oddNumber = paramJson.getString("oddNumber");
        //快递类型 auto为全部类型
        String com = paramJson.getString("com");
        try {
        	Express express=expressService.queryExpress(oddNumber, com);
            return ResponseDo.buildSuccess(express);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
	
	/**
	 * 获取免不免邮的地区
	 * @return
	 */
	@RequestMapping(value = "/getProvinceName/", method = RequestMethod.GET)
    @ResponseBody
	public ResponseDo getProvinceName(){
		 try {
	            return ResponseDo.buildSuccess(configService.provinceName());
	        } catch (ApiException e) {
	            LOG.warn(e.getMessage());
	            return ResponseDo.buildError(e.getMessage());
	        }
	}
}
