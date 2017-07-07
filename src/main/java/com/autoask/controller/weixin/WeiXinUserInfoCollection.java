package com.autoask.controller.weixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mysql.WeiXinUserInfo;
import com.autoask.service.weiixn.WeiXinUserInfoService;

/**
 * 微信用户信息控制器
 * @author ck
 *
 * @Create 2017年6月14日下午4:39:17
 */
@Controller
@RequestMapping(value = "weixin/userInfo/")
public class WeiXinUserInfoCollection {
	
	private static final Logger LOG = LoggerFactory.getLogger(WeiXinUserInfoCollection.class);
	
	@Autowired
	WeiXinUserInfoService weiXinUserInfoService;
	
	/**
	 * list列表
	 * @param page
	 * @param count
	 * @param nickName 昵称
	 * @return
	 */
	@RequestMapping(value = "/list/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseDo weiXinUserInfoList(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "count") Integer count, @RequestParam(value = "nickName", required = false) String nickName) {
		try {
			Integer start = CommonUtil.pageToSkipStart(page, count);
			Integer limit = CommonUtil.cleanCount(count);
			ListSlice<WeiXinUserInfo> staffList = weiXinUserInfoService.selectWeiXinUserInfoList(nickName, start, limit);
			return ResponseDo.buildSuccess(staffList);
		} catch (ApiException e) {
			LOG.error(e.getMessage());
			return ResponseDo.buildError(e.getMessage());
		}
	}
}
