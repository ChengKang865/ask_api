package com.autoask.cache;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.common.weiixn.AccessToken;
/**
 * 查询AccessToken 缓存里面有取缓存里
 * @author ck
 *
 * @Create 2017年6月9日上午10:27:13
 */
public interface AccessTokenService {
	/**
	 * 根据appid和appsecret获取AccessToken
	 * @param appid
	 * @param appsecret
	 * @return
	 */
	AccessToken selectAccessToken(String appid, String appsecret)throws ApiException;
}
