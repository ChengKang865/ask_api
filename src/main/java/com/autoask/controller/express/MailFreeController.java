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
import com.autoask.controller.common.BaseController;
import com.autoask.entity.mongo.express.MailFree;
import com.autoask.service.express.MailFreeService;

/**
 * 免邮设置控制器
 * @author ck
 *
 * @Create 2017年4月8日上午10:52:52
 */
@Controller
@RequestMapping(value = "/mailFree/")
public class MailFreeController extends BaseController{
private static final Logger LOG = LoggerFactory.getLogger(MailFreeController.class);
	
	@Autowired
	private MailFreeService  mailFreeService;
	
	/**
	 * 根据地区获取免邮价格
	 * @param province
	 * @return
	 */
	@RequestMapping(value = "getByProvince/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseDo getByProvince(@RequestParam(value = "id") String province) {
		 try {
			 String price = mailFreeService.getByProvince(province);
			 return ResponseDo.buildSuccess(price);
		 } catch (ApiException e) {
			 LOG.error(e.getMessage());
	         return ResponseDo.buildError(e.getMessage());
		 }
	 }
	/**
     * 增加
     * @param mailfree
     * @return
     */
    @RequestMapping(value = "add/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo addMailFree(@RequestBody @Valid MailFree mailFree) {
        try {
        	mailFreeService.saveMailFree(mailFree);
            return ResponseDo.buildSuccess("添加免邮信息成功！");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
    
    /**
     * 更新
     * @param mailfree
     * @return
     */
    @RequestMapping(value = "update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updateMailFree(@RequestBody @Valid MailFree mailfree) {

        try {
        	mailFreeService.updateMailFree(mailfree);
            return ResponseDo.buildSuccess("修改免邮信息成功！");
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
