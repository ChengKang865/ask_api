package com.autoask.service.impl.weiixn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autoask.cache.AccessTokenService;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.weixin.WeixinUtil;
import com.autoask.common.util.weixin.req.TagsInfo;
import com.autoask.entity.common.weiixn.AccessToken;
import com.autoask.entity.common.weiixn.Material;
import com.autoask.service.weiixn.WeiXinMassService;

@Service("weiXinMassService")
public class WeiXinMassServiceImpl implements WeiXinMassService {

	@Autowired
	AccessTokenService accessTokenService;

	@Override
	public ListSlice<Material> getCreateMaterialList(String type, String offset, String count) throws ApiException {
		List<Material> list = new ArrayList<Material>();
		Long num = new Long(0);
		try {
			// 获取token
			AccessToken accessToken = accessTokenService.selectAccessToken(WeixinUtil.WEIXIN_APPID,
					WeixinUtil.WEIXIN_APPSECRET);
			if (accessToken != null) {
				System.out.println("token"+accessToken.getToken());
				String outputStr = WeixinUtil.MATERIAL.replace("TYPE", type).replace("OFFSET", offset).replace("COUNT",
						count);
				list = WeixinUtil.createMaterialList(outputStr, accessToken.getToken(), type);
				//获取素材总数
				num = WeixinUtil.createMaterialNum(accessToken.getToken(), type);
			} else {
				throw new ApiException("获取access_token失败");
			}
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
		return new ListSlice(list,num);
	}

	@Override
	public ListSlice<TagsInfo> getTagsList() throws ApiException {
		List<TagsInfo> list = new ArrayList<TagsInfo>();
		Long num = new Long(0);
		try {
			// 获取token
			AccessToken accessToken = accessTokenService.selectAccessToken(WeixinUtil.WEIXIN_APPID,
					WeixinUtil.WEIXIN_APPSECRET);
			if (accessToken != null) {
				list = WeixinUtil.getTags(accessToken.getToken());
				num = new Long(list.size());
			} else {
				throw new ApiException("获取access_token失败");
			}
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
		return new ListSlice(list,num);
	}

	@Override
	public Integer Mass(Boolean isToAll, String tagId, String mediaId, String type, String content) throws ApiException {
		//参数
		String outputStr = null;
		Integer code = 0;
		try {
			// 获取token
			AccessToken accessToken = accessTokenService.selectAccessToken(WeixinUtil.WEIXIN_APPID,
					WeixinUtil.WEIXIN_APPSECRET);
			// 图片
			if (StringUtils.equals(type, WeixinUtil.IMAGE)) {
				//发送给全部
				if (isToAll == true) {
					outputStr = WeixinUtil.MASS_IMAGE.replace("IS_TO_ALL", "true").replace("TAG_ID", "").replace("MEDIA_ID", mediaId);
				} else {
					outputStr = WeixinUtil.MASS_IMAGE.replace("IS_TO_ALL", "false").replace("TAG_ID", tagId).replace("MEDIA_ID", mediaId);
				}
			}
			//text文本
			else if (StringUtils.equals(type, WeixinUtil.TEXT)) {
				if (isToAll == true) {
					outputStr = WeixinUtil.MASS_TEXT.replace("IS_TO_ALL", "true").replace("TAG_ID", "").replace("CONTENT", content);
				} else {
					outputStr = WeixinUtil.MASS_TEXT.replace("IS_TO_ALL", "false").replace("TAG_ID", tagId).replace("CONTENT", content);
				}
			}
			//图文消息
			else if (StringUtils.equals(type, WeixinUtil.MPNEWS)) {
				if (isToAll == true) {
					outputStr = WeixinUtil.MASS_MPNEWS.replace("IS_TO_ALL", "true").replace("TAG_ID", "").replace("MEDIA_ID", content);
				} else {
					outputStr = WeixinUtil.MASS_MPNEWS.replace("IS_TO_ALL", "false").replace("TAG_ID", tagId).replace("MEDIA_ID", content);
				}
			}
			code = WeixinUtil.massSendall(outputStr, accessToken.getToken(), type);
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
		return code;
	}

}
