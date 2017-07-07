package com.autoask.pay.pingpp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.autoask.pay.pingpp.config.PingppConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 16-10-11.
 * 说明:
 * 对 Event 事件进行处理.
 * 1. 验证签名是否正确
 * 调用方式: 直接调用 verifyData() 方法获取校验结果;
 * 传递参数:
 * 1. webhooksRawPostData: Event事件返回的 json 数据. 该数据请从 request 中获取原始 POST 请求数据;
 * 2. signature: Event事件返回的 sign 签名数据. 该签名数据请从 request 的 header 中获取, key 为 HTTP_X_PINGPLUSPLUS_SIGNATURE;
 * 3. publicKey: Ping++ 的公钥. 可以直接调用 getPubKey() 方法获取.
 * <p>
 * 2. 对返回参数进行解析
 */
public class PingppWebhooksUtil {

    private static String pubKeyPath = PingppConfig.getPingppPublicKeyPath();

    /**
     * 读取文件, 部署 web 程序的时候, 签名和验签内容需要从 request 中获得
     *
     * @param filePath 文件路径
     * @return 文件内容(String)
     * @throws Exception
     */
    public static String getStringFromFile(String filePath) throws Exception {
        FileInputStream in = new FileInputStream(filePath);
        InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
        BufferedReader bf = new BufferedReader(inReader);
        StringBuilder sb = new StringBuilder();
        String line;
        do {
            line = bf.readLine();
            if (line != null) {
                if (sb.length() != 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
        } while (line != null);
        bf.close();
        in.close();
        return sb.toString();
    }

    /**
     * 获得公钥
     *
     * @return 公钥
     * @throws Exception
     */
    public static PublicKey getPubKey() throws Exception {
        String pubKeyString = getStringFromFile(pubKeyPath);
        pubKeyString = pubKeyString.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
        byte[] keyBytes = Base64.decodeBase64(pubKeyString);

        // generate public key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    /**
     * 验证签名
     *
     * @param dataString      校验的参数
     * @param signatureString 签名
     * @param publicKey       公钥
     * @return true / false
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static boolean verifyData(String dataString, String signatureString, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        byte[] signatureBytes = Base64.decodeBase64(signatureString);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(dataString.getBytes("UTF-8"));
        return signature.verify(signatureBytes);
    }

    /**
     * 解析返回的参数信息
     * 说明:
     * 1. webhooks 事件是在交易成功之后触发的,且只有成功的情况下,Ping++会发送webhooks推送.
     * 使用指南: https://www.pingxx.com/docs/webhooks/webhooks
     * <p>
     * 2. 付款成功类:
     * paid: boolean 类型.
     * charge_id: Ping++ 端支付 id .
     * order_no: 我方 支付 id.
     * 业务端在获取到支付核心的 charge_id, order_no, paid 后, 可以更新业务逻辑.
     * <p>
     * 3. 退款成功类:
     * succeed: boolean 类型.
     * charge_id: Ping++ 端的支付id.
     * refund_id: Charge 类下面,会有一个或多个 refund_id 参数.
     * 业务端在获取到退款核心的 charge_id, refund_id, order_no, succeed 后, 可以更新业务逻辑.
     *
     * @return Map
     * @throws Exception
     */
    public static Map<String, Object> parseReturnData(String jsonString) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String type = (String) jsonObject.get("type");

        if (StringUtils.equals(type, PingppConfig.EventType.CHARGE_SUCCEEDED)) {
            // 付款成功类
            JSONObject data = jsonObject.getJSONObject("data").getJSONObject("object");
            resultMap.put("charge_id", data.get("id"));         // chargeId 必须要保存!!!
            resultMap.put("created", data.get("created"));      // 创建的时间戳
            resultMap.put("order_no", data.get("order_no"));    // 商户订单号(我们这边生成的)
            resultMap.put("object", data.get("object"));        // 通知对象
            resultMap.put("live_mode", data.get("livemode"));   // 支付模式
            resultMap.put("channel", data.get("channel"));      // 支付渠道
            resultMap.put("paid", data.get("paid"));            // 是否已付款
            resultMap.put("amount", data.get("amount"));        // 订单总金额(分为单位)
            resultMap.put("time_paid", data.get("time_paid"));  // 支付完成的时间戳
        }
        if (StringUtils.equals(type, PingppConfig.EventType.REFUND_SUCCEEDED)) {
            // 退款成功类
            JSONObject data = jsonObject.getJSONObject("data").getJSONObject("object");
            resultMap.put("charge_id", data.get("charge"));                     // 支付 chargeId
            resultMap.put("refund_id", data.get("id"));                         // 退款 refundId
            resultMap.put("created", data.get("created"));                      // 创建的时间戳
            resultMap.put("order_no", data.get("charge_order_no"));             // 商户订单号(我们这边生成的)
            resultMap.put("object", data.get("object"));                        // 通知对象
            resultMap.put("status", data.get("status"));                        // 退款状态 pending: 处理中; succeeded: 成功; failed: 失败
            resultMap.put("succeed", data.get("succeed"));                      // 退款是否成功
            resultMap.put("failure_code", data.getJSONObject("failure_code"));  // 如果错误,返回的错误吗
            resultMap.put("failure_msg", data.get("failure_msg"));              // 错误描述信息
        }
        return resultMap;
    }

}
