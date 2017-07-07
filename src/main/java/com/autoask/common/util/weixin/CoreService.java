package com.autoask.common.util.weixin;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autoask.common.util.weixin.resp.TextMessage;

/**
 * 微信业务实现类
 * @author ck
 *
 * @Create 2017年6月1日下午4:21:10
 */
public class CoreService {
	
	private static final Logger LOG = LoggerFactory.getLogger(CoreService.class);
	/**
	 * 处理微信发来的请求
	 * @param request
	 * @return xml
	 * 代码待优化：所需数据可以从前端获取，写一些相关页面
	 */
	public static String processRequest(HttpServletRequest request) {
		String respXml = null;
		String respContent = null;
		try {
			// 调用parseXml方法解析请求消息
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			String fromUserName = requestMap.get("FromUserName");
			String toUserName = requestMap.get("ToUserName");
			String msgType = requestMap.get("MsgType");
			String content = requestMap.get("Content");
			LOG.info("微信号fromUserName===================="+fromUserName);
			LOG.info("公众号toUserName===================="+toUserName);
			LOG.info("消息类型msgType===================="+msgType);
			LOG.info("小心内容content===================="+content);
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
			//文本消息回复
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				if ("兑换卡".equals(content)) {
					String title = "兑换卡使用教程";
					String picUrl = "http://mmbiz.qpic.cn/mmbiz_png/CPCDBxvBQCJf7NDCzPA3O43laQDjTG2o0v7lEIZ7elbaksZecRhxYD1MC1ibYUk6tjNe7eribQibM33ibYEyDjGQ7Q/0?wx_fmt=png";
					String description = "兑换卡的详细使用教程（测试）";
					String url = "https://mp.weixin.qq.com/s?__biz=MzI1NzM1NDI1Mw==&tempkey=PvfPkNQycKU%2BnT04bphqqV48E32r65bh0seY%2FftX2giK2Vbx06dBMv9hhwEzM6VhoOUn4KeyLUyZdv7GV%2FswUR31MqpngUVSfbbUDVaS1Tax1vsL6RNHaIHn6ip5TU2JsgmszD2R%2F6juOkgzvBBogQ%3D%3D&chksm=6a19fe665d6e7770c8d4fc44616907cff73d1573e1220405d7aebe97f7291e7cd526419d0331#rd";
					return CommonUtil.imageAndText(fromUserName, toUserName, description, picUrl, title, url);
				} else if ("新一代".equals(content)) {
					String title = "新一代引擎 新一代保护";
					String picUrl = "http://mmbiz.qpic.cn/mmbiz_jpg/CPCDBxvBQCKorYV1AJnA2MVZVtPveiaXxcZjv8OfFlfc8DrdD2eoPsmqaXG76nSJ9ZQpmyfKk7hhEnGEslRZQbg/0?wx_fmt=jpeg";
					String description = "AutoASK · NEWGEN SERIES系列产品";
					String url = "https://mp.weixin.qq.com/s?__biz=MzI1NzM1NDI1Mw==&tempkey=PvfPkNQycKU%2BnT04bphqqV48E32r65bh0seY%2FftX2gh3TPsQWJqCIBesHNgVvI%2FpNEbXg5HN58jpiIrJ7EE8YUdn%2F%2BBF9vm1C7IaAcmZ7Rax1vsL6RNHaIHn6ip5TU2JJQKiCEatBJYGNsi5w6AYLg%3D%3D&chksm=6a19fe6f5d6e7779a2b6799cea78ad593ed05c0291d54d71a0515864f4adf58b6243b909eb11#rd";
					return CommonUtil.imageAndText(fromUserName, toUserName, description, picUrl, title, url);
				} else if ("团队".equals(content)){
					String title = "AutoASK · 团队";
					String picUrl = "http://mmbiz.qpic.cn/mmbiz_png/CPCDBxvBQCJHsBIDYiacM6fVGCbDXp0774ZfibdibyWcibZagoXGuOwmKQ3ASAq2rdTW4s4npJzC76FfeVicqs42nRg/0?wx_fmt=png";
					String description = "我们拥有的不仅仅是专业，更要为用户创造超越期望的价值。";
					String url = "https://mp.weixin.qq.com/s?__biz=MzI1NzM1NDI1Mw==&tempkey=PvfPkNQycKU%2BnT04bphqqV48E32r65bh0seY%2FftX2ggtYW5Yv9S5qqmeLVJZbXDBntoqXNxjVGh3ghzYegAxxGf5qaYVns4%2BxLvFLU12%2Btix1vsL6RNHaIHn6ip5TU2JxfLYcNGqb1ouhJHswXC49A%3D%3D&chksm=6a19fe695d6e777fccac1fc2c6a69e8ee5726e4757781553f57e7847b05a4b2578c7a611eadc#rd";
					return CommonUtil.imageAndText(fromUserName, toUserName, description, picUrl, title, url);
				} else if("经典".equals(content)){
					String title = "平顺一点 好开一点";
					String picUrl = "http://mmbiz.qpic.cn/mmbiz_jpg/CPCDBxvBQCKoFDS3wQcq2cJZ2XYf7vrbFxtQZK2jlPhyz2MW1vZtZuYRuRxn2JnMUCiaCKDTWrqBK3l10C3ibrhA/0?wx_fmt=jpeg";
					String description = "AutoASK · THE CLASSIC SERIES 系列产品";
					String url = "https://mp.weixin.qq.com/s?__biz=MzI1NzM1NDI1Mw==&tempkey=PvfPkNQycKU%2BnT04bphqqV48E32r65bh0seY%2FftX2gj%2FcBN0Jx9X65jvGzfHxLc8v5q5Ot1mGDZws%2BID8nSl7cQtLCAHiyzDbR7d4iM2wFyx1vsL6RNHaIHn6ip5TU2JxiKFl9p6HwzNDK4g4OyoqA%3D%3D&chksm=6a19fe725d6e77642eee16b211cc3b44fae8e71e90e1fd5bdac0caff3500243a4b9045435b0a#rd";
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
					respContent = "终于等到你，AutoASK会为您提供最贴心汽车养护服务。";
				}
				/**
				 * 取消关注
				 */
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					LOG.info("========================取消关注！");
					// 取消订阅后用户不会再收到公众账号发送的消息，因此不需要回复
				}
				/**
				 * 扫描带参数二维码事件
				 */
				else if (eventType.equals(MessageUtil.EVENT_TYPE_SCAN)) {
					LOG.info("========================扫描带参数二维码！");
					
				}
				/**
				 * 上报地理位置事件
				 */
				else if (eventType.equals(MessageUtil.EVENT_TYPE_LOCATION)) {
					//LOG.info("========================地理位子！");
					// TODO 处理上报地理位置事件
				}
				// 自定义菜单消息处理
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					LOG.info("========================菜单点击事件！");
					//String eventKey = requestMap.get("EventKey");
					if (eventKey.equals("V1001_TODAY_MUSIC")) {
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
