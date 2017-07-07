package com.autoask.service.impl.weiixn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autoask.cache.AccessTokenService;
import com.autoask.common.util.weixin.CommonUtil;
import com.autoask.common.util.weixin.MessageUtil;
import com.autoask.common.util.weixin.WeixinUtil;
import com.autoask.common.util.weixin.resp.Article;
import com.autoask.common.util.weixin.resp.NewsMessage;
import com.autoask.common.util.weixin.resp.TextMessage;
import com.autoask.entity.common.weiixn.AccessToken;
import com.autoask.entity.mysql.WeiXinQrcodeInfo;
import com.autoask.entity.mysql.WeiXinUserInfo;
import com.autoask.service.weiixn.CoreService;
import com.autoask.service.weiixn.WeiXinQrcodeInfoService;
import com.autoask.service.weiixn.WeiXinUserInfoService;
/**
 * 微信核心业务
 * @author ck
 *
 * @Create 2017年6月14日上午9:34:27
 */
@Service("coreService")
public class CoreServiceImpl implements CoreService{
	private static final Logger LOG = LoggerFactory.getLogger(CoreServiceImpl.class);
	
	@Autowired
	WeiXinQrcodeInfoService weiXinQrcodeInfoService;
	@Autowired
	WeiXinUserInfoService weiXinUserInfoService;
	@Autowired
	AccessTokenService accessTokenService;

	@Override
	public String processRequest(Map<String, String> requestMap) {
		String respXml = null;
		String respContent = null;
		try {
			// 调用parseXml方法解析请求消息
			String fromUserName = requestMap.get("FromUserName");
			String toUserName = requestMap.get("ToUserName");
			String msgType = requestMap.get("MsgType");
			String content = requestMap.get("Content");
			String createTime = requestMap.get("CreateTime");
			LOG.info("微信号fromUserName===================="+fromUserName);
			LOG.info("公众号toUserName===================="+toUserName);
			LOG.info("消息类型msgType===================="+msgType);
			LOG.info("小心内容content===================="+content);
			LOG.info("时间===================="+createTime);
			//编号
			String eventKey = requestMap.get("EventKey");
			LOG.info("事件eventKey========================"+eventKey);
			/**
			 * 回复文本消息
			 */
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			//查询是否关注过公众号
			WeiXinUserInfo weiXinUserInfo = weiXinUserInfoService.selectWeiXinUserInfoById(fromUserName);
			//文本消息回复
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				if ("兑换卡".equals(content)) {
					String title = "兑换卡使用教程";
					String picUrl = "http://mmbiz.qpic.cn/mmbiz_png/CPCDBxvBQCJf7NDCzPA3O43laQDjTG2o0v7lEIZ7elbaksZecRhxYD1MC1ibYUk6tjNe7eribQibM33ibYEyDjGQ7Q/0?wx_fmt=png";
					String description = "兑换卡的详细使用教程（测试）";
					String url = "http://mp.weixin.qq.com/s?__biz=MzI1NzM1NDI1Mw==&mid=100000004&idx=1&sn=323987fd038eb2ea0d08f8b37421edf7&chksm=6a19fe665d6e7770c8d4fc44616907cff73d1573e1220405d7aebe97f7291e7cd526419d0331#rd";
					return CommonUtil.imageAndText(fromUserName, toUserName, description, picUrl, title, url);
				} else if ("新一代".equals(content)) {
					String title = "新一代引擎，新一代保护";
					String picUrl = "http://mmbiz.qpic.cn/mmbiz_png/CPCDBxvBQCLCw5nnnDLr26wCTgynd9XH33wH6Mj4OBQAZricD1B7bAfMUVpy7b21bl1Q8TPs98V8ZoefJicg4buA/0?wx_fmt=png";
					String description = "NEWGEN SERIES系列";
					String url = "http://mp.weixin.qq.com/s?__biz=MzI1NzM1NDI1Mw==&mid=100000048&idx=1&sn=755e850bb256007afa56b2f8faab6932&chksm=6a19fe525d6e77445c09812193259563f5e30935141f11bf2f51c9e208e15d47523351c772b2#rd";
					return CommonUtil.imageAndText(fromUserName, toUserName, description, picUrl, title, url);
				} else if ("团队".equals(content)){
					String title = "AutoASK · 团队";
					String picUrl = "http://mmbiz.qpic.cn/mmbiz_jpg/CPCDBxvBQCJf7NDCzPA3O43laQDjTG2obNFp7UA1ZrKSP4iaibhUDYdkM2uDGbyUicEfvytkQ4dZG9icK4PqApdiaaw/0?wx_fmt=jpeg";
					String description = "我们拥有的不仅仅是专业，更要为用户创造超越期望的价值。";
					String url = "http://mp.weixin.qq.com/s?__biz=MzI1NzM1NDI1Mw==&mid=100000002&idx=1&sn=32855db81f4dd267e59afe6c0a454304&chksm=6a19fe605d6e7776348a8bf5a63798cb917b0f4535b1cc40fa498fef36adbe24a6b07e146131#rd";
					return CommonUtil.imageAndText(fromUserName, toUserName, description, picUrl, title, url);
				} else if("经典".equals(content)){
					String title = "平顺一点 好开一点";
					String picUrl = "http://mmbiz.qpic.cn/mmbiz_jpg/CPCDBxvBQCKoFDS3wQcq2cJZ2XYf7vrbFxtQZK2jlPhyz2MW1vZtZuYRuRxn2JnMUCiaCKDTWrqBK3l10C3ibrhA/0?wx_fmt=jpeg";
					String description = "AutoASK · THE CLASSIC SERIES 系列产品";
					String url = "https://mp.weixin.qq.com/s?__biz=MzI1NzM1NDI1Mw==&mid=100000016&idx=1&sn=25fb94e8d469caa057b2b29c82eac745&chksm=6a19fe725d6e77642eee16b211cc3b44fae8e71e90e1fd5bdac0caff3500243a4b9045435b0a#rd";
					return CommonUtil.imageAndText(fromUserName, toUserName, description, picUrl, title, url);
				} else {
					respContent = "感谢您关注AutoASK官方微信，我们为为创新技术而生，为亿万车主而行，我们拥有一颗勇于探索与挑战的心，探索带来创新，挑战带来革命。\n------------------------------\n自助服务\n1.回复“兑换卡”了解兑换卡使用流程\n2.回复“新一代”了解新一代系列产品\n3.回复“团队”了解AutoASK · 团队4.回复“经典”了解经典系列产品";
				}
			}
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				LOG.info("========================您发送的是图片消息！");
			}
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				LOG.info("========================您发送的是语言消息！");
			}
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {
				LOG.info("您发送的是图片消息！");
			}
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_SHORTVIDEO)) {
				LOG.info("========================您发送的是小视频消息！");
			}
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				LOG.info("========================您发送的是地理位置消息！");
			}
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				LOG.info("========================您发送的是链接消息！");
			}
			/**
			 * 事件推送
			 */
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				String eventType = requestMap.get("Event");
				/**
				 * 关注
				 */
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					LOG.info("========================关注！");
					//respContent = "终于等到你，AutoASK会为您提供最贴心汽车养护服务。";
					respContent = "感谢您关注AutoASK官方微信，我们为为创新技术而生，为亿万车主而行，我们拥有一颗勇于探索与挑战的心，探索带来创新，挑战带来革命。";
					
					if(weiXinUserInfo == null){
						// 获取token信息
						AccessToken accessToken = accessTokenService.selectAccessToken(WeixinUtil.WEIXIN_APPID,
								WeixinUtil.WEIXIN_APPSECRET);
						//根据openid查询微信用户信息
						WeiXinUserInfo uerInfo = WeixinUtil.getUserInfo(fromUserName, accessToken.getToken());
						WeiXinQrcodeInfo qrCode = new WeiXinQrcodeInfo();
						if(!StringUtils.isEmpty(eventKey)){
							 qrCode = weiXinQrcodeInfoService.getWeiXinQrcodeInfoById(eventKey);
						}
						if(qrCode != null){
							uerInfo.setQrCodeInfoId(eventKey);
							weiXinUserInfoService.saveWeiXinUserInfo(uerInfo);
							//根据二维码编号确定扫描入口 编号为空或者没有查到入口二维码就判定给其它二维码入口 其它(未知)
							if(!StringUtils.isEmpty(eventKey)){
								weiXinQrcodeInfoService.updateSubscribeCountAddOne(eventKey);
							}else{
								//没有编号
								weiXinQrcodeInfoService.updateSubscribeCountAddOne(WeixinUtil.OTHER_QR_CODE_INFO);
							}
						}else{
							//未知的二维码
							weiXinUserInfoService.saveWeiXinUserInfo(uerInfo);
							weiXinQrcodeInfoService.updateSubscribeCountAddOne(WeixinUtil.OTHER_QR_CODE_INFO);
						}
					}else{
						//重新关注
						weiXinUserInfoService.subscribe(fromUserName);
					}
				}
				/**
				 * 取消关注
				 */
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					LOG.info("========================取消关注！");
					// 取消订阅后用户不会再收到公众账号发送的消息，因此不需要回复
					if(weiXinUserInfo != null){
						weiXinUserInfoService.cancelAttention(fromUserName);
					}
				}
				/**
				 * 扫描带参数二维码事件
				 */
				else if (eventType.equals(MessageUtil.EVENT_TYPE_SCAN)) {
					LOG.info("========================扫描带参数二维码！");
					respContent = "终于等到你，AutoASK会为您提供最贴心汽车养护服务。";
					
				}
				/**
				 * 上报地理位置事件
				 */
				else if (eventType.equals(MessageUtil.EVENT_TYPE_LOCATION)) {
					LOG.info("========================地理位子！");
				}
				// 自定义菜单消息处理
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					//AutoASK 图文消息
					if (eventKey.equals("AUTOASK_CLICK")) {
						NewsMessage newsMessage = new NewsMessage();
						newsMessage.setToUserName(fromUserName);
						newsMessage.setFromUserName(toUserName);
						newsMessage.setCreateTime(new Date().getTime());
						newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						Article article = new Article();
						article.setDescription("AutoASK简介");
						article.setPicUrl("http://mmbiz.qpic.cn/mmbiz_jpg/CPCDBxvBQCJf7NDCzPA3O43laQDjTG2obNFp7UA1ZrKSP4iaibhUDYdkM2uDGbyUicEfvytkQ4dZG9icK4PqApdiaaw/0?wx_fmt=jpeg");
						article.setTitle("为亿万车主而");
						article.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1NzM1NDI1Mw==&mid=100000002&idx=1&sn=32855db81f4dd267e59afe6c0a454304&chksm=6a19fe605d6e7776348a8bf5a63798cb917b0f4535b1cc40fa498fef36adbe24a6b07e146131#rd");
						List<Article> list = new ArrayList<Article>();
						list.add(article);
						newsMessage.setArticleCount(list.size());
						newsMessage.setArticles(list);
						return MessageUtil.messageToXml(newsMessage);
					}
					if(eventKey.equals("NEW_GENERATION_CLICK")){
						String title = "新一代引擎，新一代保护";
						String picUrl = "http://mmbiz.qpic.cn/mmbiz_png/CPCDBxvBQCLCw5nnnDLr26wCTgynd9XH33wH6Mj4OBQAZricD1B7bAfMUVpy7b21bl1Q8TPs98V8ZoefJicg4buA/0?wx_fmt=png";
						String description = "NEWGEN SERIES系列";
						String url = "http://mp.weixin.qq.com/s?__biz=MzI1NzM1NDI1Mw==&mid=100000048&idx=1&sn=755e850bb256007afa56b2f8faab6932&chksm=6a19fe525d6e77445c09812193259563f5e30935141f11bf2f51c9e208e15d47523351c772b2#rd";
						return CommonUtil.imageAndText(fromUserName, toUserName, description, picUrl, title, url);
					}
					if(eventKey.equals("CLASSIC_SERIES_CLICK")){
						String title = "平顺一点 好开一点";
						String picUrl = "http://mmbiz.qpic.cn/mmbiz_jpg/CPCDBxvBQCKoFDS3wQcq2cJZ2XYf7vrbFxtQZK2jlPhyz2MW1vZtZuYRuRxn2JnMUCiaCKDTWrqBK3l10C3ibrhA/0?wx_fmt=jpeg";
						String description = "AutoASK · THE CLASSIC SERIES 系列产品";
						String url = "https://mp.weixin.qq.com/s?__biz=MzI1NzM1NDI1Mw==&mid=100000016&idx=1&sn=25fb94e8d469caa057b2b29c82eac745&chksm=6a19fe725d6e77642eee16b211cc3b44fae8e71e90e1fd5bdac0caff3500243a4b9045435b0a#rd";
						return CommonUtil.imageAndText(fromUserName, toUserName, description, picUrl, title, url);
					} 
					//联系客服
					else if(eventKey.equals("CONTACT_CUSTOMER_SERVICE")){
/*						KKTransInfo  kfAccount = new  KKTransInfo();
						kfAccount.setKfAccount("chengkang19931107");
						MessageTransInfo message = new MessageTransInfo();  
						message.setToUserName(fromUserName);  
						message.setFromUserName(toUserName);  
						message.setCreateTime(new Date().getTime());  
						message.setMsgType("transfer_customer_service"); 
						message.setTransInfo(kfAccount);
						System.out.println(MessageUtil.infoMessageToXml(message));
						return MessageUtil.infoMessageToXml(message);*/
						respContent = "请在左下角键盘后发送您想要说的话。";
					}
				}
			}
			textMessage.setContent(respContent);
			respXml = MessageUtil.messageToXml(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respXml;
	}
}
