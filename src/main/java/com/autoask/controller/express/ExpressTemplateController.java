package com.autoask.controller.express;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.controller.common.BaseController;
import com.autoask.entity.mongo.express.ExpressTemplate;
import com.autoask.service.express.ExpressTemplateService;

/**
 * 快递模板控制器
 * @author ck
 *
 * @Create 2017年4月1日上午10:53:38
 */
@Controller
@RequestMapping(value = "/expressTemplate/")
public class ExpressTemplateController extends BaseController{
	private static final Logger LOG = LoggerFactory.getLogger(ExpressTemplateController.class);
	
	@Autowired
	private ExpressTemplateService  expressTemplateService;
	
	/**
	 * 根据id获取信息
	 * @param id 
	 * @return
	 */
	@RequestMapping(value = "get/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseDo getExpressTemplateById(@RequestParam(value = "id") String id) {
		 try {
			 ExpressTemplate expressTemplate = expressTemplateService.getById(id);
			 return ResponseDo.buildSuccess(expressTemplate);
		 } catch (ApiException e) {
			 LOG.error(e.getMessage());
	         return ResponseDo.buildError(e.getMessage());
		 }
	 }
	
	/**
	 * list列表
	 * @param page
	 * @param count
	 * @param expressType 模板类型
	 * @param name 模板名称
	 * @return
	 */
    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo expressTemplateList(@RequestParam(value = "page") Integer page,
                                @RequestParam(value = "count") Integer count,
                                @RequestParam(value = "expressTypeId", required = false) String expressTypeId,
                                @RequestParam(value = "name", required = false) String name) {
        try {
            Integer start = CommonUtil.pageToSkipStart(page, count);
            Integer limit = CommonUtil.cleanCount(count);
            ListSlice<ExpressTemplate> staffList = expressTemplateService.getExpressTemplateList(name, expressTypeId, start, limit);
            return ResponseDo.buildSuccess(staffList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
    
    /**
     * 增加
     * @param expressTemplate
     * @return
     */
    @RequestMapping(value = "add/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo addExpressTemplate(@RequestBody @Valid ExpressTemplate expressTemplate) {
        try {
        	expressTemplateService.saveExpressTemplate(expressTemplate);
            return ResponseDo.buildSuccess("添加模板成功！");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
    
    /**
     * 更新
     * @param expressTemplate
     * @return
     */
    @RequestMapping(value = "update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updateExpressTemplate(@RequestBody @Valid ExpressTemplate expressTemplate) {

        try {
        	expressTemplateService.updateExpressTemplate(expressTemplate);
            return ResponseDo.buildSuccess("修改模板成功！");
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
    
    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "delete/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo deleteExpressTemplate(@RequestParam(value = "id") String id) {
        try {
        	expressTemplateService.deleteExpressTemplateById(id);
            return ResponseDo.buildSuccess("删除模板成功！");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
    /**
     * 封装的checkBox数据
     * @return
     */
    @RequestMapping(value = "/getProvinceList/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo getProvinceList(String id){
    	 try {
             return ResponseDo.buildSuccess(expressTemplateService.getTemplateProvinceList(id));
         } catch (ApiException e) {
             LOG.warn(e.getMessage());
             return ResponseDo.buildError(e.getMessage());
         }
    }
}
