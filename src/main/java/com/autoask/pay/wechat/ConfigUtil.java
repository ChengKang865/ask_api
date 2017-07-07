package com.autoask.pay.wechat;

/**
 * Created by hp on 16-9-7.
 */
public class ConfigUtil {
    /**
     * 服务号相关信息
     */
    public final static String APPID = "wx35141fc0d68aabd3";    // 公众账号ID
    public final static String MCH_ID = "1366212602";   // 商户号
    public final static String DEVICE_INFO = "WEB"; // 设备号: 扫码和公众号默认是WEB
    public final static String DEALER_KEY = "wO5b6HdyxQZyU2tGqUNM1n9E2dJhAnfh";
    public final static String SIGN_TYPE = "MD5";
    public final static String APP_SECRET = "acf63584579f4afcb9dbebbc1c9c4f1a";

    // 微信支付统一接口的回调
    // TODO: 设计好后,添加进来
    public final static String NOTIFY_URL = "http://58.240.32.162:8880/wxpay/notify/";
    // 微信支付成功支付后跳转的地址 web端使用
    // TODO: 需要添加
    public final static String SUCCESS_URL = "";
    // oauth2授权时回调action
    // TODO: 暂不确定
    public final static String REDIRECT_URI = "";

    /**
     * 微信支付接口地址
     */
    // 微信支付统一下单接口(POST)
    public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    // 微信退款接口(POST)
    public final static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    // 订单查询接口(POST)
    public final static String CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    // 关闭订单接口(POST)
    public final static String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
    // 退款查询接口(POST)
    public final static String CHECK_REFUND_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
    // 对账单接口(POST)
    public final static String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
    // 短链接转换接口(POST)
    public final static String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
    // 接口调用上报接口(POST)
    public final static String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";

    /**
     * 微信基础接口地址
     */
    // 获取token接口(GET)
    public final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    // oauth2授权接口(GET)
    public final static String OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";
    // 刷新access_token接口（GET）
    public final static String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
    // 菜单创建接口（POST）
    public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    // 菜单查询（GET）
    public final static String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
    // 菜单删除（GET）
    public final static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

}
