package com.autoask.controller.weixin;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.ImageUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mysql.WeiXinQrcodeInfo;
import com.autoask.service.weiixn.WeiXinQrcodeInfoService;

/**
 * 微信二维码管理
 * 
 * @author ck
 *
 * @Create 2017年6月9日下午7:06:52
 */
@Controller
@RequestMapping(value = "weixin/qrCodeInfo/")
public class WeiXinQrCodeInfo {
	private static final Logger LOG = LoggerFactory.getLogger(WeiXinQrCodeInfo.class);

	@Autowired
	WeiXinQrcodeInfoService weiXinQrcodeInfoService;

	/**
	 * list列表
	 * 
	 * @param page
	 * @param count
	 * @param name
	 *            名称
	 * @return
	 */
	@RequestMapping(value = "/list/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseDo weiXinQrcodeInfoList(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "count") Integer count, @RequestParam(value = "name", required = false) String name) {
		try {
			Integer start = CommonUtil.pageToSkipStart(page, count);
			Integer limit = CommonUtil.cleanCount(count);
			ListSlice<WeiXinQrcodeInfo> staffList = weiXinQrcodeInfoService.getWeiXinQrcodeInfoList(name, start, limit);
			// 订阅总数
			Long subscribeCount = weiXinQrcodeInfoService.countSubscribeCount();
			Map<String, Object> map = new HashMap<String, Object>(1, 2);
			map.put("list", staffList);
			map.put("subscribeCount", subscribeCount);
			return ResponseDo.buildSuccess(map);
		} catch (ApiException e) {
			LOG.error(e.getMessage());
			return ResponseDo.buildError(e.getMessage());
		}
	}

	/**
	 * 增加
	 * 
	 * @param name
	 *            名称
	 * @param type
	 *            二维码类型
	 * @return
	 */
	@RequestMapping(value = "add/", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDo addWeiXinQrcodeInfo(@RequestBody JSONObject paramJson) {
		try {
			String name = paramJson.getString("name");
			String type = paramJson.getString("type");
			Integer expireSeconds = paramJson.getInteger("expireSeconds");
			weiXinQrcodeInfoService.saveWeiXinQrcodeInfo(name, type, expireSeconds);
			return ResponseDo.buildSuccess("添加二维码成功！");
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return ResponseDo.buildError(e.getMessage());
		}
	}
	
	/**
	 * 删除
	 * @param paramJson
	 * @return
	 */
	@RequestMapping(value = "delete/", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDo deleteWeiXinQrcodeInfo(@RequestBody JSONObject paramJson) {
		try {
			String qrCodeInfoId = paramJson.getString("qrCodeInfoId");
			weiXinQrcodeInfoService.deleteWeiXinQrcodeInfoById(qrCodeInfoId);
			return ResponseDo.buildSuccess("删除二维码成功！");
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return ResponseDo.buildError(e.getMessage());
		}
	}
	

	/**
	 * 下载二维码操作
	 * 
	 * @param paramJson
	 * @return
	 */
	@RequestMapping(value = "downloadImg/", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDo downloadImg(@RequestBody JSONObject paramJson) {
		try {
			String imgName = paramJson.getString("imgName");
			String imgUrl = paramJson.getString("imgUrl");
			ImageUtil.downloadPhotos(imgName, imgUrl);
			return ResponseDo.buildSuccess("下载二维码成功！");
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return ResponseDo.buildError(e.getMessage());
		}
	}
}
