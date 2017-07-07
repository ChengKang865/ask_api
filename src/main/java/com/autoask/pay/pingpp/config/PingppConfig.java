package com.autoask.pay.pingpp.config;

import com.autoask.common.util.PropertiesUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hp on 16-10-10.
 */
public class PingppConfig {
    /**
     * Test Secret Key
     */
    public final static String testSecretKey = "sk_test_8OiPG81SW1yDWzLyDKiTmHCG";

    /**
     * Live Secret Key
     */
    public final static String liveSecretKey = "sk_live_0ePajPzfT4GOeX1arPLqbDqH";

    /**
     * Test Publishable Key
     * <p>
     * Publishable Key 仅限于 应用内快捷支付，非应用内快捷支付请忽略 Publishable Key ，仅需使用 Secret Key 于服务端请求即可。
     */
    public final static String testPublishableKey = "pk_test_SurTaLLi5OG8a9O8GOLOyjjT";

    /**
     * Live Publishable Key
     */
    public final static String livePublishableKey = "pk_live_G48in5LC4afTXTGu5S1COev1";

    /**
     * Pingpp 管理平台对应的应用 ID.
     * app_id 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击你创建的应用->应用首页->应用 ID(App ID)
     */
    public final static String appId = "app_zXTSKC4GyLqTSCKC";

    /*
     * 设置请求签名密钥，密钥对需要你自己用 openssl 工具生成，如何生成可以参考帮助中心：https://help.pingxx.com/article/123161；
     * 生成密钥后，需要在代码中设置请求签名的私钥(rsa_private_key.pem)；
     * 然后登录 [Dashboard](https://dashboard.pingxx.com)->点击右上角公司名称->开发信息->商户公钥（用于商户身份验证）
     * 将你的公钥复制粘贴进去并且保存->先启用 Test 模式进行测试->测试通过后启用 Live 模式
     */

    /**
     * 获取私钥
     *
     * @return String
     */
    public static String getPrivateKeyFilePath() {
        return PropertiesUtil.class.getClassLoader().getResource("pem/autoask_rsa_private_key.pem").getFile();
    }

    /**
     * 获取Ping++公钥
     *
     * @return String
     */
    public static String getPingppPublicKeyPath() {
        return PropertiesUtil.class.getClassLoader().getResource("pem/pingpp_public_key.pem").getFile();
    }

    /**
     * 支付渠道
     */
    public interface PayChannel {
        String ALIPAY_WAP = "alipay_wap";               // 支付宝手机网页支付
        String ALIPAY_PC_DIRECT = "alipay_pc_direct";   // 支付宝 PC 网页支付
        String ALIPAY_QR = "alipay_qr";                 // 支付宝当面付
        String CP_B2B = "cp_b2b";                       // 银联企业网银支付
        String UPACP = "upacp";                         // 银联全渠道支付，即银联 APP 支付
        String UPACP_WAP = "upacp_wap";                 // 银联全渠道手机网页支付
        String UPACP_PC = "upacp_pc";                   // 银联 PC 网页支付
        String WX_PUB = "wx_pub";                       // 微信公众号支付
        String WX_PUB_QR = "wx_pub_qr";                 // 微信公众号扫码支付
        String WX_WAP = "wx_wap";                       // 微信 WAP 支付
        String WX = "wx";                             // 微信支付，即微信 APP 支付

        List CHANNEL_LIST = Arrays.asList(ALIPAY_WAP, ALIPAY_PC_DIRECT, ALIPAY_QR, CP_B2B,
                UPACP, UPACP_WAP, UPACP_PC, WX_PUB, WX_PUB_QR, WX_WAP, WX);
    }

    /**
     * Event 事件类型
     */
    public interface EventType {
        String SUMMARY_DAYLY_AVAILABLE = "summary.daily.available";     // 上一天 0 点到 23 点 59 分 59 秒的交易金额和交易量统计，在每日 02:00 点左右触发
        String SUMMARY_WEEKLY_AVAILABLE = "summary.weekly.available";   // 上周一 0 点至上周日 23 点 59 分 59 秒的交易金额和交易量统计，在每周一 02:00 点左右触发
        String SUMMARY_MONTHLY_AVAILABLE = "summary.monthly.available"; // 上月一日 0 点至上月末 23 点 59 分 59 秒的交易金额和交易量统计，在每月一日 02:00 点左右触发
        String CHARGE_SUCCEEDED = "charge.succeeded";                   // 支付对象，支付成功时触发
        String REFUND_SUCCEEDED = "refund.succeeded";                   // 退款对象，退款成功时触发
        String TRANSFER_SUCCEEDED = "transfer.succeeded";               // 企业支付对象，支付成功时触发
        String RED_EVELOP_SENT = "red_envelope.sent";                   // 红包对象，红包发送成功时触发
        String RED_EVELOPE_RECEIVED = "red_envelope.received";          // 红包对象，红包接收成功时触发
    }

    public interface TransferChannel {
        String UNION_PAY = "unionpay";  // 银联支付
        String WX_PUB = "wx_pub";   // 微信公众号支付
        String WX = "wx";   // 微信（渠道）
        List CHANNEL_LIST = Arrays.asList(UNION_PAY, WX, WX_PUB);
    }

}
