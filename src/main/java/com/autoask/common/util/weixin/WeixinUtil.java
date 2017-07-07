package com.autoask.common.util.weixin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.weixin.req.TagsInfo;
import com.autoask.entity.common.weiixn.AccessToken;
import com.autoask.entity.common.weiixn.Material;
import com.autoask.entity.common.weiixn.Menu;
import com.autoask.entity.common.weiixn.NewsItem;
import com.autoask.entity.common.weiixn.WeiXinQRCode;
import com.autoask.entity.mysql.WeiXinUserInfo;

/**
 * 微信么公共类
 * 
 * @author ck
 *
 * @Create 2017年6月13日上午9:53:18
 */
public class WeixinUtil {
	private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);

	/**
	 * 获取access_token的接口地址（GET） 限200（次/天） 限时2小时
	 */
	public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	/**
	 * 菜单创建（POST） 限100（次/天）
	 */
	public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	/**
	 * 菜单删除（get）
	 */
	public final static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

	/**
	 * 菜单查询
	 */
	public final static String MENU_CHECK_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	/**
	 * 获取素材列表
	 */
	public final static String MATERIAL_CHECK_URL = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";
	/**
	 * 根据media_id获取素材信息
	 */
	public final static String MATERIAL_URL = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=ACCESS_TOKEN";
	/**
	 * 创建二维码POST
	 */
	public final static String ESTABLISH_QRCODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
	/**
	 * 通过ticket换取二维码
	 */
	public final static String SHOW_QRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
	/**
	 * 根据openid获取微信用户信息 GET请求
	 */
	public final static String GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	/**
	 * 获取公众号的标签人员
	 */
	public final static String GET_TAGS = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=ACCESS_TOKEN";
	
	/**
	 * 群发消息
	 */
	public final static String MASS_SENDALL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";
	
	/**
	 * 获取素材总数
	 */
	public final static String GET_MATERIAL_COUNT = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=ACCESS_TOKEN";

	/**
	 * 微信二次开发借口配置信息中的Token要一致
	 */
	public final static String TOKEN = "autoask";

	/**
	 * 没有图片地址提示
	 */
	public final static String NOT_IMG = "../common/images/no-img.png";
	/**
	 * 素材参数素材类型 图片（image）、视频（video）、语音 （voice）、图文（news）
	 */
	public final static String IMAGE = "image";
	public final static String VIDEO = "video";
	public final static String VOICE = "voice";
	public final static String NEWS = "news";
	//文本格式
	public final static String TEXT = "text";
	//图文格式
	public final static String MPNEWS = "mpnews";
	/**
	 * QR_SCENE 永久 QR_LIMIT_SCENE 临时 QR_LIMIT_STR_SCENE 永久的字符串参数值
	 */
	public final static String QR_SCENE = "QR_SCENE";
	public final static String QR_LIMIT_SCENE = "QR_LIMIT_SCENE";
	public final static String QR_LIMIT_STR_SCENE = "QR_LIMIT_STR_SCENE";
	/**
	 * 其它类型二维码
	 */
	public final static String OTHER_QR_CODE_INFO = "QRSCENE_OTHER";
	
	public final static String TEMPORARY = "临时";
	public final static String PERMANENT = "永久";


	/**
	 * 永久字符串类型二维码参数
	 */
	public static final String PARAMETER_QR_LIMIT_STR_SCENE = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"scene_number\"}}}";
	
	public static final String TEMPORARY_QR_SCENE = "{\"expire_seconds\": EXPIRE_SECONDS, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": SCENE_ID}}}";
	
	/**
	 * 群发消息 mpnews(图文) TEXT(文本) image(图片)
	 */
	public static final String MASS_MPNEWS ="{\"filter\":{\"is_to_all\":IS_TO_ALL,\"tag_id\":TAG_ID},\"mpnews\":{\"media_id\":\"MEDIA_ID\"},\"msgtype\":\"mpnews\",\"send_ignore_reprint\":0}";
	public static final String MASS_TEXT ="{\"filter\":{\"is_to_all\":IS_TO_ALL,\"tag_id\":TAG_ID},\"text\":{\"content\":\"CONTENT\"},\"msgtype\":\"text\"}";
	public static final String MASS_IMAGE ="{\"filter\":{\"is_to_all\":IS_TO_ALL,\"tag_id\":TAG_ID},\"image\":{\"media_id\":\"MEDIA_ID\"},\"msgtype\":\"image\"}";
	
	/**
	 * 素材列表参数
	 */
	public static final String MATERIAL = "{\"type\":\"TYPE\",\"offset\":OFFSET,\"count\":COUNT}";
	/**
	 * 线上
	 */
	public final static String WEIXIN_APPID = "wx35141fc0d68aabd3";

	public final static String WEIXIN_APPSECRET = "acf63584579f4afcb9dbebbc1c9c4f1a";

	public static final String ENCODING_AES_KEY = "26zEs8evgK61gVMXhLxdIDCW1NcyY0O885xBhnsD2Pc";
	
	/**
	 * 测试配置
	 */
	 /*public final static String WEIXIN_APPID = "wx27c506e1b032dfcd";

	 public final static String WEIXIN_APPSECRET = "d774b0db73b4d03f815cb750cdd4d8b7";
	*/
	
	/**
	 * 群发消息
	 * @param outputStr 消息参数
	 * @param accessToken
	 * @param type 消息类型
	 * @throws ApiException 
	 */
	public static Integer massSendall(String outputStr, String accessToken, String type) throws ApiException{
		JSONObject jsonObject = null;
		Integer code = 0;
		try {
			//参数判断
			isMassMaterial(outputStr, accessToken, type);
			String requestUrl = MASS_SENDALL.replace("ACCESS_TOKEN", accessToken);
			jsonObject = httpRequest(requestUrl, "POST", outputStr);
			if(jsonObject != null){
				code = jsonObject.getInt("errcode"); 
			}else{
				throw new ApiException("群发失败！");
			}
		} catch (Exception e) {
			log.error(type+"群发失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"),
					jsonObject.getString("errmsg"));
			throw new ApiException(e.getMessage());
		}
		return code;
	}
	
	/**
	 * 获取公众号标签
	 * @param accessToken
	 * @return
	 */
	public static List<TagsInfo> getTags(String accessToken) {
		JSONObject jsonObject = null;
		List<TagsInfo> list = new ArrayList<TagsInfo>();
			try {
				String requestUrl = GET_TAGS.replace("ACCESS_TOKEN", accessToken);
				jsonObject = httpRequest(requestUrl, "GET", null);
				if (null != jsonObject) {
					JSONArray itemArry = JSONArray.fromObject(jsonObject.get("tags"));
					for (int i = 0; i < itemArry.size(); i++) {
						JSONObject tags = itemArry.getJSONObject(i);
						TagsInfo tagsInfo =new TagsInfo();
						tagsInfo.setId(tags.getInt("id"));
						tagsInfo.setName(tags.getString("name"));
						tagsInfo.setCount(tags.getInt("count"));
						list.add(tagsInfo);
					}
					//添加全部类型
					TagsInfo ti =new TagsInfo();
					ti.setId(0);
					ti.setName("全部");
					ti.setCount(0);
					list.add(0, ti);
				}
			} catch (JSONException e) {
				log.error("获取weiixn用户失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"),
						jsonObject.getString("errmsg"));
			}
		
		return list;
	}
	
	
	/**
	 * 获取微信用户信息
	 * @param openid
	 * @param accessToken
	 * @return
	 */
	public static WeiXinUserInfo getUserInfo(String openid, String accessToken) {
		WeiXinUserInfo userInfo = null;
		JSONObject jsonObject = null;
			try {
				String requestUrl = GET_USER_INFO.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);
				jsonObject = httpRequest(requestUrl, "GET", null);
				if (null != jsonObject) {
				userInfo = new WeiXinUserInfo();
				userInfo.setSubscribe(new Long((long)jsonObject.getInt("subscribe")));
				userInfo.setOpenId(jsonObject.getString("openid"));
				userInfo.setNickName(jsonObject.getString("nickname"));
				userInfo.setSex(new Long((long)jsonObject.getInt("sex")));
				userInfo.setLanguage(jsonObject.getString("language"));
				userInfo.setCity(jsonObject.getString("city"));
				userInfo.setProvince(jsonObject.getString("province"));
				userInfo.setCountry(jsonObject.getString("country"));
				userInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
				}
			} catch (JSONException e) {
				userInfo = null;
				log.error("获取weiixn用户失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"),
						jsonObject.getString("errmsg"));
		}
		return userInfo;
	}
	/**
	 * 创建二维码 JSON格式POST请求
	 * 
	 * @param outputStr参数
	 * @param accessToken
	 * @param type
	 *            创建二维码类型
	 * @return
	 * @throws ApiException
	 */
	public static WeiXinQRCode establishQrcode(String outputStr, String accessToken, String type) throws ApiException {
		WeiXinQRCode weiXinQRCode = new WeiXinQRCode();
		JSONObject jsonObject = null;
		try {
			// 参数判断
			if (!StringUtils.isNotEmpty(outputStr)) {
				throw new ApiException("参数不能为空！");
			}
			if (!StringUtils.isNotEmpty(accessToken)) {
				throw new ApiException("accessToken不能为空！");
			}
			if (!StringUtils.isNotEmpty(type)) {
				throw new ApiException("参数类型不能为空！");
			}
			String requestUrl = ESTABLISH_QRCODE.replace("ACCESS_TOKEN", accessToken);
			jsonObject = httpRequest(requestUrl, "POST", outputStr);
			if (jsonObject != null) {
				weiXinQRCode.setCreateTime(DateUtil.getTime());
				// 临时二维码
				if (QR_SCENE.equals(type)) {
					// 有效时间
					weiXinQRCode.setExpireSeconds(jsonObject.getLong("expire_seconds"));
				} 
					weiXinQRCode.setTicket(jsonObject.getString("ticket"));
					weiXinQRCode.setUrl(jsonObject.getString("url"));
					weiXinQRCode.setType(type);
			} else {
				throw new ApiException("创建二维码失败！");
			}
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
		return weiXinQRCode;
	}

	/**
	 * 创建菜单
	 *
	 * @param menu
	 *            菜单实例
	 * @param accessToken
	 *            有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */

	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;
		String url = MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.fromObject(menu).toString();
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}
		return result;
	}

	/**
	 * 获取素材列表
	 * 
	 * @param outputStr
	 *            参数
	 * @param accessToken
	 * @param type
	 *            素材的类型
	 * @return
	 * @throws ApiException
	 */
	public static List<Material> createMaterialList(String outputStr, String accessToken, String type)
			throws ApiException {
		List<Material> list = new ArrayList<Material>();
		isMaterial(outputStr, accessToken, type);
		try {
			String requestUrl = MATERIAL_CHECK_URL.replace("ACCESS_TOKEN", accessToken);
			JSONObject jsonObject = httpRequest(requestUrl, "POST", outputStr);
			JSONArray itemArry = jsonObject.getJSONArray("item");
			if (itemArry != null && itemArry.size() != 0) {
				for (int i = 0; i < itemArry.size(); i++) {
					Material material = new Material();
					JSONObject item = itemArry.getJSONObject(i);
					material.setType(type);
					// 素材id
					material.setMediaId(item.getString("media_id"));
					// 最后更新时间
					material.setUpdateTime(item.getLong("update_time"));
					// 图文类型素材
					if (type.equals(NEWS)) {
						String content = item.getString("content");
						JSONObject obj = JSONObject.fromObject(content);
						JSONArray newsItemArry = obj.getJSONArray("news_item");
						List<NewsItem> newsList = new ArrayList<NewsItem>();
						if (newsItemArry != null && newsItemArry.size() != 0) {
							for (int j = 0; j < newsItemArry.size(); j++) {
								NewsItem newsItem = new NewsItem();
								JSONObject news = newsItemArry.getJSONObject(j);
								// 图文消息的封面摘要
								newsItem.setDigest(news.getString("digest"));
								// 封面图片id
								newsItem.setThumbMediaId(news.getString("thumb_media_id"));
								// 封面图片路径
								newsItem.setThumbUrl(news.getString("thumb_url"));
								// 标题信息
								newsItem.setTitle(news.getString("title"));
								// 图文内容链接地址
								newsItem.setUrl(news.getString("url"));
								// 作者
								newsItem.setAuthor(news.getString("author"));
								// 是否显示封面，0为false，即不显示，1为true，即显示
								newsItem.setShowCoverPic(news.getInt("show_cover_pic"));
								newsList.add(newsItem);
							}
							material.setList(newsList);
						}
					} else {
						material.setName(item.getString("name"));
						material.setUrl(item.getString("url"));
					}
					list.add(material);
				}
			}
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
		return list;
	}
	
	/**
	 * 获取素材总数
	 * @param accessToken
	 * @param type 素材类型
	 * @return
	 * @throws ApiException
	 */
	public static Long createMaterialNum(String accessToken, String type) throws ApiException{
		Long count = new Long(0);
		try {
			String requestUrl = GET_MATERIAL_COUNT.replace("ACCESS_TOKEN", accessToken);
			JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
			if(jsonObject != null){
				if(StringUtils.equals(type, IMAGE)){
					count = jsonObject.getLong("image_count");
				}
				if(StringUtils.equals(type, NEWS)){
					count = jsonObject.getLong("news_count");
				}
				if(StringUtils.equals(type, VIDEO)){
					count = jsonObject.getLong("video_count");
				}
				if(StringUtils.equals(type, VOICE)){
					count = jsonObject.getLong("voice_count");
				}
			}
		} catch (Exception e) {
			throw new ApiException(e.getMessage()); 
		}
		return count;
	}

	/**
	 * 根据media_id获取素材信息
	 * 
	 * @param outputStr参数
	 *            media_id
	 * @param accessToken
	 * @param type
	 *            查询的素材类型
	 * @return
	 * @throws ApiException
	 */
	public static JSONObject getMaterial(String outputStr, String accessToken, String type) throws ApiException {
		JSONObject jsonObject = null;
		try {
			// 参数判断
			isMaterial(outputStr, accessToken, type);
			String requestUrl = MATERIAL_URL.replace("ACCESS_TOKEN", accessToken);
			jsonObject = httpRequest(requestUrl, "POST", outputStr);
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
		return jsonObject;
	}

	/**
	 * 获取access_token
	 *
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;
		String requestUrl = ACCESS_TOKEN_URL.replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"),
						jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}

	/**
	 * 描述: 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();

			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			String json = buffer.toString();
			jsonObject = JSONObject.fromObject(json);
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}

	/**
	 * 判断素材类型是否正确
	 * 
	 * @param outputStr
	 * @param accessToken
	 * @param type
	 * @throws ApiException
	 */
	public static void isMaterial(String outputStr, String accessToken, String type) throws ApiException {
		if (!StringUtils.isNotEmpty(outputStr)) {
			throw new ApiException("参数不能为空！");
		}
		if (!StringUtils.isNotEmpty(accessToken)) {
			throw new ApiException("accessToken不能为空！");
		}
		if (!StringUtils.isNotEmpty(type)) {
			throw new ApiException("参数类型不能为空！");
		}
		if (!type.equals(IMAGE) && !type.equals(VIDEO) && !type.equals(VOICE) && !type.equals(NEWS)) {
			throw new ApiException("参数类型错误！");
		}
	}
	/**
	 * 判断群发消息参数、类型等是否正确
	 * @param outputStr
	 * @param accessToken
	 * @param type
	 * @throws ApiException
	 */
	public static void isMassMaterial(String outputStr, String accessToken, String type) throws ApiException {
			if (!StringUtils.isNotEmpty(outputStr)) {
				throw new ApiException("参数不能为空！");
			}
			if (!StringUtils.isNotEmpty(accessToken)) {
				throw new ApiException("accessToken不能为空！");
			}
			if (!StringUtils.isNotEmpty(type)) {
				throw new ApiException("参数类型不能为空！");
			}
			if (!type.equals(IMAGE) && !type.equals(TEXT) && !type.equals(MPNEWS)) {
				throw new ApiException("参数类型错误！");
			}	
	}

}
