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
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.weixin.req.TagsInfo;
import com.autoask.entity.common.weiixn.Material;
import com.autoask.service.weiixn.WeiXinMassService;

/**
 * 微信群发控制器
 * @author ck
 *
 * @Create 2017年6月20日下午1:18:59
 */
@Controller
@RequestMapping(value = "weixin/mass/")
public class WeiXinMassCollection {
	
	private static final Logger LOG = LoggerFactory.getLogger(WeiXinMassCollection.class);
	
	@Autowired
	WeiXinMassService weiXinMassService;
	
	/**
	 * 素材list列表
	 * @param type 素材类型
	 * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
	 * @param count 返回素材的数量，取值在1到20之间 
	 * @return
	 */
	@RequestMapping(value = "/materialList/", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDo getMaterialList(@RequestParam(value = "type") String type,
			@RequestParam(value = "count") Integer count, 
			@RequestParam(value = "offset") Integer offset) {
		try {
			ListSlice<Material> staffList = weiXinMassService.getCreateMaterialList(type, String.valueOf(offset), String.valueOf(count));
			return ResponseDo.buildSuccess(staffList);
		} catch (ApiException e) {
			LOG.error(e.getMessage());
			return ResponseDo.buildError(e.getMessage());
		}
	}
	
	/**
	 * 用户标签list列表
	 * @return
	 */
	@RequestMapping(value = "/tagsInfoList/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseDo getTagsInfoList() {
		try {
			ListSlice<TagsInfo> staffList = weiXinMassService.getTagsList();
			return ResponseDo.buildSuccess(staffList);
		} catch (ApiException e) {
			LOG.error(e.getMessage());
			return ResponseDo.buildError(e.getMessage());
		}
	}
	
	/**
	 * 群发消息
	 * @param isToAll  用于设定是否向全部用户发送，值为true或false，选择true该消息群发给所有用户，选择false可根据tag_id发送给指定群组的用户
	 * @param tagId 标签id
	 * @param mediaId 图片或者图文唯一id  
	 * @param type 消息类型
	 * @param content 文本消息内容
	 * @return
	 */
	@RequestMapping(value = "/massNews/", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDo massNews(@RequestParam(value = "isToAll") Boolean isToAll,
			@RequestParam(value = "tagId", required = false) String tagId,
			@RequestParam(value = "mediaId", required = false) String mediaId,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "type") String type) {
		try {
			 Integer code = weiXinMassService.Mass(isToAll, tagId, mediaId, type, content);
			 if(code == 0){
				 return ResponseDo.buildSuccess("群发成功！");
			 }else{
				 return ResponseDo.buildError("群发失败！");
			 }
			
		} catch (ApiException e) {
			LOG.error(e.getMessage());
			return ResponseDo.buildError(e.getMessage());
		}
	}
	
}
