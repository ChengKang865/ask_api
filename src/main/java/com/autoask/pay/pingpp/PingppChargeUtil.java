package com.autoask.pay.pingpp;

import com.autoask.common.exception.ApiException;
import com.autoask.pay.pingpp.config.PingppConfig;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.PingppException;
import com.pingplusplus.exception.RateLimitException;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeCollection;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 16-10-10.
 * <p>
 * Ping++ 付款 util.
 */
public class PingppChargeUtil {
    private static final Logger LOG = LoggerFactory.getLogger(PingppChargeUtil.class);

    private static String appId = PingppConfig.appId;

    /**
     * 发起预支付接口.
     * - 特定渠道发起交易时需要的额外参数，以及部分渠道支付成功返回的额外参数。
     * - alipay_qr ，bfb ，cp_b2b ，upacp ，applepay_upacp 渠道不需要 extra 参数。
     *
     * @param amount      支付金额
     * @param subject     标题
     * @param body        描述信息
     * @param ip          请求端的ip地址
     * @param orderNumber 我方订单号
     * @param channel     支付渠道
     * @param extraMap    extra参数集
     * @return Charge instance
     * @throws ApiException
     */
    public static Charge createCharge(Integer amount, String subject, String body, String ip, String orderNumber,
                                      String channel, Map<String, String> extraMap) throws ApiException {
        Pingpp.apiKey = PingppConfig.liveSecretKey;
        Pingpp.privateKeyPath = PingppConfig.getPrivateKeyFilePath();

        // 校验请求支付的参数
        verifyAmount(amount);
        verifyIp(ip);
        verifyOrderNo(orderNumber);
        verifyChannel(channel);
        verifyExtraField(channel, extraMap);

        Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<>();
        chargeMap.put("amount", amount);        //订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
        chargeMap.put("currency", "cny");       // 三位 ISO 货币代码，目前仅支持人民币  cny
        chargeMap.put("subject", subject);      // 商品的标题，该参数最长为 32 个 Unicode 字符
        chargeMap.put("body", body);            // 商品的描述信息
        chargeMap.put("order_no", orderNumber); // 推荐使用 8-20 位，要求数字或字母，不允许其他字符
        chargeMap.put("channel", channel);      // 支付使用的第三方支付渠道取值，请参考：https://www.pingxx.com/api#api-c-new
        chargeMap.put("client_ip", ip);         // 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1

        Map<String, String> app = new HashMap<>();
        app.put("id", appId);
        chargeMap.put("app", app);
        chargeMap.put("extra", extraMap);

        try {
            //发起交易请求
            charge = Charge.create(chargeMap);
        } catch (PingppException e) {
            LOG.error("create Charge error: {}", e.getMessage());
            throw new ApiException(e.getMessage());
        }
        return charge;
    }

    public static Charge createWxQr(String orderId, Integer amount, String ip) throws ApiException {
        Pingpp.apiKey = PingppConfig.liveSecretKey;
        Pingpp.privateKeyPath = PingppConfig.getPrivateKeyFilePath();
        String subject = "autoask 汽车备品";
        String body = "autoask 汽车备品";
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("order_no", orderId);
        chargeParams.put("amount", amount); //订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
        Map<String, String> app = new HashMap<>();
        app.put("id", appId);
        chargeParams.put("app", app);
        chargeParams.put("channel", PingppConfig.PayChannel.WX_PUB_QR);
        chargeParams.put("currency", "cny");
        chargeParams.put("client_ip", ip);
        chargeParams.put("subject", subject);
        chargeParams.put("body", body);
        Map<String, String> extraMap = new HashMap<>();
        extraMap.put("product_id", orderId);
        chargeParams.put("extra", extraMap);
        Charge charge = null;
        try {
            charge = Charge.create(chargeParams);
        } catch (Exception e) {
            LOG.error("createWxQr error: {}", e.getMessage());
            throw new ApiException(e.getMessage());
        }
        return charge;
    }

    /**
     * 创建 Charge (微信公众号)
     * <p>
     * 创建 Charge 用户需要组装一个 map 对象作为参数传递给 Charge.create();
     * map 里面参数的具体说明请参考：https://pingxx.com/document/api#api-c-new
     *
     * @param openid  用户的 openid
     * @param orderId 订单号
     * @param amount  支付金额
     * @param ip      用户 ip 地址
     * @param subject 用户的 subject
     * @param body    描述信息
     * @return Charge
     */
    public static Charge createChargeWithOpenId(String openid, String orderId,
                                                Integer amount, String ip, String subject, String body) throws ApiException {
        Pingpp.apiKey = PingppConfig.liveSecretKey;
        Pingpp.privateKeyPath = PingppConfig.getPrivateKeyFilePath();

        Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<>();
        chargeMap.put("order_no", orderId);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
        chargeMap.put("amount", amount);    //订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
        Map<String, String> app = new HashMap<>();
        app.put("id", appId);
        chargeMap.put("app", app);
        chargeMap.put("channel", "wx_pub");// 支付使用的第三方支付渠道取值，请参考：https://www.pingxx.com/api#api-c-new
        chargeMap.put("currency", "cny");
        chargeMap.put("client_ip", ip); // 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
        chargeMap.put("subject", subject);
        chargeMap.put("body", body);
        Map<String, Object> extra = new HashMap<>();
        extra.put("open_id", openid);// 用户在商户微信公众号下的唯一标识，获取方式可参考 WxPubOAuthExample.java
        chargeMap.put("extra", extra);
        try {
            charge = Charge.create(chargeMap);
        } catch (PingppException e) {
            LOG.error("createChargeWithOpenId error: {}", e.getCause());
            throw new ApiException(e.getMessage());
        }
        return charge;
    }

    /**
     * 查询 Charge
     * <p>
     * 该接口根据 charge Id 查询对应的 charge 。
     * 参考文档：https://pingxx.com/document/api#api-c-inquiry
     * <p>
     * 该接口可以传递一个 expand ， 返回的 charge 中的 app 会变成 app 对象。
     * 参考文档： https://pingxx.com/document/api#api-expanding
     *
     * @param id
     */
    public static Charge retrieve(String id) {
        Charge charge = null;
        try {
            Map<String, Object> params = new HashMap<>();
//            List<String> expand = new ArrayList<String>();
//            expand.add("app");
//            params.put("expand", expand);
            charge = Charge.retrieve(id, params);
        } catch (PingppException e) {
            e.printStackTrace();
        }

        return charge;
    }

    /**
     * 分页查询 Charge
     * <p>
     * 该接口为批量查询接口，默认一次查询10条。
     * 用户可以通过添加 limit 参数自行设置查询数目，最多一次不能超过 100 条。
     * <p>
     * 该接口同样可以使用 expand 参数。
     *
     * @return chargeCollection
     */
    public ChargeCollection all() throws ApiException, RateLimitException {
        ChargeCollection chargeCollection = null;
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 3);
        Map<String, String> app = new HashMap<>();
        app.put("id", this.appId);
        params.put("app", app);

        try {
            chargeCollection = Charge.all(params);
            System.out.println(chargeCollection);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
        return chargeCollection;
    }

    public static SecureRandom random = new SecureRandom();

    public static String randomString(int length) {
        String str = new BigInteger(130, random).toString(32);
        return str.substring(0, length);
    }

    /**
     * 校验支付总额
     *
     * @param amount 支付总额
     * @throws ApiException
     */
    public static void verifyAmount(Integer amount) throws ApiException {
        if (null == amount) {
            throw new ApiException("金额为空！");
        }

        if ((float) amount < 0.0) {
            throw new ApiException("支付金额不正确！必须大于0！");
        }
    }

    /**
     * 校验 IP 地址
     *
     * @param ip ip地址
     * @throws ApiException
     */
    public static void verifyIp(String ip) throws ApiException {
        if (StringUtils.isEmpty(ip)) {
            throw new ApiException("ip 地址为空!");
        }
    }

    /**
     * 校验订单号
     *
     * @param orderNum 订单号
     * @throws ApiException
     */
    public static void verifyOrderNo(String orderNum) throws ApiException {
        if (StringUtils.isEmpty(orderNum)) {
            throw new ApiException("订单号为空！");
        }
    }

    /**
     * 支付渠道校验
     *
     * @param channel 渠道
     * @throws ApiException
     */
    public static void verifyChannel(String channel) throws ApiException {
        if (StringUtils.isEmpty(channel)) {
            throw new ApiException("支付渠道为空！");
        }

        if (!PingppConfig.PayChannel.CHANNEL_LIST.contains(channel)) {
            throw new ApiException("支付渠道超出范围!");
        }
    }

    /**
     * 校验 extra 参数
     * 各个渠道的 extra 参数说明见链接： https://www.pingxx.com/api?language=Java#支付渠道-extra-参数说明
     *
     * @param channel  支付渠道
     * @param extraMap extra map
     * @throws ApiException
     */
    public static void verifyExtraField(String channel, Map<String, String> extraMap) throws ApiException {
        /*
         * 如果是 支付宝 wap 支付
         */
        if (StringUtils.equals(channel, PingppConfig.PayChannel.ALIPAY_WAP)) {

            if (MapUtils.isEmpty(extraMap)) {
                throw new ApiException("extra 参数为空!");
            }

            String successUrl = extraMap.get("success_url");

            if (StringUtils.isEmpty(successUrl)) {
                throw new ApiException("支付宝 WAP 支付时，必须要传 success_url 参数！");
            }
        }

        /*
         * 如果是 支付宝 PC 支付
         */
        if (StringUtils.equals(channel, PingppConfig.PayChannel.ALIPAY_PC_DIRECT)) {
            if (MapUtils.isEmpty(extraMap)) {
                throw new ApiException("extra 参数为空!");
            }

            String successUrl = extraMap.get("success_url");

            if (StringUtils.isEmpty(successUrl)) {
                throw new ApiException("支付宝 PC 网页支付时，必须传 success_url 参数！");
            }
        }

        /*
         * 如果是 微信 wx_pub 微信公众号支付
         */
        if (StringUtils.equals(channel, PingppConfig.PayChannel.WX_PUB)) {
            if (MapUtils.isEmpty(extraMap)) {
                throw new ApiException("extra 参数为空!");
            }

            String openId = extraMap.get("open_id");

            if (StringUtils.isEmpty(openId)) {
                throw new ApiException("微信公众号支付必须要传 openId 参数!");
            }
        }

        /*
         * 如果是 微信 wx_pub_qr 微信公众号扫码支付
         */
        if (StringUtils.equals(channel, PingppConfig.PayChannel.WX_PUB_QR)) {
            if (MapUtils.isEmpty(extraMap)) {
                throw new ApiException("extra 参数为空!");
            }

            String productId = extraMap.get("product_id");

            if (StringUtils.isEmpty(productId)) {
                throw new ApiException("微信公众号扫码支付必须要传 product_id 参数!");
            }
        }
    }

}
