package com.autoask.cache.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autoask.cache.AccessTokenService;
import com.autoask.cache.CacheService;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.weixin.WeixinUtil;
import com.autoask.entity.common.weiixn.AccessToken;
/**
 * token缓存
 * @author ck
 *
 * @Create 2017年6月9日下午5:47:04
 */
@Service("accessTokenService")
public class AccessTokenServiceImpl implements AccessTokenService {

	private static Logger LOG = LoggerFactory.getLogger(AccessTokenServiceImpl.class);
	/**
	 * 缓存信息默认的失效时间为7200秒
	 */
	private static final int ACCESS_TOKEN_TIME = 7200;

	@Autowired
	private CacheService cacheService;

	@Override
	public AccessToken selectAccessToken(String appid, String appsecret) throws ApiException {
		LOG.debug("select AccessToken");
		if(StringUtils.isEmpty(appid)){
			throw new ApiException("appid不能为空");
		}
		if(StringUtils.isEmpty(appsecret)){
			throw new ApiException("appsecret不能为空");
		}
		AccessToken token = new AccessToken();
		// 把appid作为缓存key来进行查询等操作
		AccessToken accessToken = cacheService.get(appid, AccessToken.class);
		// 判断缓存中是否有tkoen 没有就要调用微信接口并且存入缓存中
		if (accessToken != null) {
			if (!StringUtils.isEmpty(accessToken.getToken())) {
				token.setToken(accessToken.getToken());
				token.setExpiresIn(accessToken.getExpiresIn());
			} else {
				AccessToken weiXinToken = WeixinUtil.getAccessToken(appid, appsecret);
				if (weiXinToken != null) {
					token.setToken(weiXinToken.getToken());
					token.setExpiresIn(weiXinToken.getExpiresIn());
					cacheService.set(appid, weiXinToken, ACCESS_TOKEN_TIME);
				} else {
					throw new ApiException("获取token失败！");
				}
			}
		} else {
			AccessToken weiXinToken = WeixinUtil.getAccessToken(appid, appsecret);
			if (weiXinToken != null) {
				token.setToken(weiXinToken.getToken());
				token.setExpiresIn(weiXinToken.getExpiresIn());
				cacheService.set(appid, weiXinToken, ACCESS_TOKEN_TIME);
			} else {
				throw new ApiException("获取token失败！");
			}
		}
		return token;
	}

}
