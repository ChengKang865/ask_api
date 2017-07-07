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
import com.autoask.entity.mongo.express.ExpressType;
import com.autoask.service.express.ExpressTypeService;

/**
 * 快递类型控制器
 * @author ck
 *
 * @Create 2017年4月1日下午3:18:30
 */
@Controller
@RequestMapping(value = "/expressType/")
public class ExpressTypeController extends BaseController{
	private static final Logger LOG = LoggerFactory.getLogger(ExpressTypeController.class);
	
	@Autowired
	private ExpressTypeService  expressTypeService;
	
	/**
	 * 根据id获取信息
	 * @param id 
	 * @return
	 */
	@RequestMapping(value = "get/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseDo getExpressTypeById(@RequestParam(value = "id") String id) {
		 try {
			 ExpressType expressType = expressTypeService.getById(id);
			 return ResponseDo.buildSuccess(expressType);
		 } catch (ApiException e) {
			 LOG.error(e.getMessage());
	         return ResponseDo.buildError(e.getMessage());
		 }
	 }
	
	/**
	 * list列表
	 * @param page
	 * @param count
	 * @param cnName 中文名称
	 * @param ukName 英文名称
	 * @return
	 */
    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo expressTypeList(@RequestParam(value = "page") Integer page,
                                @RequestParam(value = "count") Integer count,
                                @RequestParam(value = "cnName", required = false) String cnName,
                                @RequestParam(value = "ukName", required = false) String ukName) {
        try {
            Integer start = CommonUtil.pageToSkipStart(page, count);
            Integer limit = CommonUtil.cleanCount(count);
            ListSlice<ExpressType> staffList = expressTypeService.getExpressTypeList(cnName, ukName, start, limit);
            return ResponseDo.buildSuccess(staffList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
    
	/**
	 * 没有分页的快递类型List列表
	 * @return
	 */
    @RequestMapping(value = "notPageList/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo notPageExpressTypeList() {
        try {
            ListSlice<ExpressType> staffList = expressTypeService.getNotPageExpressTypeList();
            return ResponseDo.buildSuccess(staffList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
    
    /**
     * 增加
     * @param expressType
     * @return
     */
    @RequestMapping(value = "add/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo addExpressType(@RequestBody @Valid ExpressType expressType) {
        try {
        	expressTypeService.saveExpressType(expressType);
            return ResponseDo.buildSuccess("添加快递类型成功！");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
    
    /**
     * 更新
     * @param expressType
     * @return
     */
    @RequestMapping(value = "update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updateExpressType(@RequestBody @Valid ExpressType expressType) {

        try {
        	expressTypeService.updateExpressType(expressType);
            return ResponseDo.buildSuccess("修改快递类型成功！");
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
    public ResponseDo deleteExpressType(@RequestParam(value = "id") String id) {
        try {
        	expressTypeService.deleteExpressTypeById(id);
            return ResponseDo.buildSuccess("删除快递类型成功！");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
	
}
