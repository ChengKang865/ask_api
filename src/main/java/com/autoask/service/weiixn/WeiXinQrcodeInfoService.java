package com.autoask.service.weiixn;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mysql.WeiXinQrcodeInfo;
/**
 * 微信二维码关注来源接口
 * @author ck
 *
 * @Create 2017年6月9日上午10:06:36
 */
public interface WeiXinQrcodeInfoService {
	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return
	 */
	WeiXinQrcodeInfo getWeiXinQrcodeInfoById(String id) throws ApiException;

	/**
	 * list列表信息
	 * 
	 * @param name
	 *            中文名称
	 * @param start
	 * @param limit
	 * @return
	 * @throws ApiException
	 */
	ListSlice<WeiXinQrcodeInfo> getWeiXinQrcodeInfoList(String name, Integer start, Integer limit) throws ApiException;

	/**
	 * 增加
	 * 
	 * @param WeiXinQrcodeInfo
	 * @throws ApiException
	 */
	void saveWeiXinQrcodeInfo(String name, String type,Integer expireSecondsDay) throws ApiException;

	/**
	 * 订阅增加一次
	 * 
	 * @param weiXinQrcodeInfo
	 * @throws ApiException
	 */
	void updateSubscribeCountAddOne(String qrCodeInfoId) throws ApiException;

	/**
	 * 删除
	 * 
	 * @param id
	 *            根据id删除
	 * @throws ApiException
	 */
	void deleteWeiXinQrcodeInfoById(String id) throws ApiException;
	
	/**
	 * 订阅总数
	 * @return
	 */
	Long countSubscribeCount() throws ApiException;
}
