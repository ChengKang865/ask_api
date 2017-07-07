package com.autoask.service.weiixn;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.weixin.req.TagsInfo;
import com.autoask.entity.common.weiixn.Material;

/**
 * 微信群发控制器
 * @author ck
 *
 * @Create 2017年6月20日下午1:33:50
 */
public interface WeiXinMassService {
	/**
	 * 获取素材列表
	 * @param type 素材类型
	 * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
	 * @param count 返回素材的数量，取值在1到20之间
	 * @return
	 */
	ListSlice<Material> getCreateMaterialList(String type,String offset,String count) throws ApiException;
	
	/**
	 * 获取标签
	 * @return
	 * @throws ApiException
	 */
	ListSlice<TagsInfo> getTagsList()throws ApiException;
	
	/**
	 * 群发消息
	 * @param isToAll  用于设定是否向全部用户发送，值为true或false，选择true该消息群发给所有用户，选择false可根据tag_id发送给指定群组的用户
	 * @param tagId 标签id
	 * @param mediaId  
	 * @param type 消息类型
	 * @param content 文本消息内容
	 */
	Integer Mass(Boolean isToAll,String tagId,String mediaId,String type,String content) throws ApiException;
}
