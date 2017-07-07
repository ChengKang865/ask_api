package com.autoask.service.weiixn;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mysql.WeiXinQrcodeInfo;
import com.autoask.entity.mysql.WeiXinUserInfo;
/**
 * 微信用户信息
 * @author ck
 *
 * @Create 2017年6月14日下午3:23:20
 */
public interface WeiXinUserInfoService {
	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return
	 */
	WeiXinUserInfo selectWeiXinUserInfoById(String openId) throws ApiException;

	/**
	 * list列表信息
	 * 
	 * @param nickName
	 *            名称
	 * @param start
	 * @param limit
	 * @return
	 * @throws ApiException
	 */
	ListSlice<WeiXinUserInfo> selectWeiXinUserInfoList(String nickName, Integer start, Integer limit) throws ApiException;

	/**
	 * 增加
	 * 
	 * @param weiXinQrcodeInfo
	 * @throws ApiException
	 */
	void saveWeiXinUserInfo(WeiXinUserInfo weiXinUserInfo) throws ApiException;

	/**
	 * 根据openId取消订阅
	 * @param openId
	 * @throws ApiException
	 */
	void cancelAttention(String openId) throws ApiException;
	
	/**
	 * 根据openId取消订阅
	 * @param openId
	 * @throws ApiException
	 */
	void subscribe(String openId) throws ApiException;
	
}
