package com.autoask.common.util.weixin.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autoask.common.util.weixin.CommonUtil;
import com.autoask.entity.common.weiixn.Token;

/*
 * 线程方式，定时获取accessToken
 * 调用accessToken为：TokenThread.accessToken.getAccessToken()
 *
 */

/**
 * 线程方式，定时获取accessToken
 * 调用accessToken为：TokenThread.accessToken.getAccessToken()
 * @author hb
 *
 */
public class TokenThread implements Runnable {

	private static Logger log = LoggerFactory.getLogger(TokenThread.class);

	public static String appid = "";
	public static String appsecret = "";
	public static Token accessToken = null;

	@Override
	public void run() {
		while (true) {
			try {
				accessToken = CommonUtil.getToken(appid, appsecret);
				if (null != accessToken) {
					log.info("获取access_token成功，有效时长{}秒 token:{}", accessToken.getExpiresIn(),
							accessToken.getAccessToken());
					// 休眠7000秒
					Thread.sleep((accessToken.getExpiresIn() - 200) * 1000);
				} else {
					// 如果access_token为null，60秒后再获取
					Thread.sleep(60 * 1000);
				}
			} catch (InterruptedException e) {
				try {
					Thread.sleep(60 * 1000);
				} catch (InterruptedException e1) {
					log.error("{}", e1);
				}
				log.error("{}", e);
			}
		}
	}
}
