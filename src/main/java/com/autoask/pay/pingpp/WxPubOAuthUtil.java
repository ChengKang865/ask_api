package com.autoask.pay.pingpp;

import com.autoask.common.exception.ApiException;
import com.pingplusplus.model.Charge;
import com.pingplusplus.util.WxpubOAuth;

import java.io.UnsupportedEncodingException;

/**
 * Created by hp on 16-10-15.
 * <p>
 * 微信公共账号，付款签名示例
 * <p>
 * 开发者需要填写 apiKey 、appId 、url 和 openid。
 * <p>
 * apiKey 有 TestKey 和 LiveKey 两种。
 * TestKey 模式下不会产生真实的交易。
 * <p>
 * openid 是发送红包的对象在公共平台下的 openid ,获得 openid 的方法可以参考微信文档：http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html
 */
public class WxPubOAuthUtil {

    /**
     * Ping++ 管理平台对应的应用 ID，app_id 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击你创建的应用->应用首页->应用 ID(App ID)
     */
    private static String appId;

    /**
     * 微信公共号里面获取 openid 时的回跳 URL
     */
    public static String redirectUrl = "YOUR_URL";

    /**
     * 微信公共号的 appId，通常为 `wx` 开头的字符串
     */
    public static String wxAppId = "wx35141fc0d68aabd3";

    /**
     * 微信公共号的 secret
     */
    public static String wxAppSecret = "acf63584579f4afcb9dbebbc1c9c4f1a";

    public static void runDemos(String appId) throws Exception{
        WxPubOAuthUtil.appId = appId;
        System.out.println("------- 获取 openid -------");
        getOpenid();

        System.out.println("------- 如果要是用微信的 jsapi 并且要启用签名, 请参考以下方法 -------");
        jsApiSignatureDemo();
    }

    public static void getOpenid() throws UnsupportedEncodingException, ApiException {
        System.out.println("1. 你需要有一个处理回跳的 URL");
        redirectUrl = "http://用于处理回跳的URL";

        String url = WxpubOAuth.createOauthUrlForCode(wxAppId, redirectUrl, false);
        System.out.println("2. 跳转到该 URL");
        System.out.println(url);

        getOpenidWithCode();
    }

    public static void getOpenidWithCode() throws UnsupportedEncodingException, ApiException {
        System.out.println("3. 微信内置浏览器会带上参数 code 跳转到你传的地址: " + redirectUrl + "?code=os823ndskelcncfyfms");
        // 获取 URL 中的 code 参数
        String code = "os823ndskelcncfyfms";
        String openid = WxpubOAuth.getOpenId(wxAppId, wxAppSecret, code);
        System.out.println("4. 得到 openid 用于创建 charge");
//        Charge chargeExample = PingppChargeUtil.createCharge(1, "", "", "58.240.32.162", "12352466677", "wx_pub");
        openid = "USER_OPENID";

        // 开始调用
        PingppChargeUtil.createChargeWithOpenId(wxAppId, "12345656677", 1, "58.240.32.162", "", "");

    }

    public static void jsApiSignatureDemo() throws Exception {
        // 获得 ticket
        String ticket = WxpubOAuth.getJsapiTicket(wxAppId, wxAppSecret);
        System.out.println("ticket " + ticket);
        // 创建 Charge
        PingppChargeUtil chargeUtil = new PingppChargeUtil();
        Charge charge = PingppChargeUtil.createChargeWithOpenId("USER_OPENID", "12345656677", 1, "58.240.32.162", "", "");
        // 获得签名
        String signature = WxpubOAuth.getSignature(charge.toString(), ticket, redirectUrl);
        System.out.println("------- JSAPI 签名 -------");
        System.out.println(signature);
    }
}

