package com.autoask.pay.wechat;

/**
 * Created by hp on 16-9-7.
 */

import com.autoask.common.exception.ApiException;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;

public class PayCommonUtil {
    private static Logger LOG = LoggerFactory.getLogger(PayCommonUtil.class);

    /**
     * 自定义长度随机字符串
     */
    public static String createNonceString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < length; i++) {
            Random rd = new Random();
            res += chars.indexOf(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    /**
     * 默认16 位随机字符串
     */
    public static String createNonceString() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < 16; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    /**
     * 签名工具 包含商户id号
     */
    public static String createSignWithKey(String characterEncoding,
                                           Map<String, Object> parameters) {
        StringBuffer sb = getMapStringBuffer(parameters);
        sb.append("key=" + ConfigUtil.DEALER_KEY);
        //注意sign转为大写
        return MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
    }

    public static StringBuffer getMapStringBuffer(Map<String, Object> parameters) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Entry<String, Object> entry : parameters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();    // 去掉带 sign 的项
            if (null != value && !"".equals(value) && !"sign".equals(key)
                    && !"key".equals(key)) {
                stringBuffer.append(key + "=" + value + "&");
            }
        }
        return stringBuffer;
    }

    /**
     * 签名工具 不含商户密钥 － 暂时不用
     */
    public static String createSignWithoutKey(String characterEncoding, Map<String, Object> parameters) throws ApiException {
        StringBuffer sb = getMapStringBuffer(parameters);
        String signStr = sb.toString();
        String subStr = signStr.substring(0, signStr.length() - 1);
        //注意sign转为大写
        return MD5Util.MD5Encode(subStr, characterEncoding).toUpperCase();
    }

    /**
     * 请求参数
     */
    public static String getRequestXml(SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (Entry<String, Object> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = (String) entry.getValue();
            sb.append("<" + key + ">" + value + "</" + key + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 设置XML参数
     */
    public static String setXML(String returnCode, String returnMessage) {
        return "<xml><return_code><![CDATA[" + returnCode
                + "]]></return_code><return_msg><![CDATA[" + returnMessage
                + "]]></return_msg></xml>";
    }

    /**
     * 从 API 返回的 XML 数据里面重新计算一次签名
     */
    public static String getSignFromResponseString(String responseString)
            throws IOException, SAXException, ParserConfigurationException, JDOMException {
        SortedMap<String, Object> map = XMLUtil.doXMLParse(responseString);
        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
        map.put("sign", "");
        // map.remove("sign");
        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        return PayCommonUtil.createSignWithKey("UTF-8", map);
    }

    /**
     * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
     */
    public static boolean checkIsSignValidFromResponseString(String responseString) {
        try {
            SortedMap<String, Object> map = XMLUtil.doXMLParse(responseString);
            //Map<String, Object> map = XMLParser.getMapFromXML(responseString);
            LOG.debug(map.toString());
            String signFromAPIResponse = map.get("sign").toString();
            if ("".equals(signFromAPIResponse) || signFromAPIResponse == null) {
                LOG.debug("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
                return false;
            }
            LOG.debug("服务器回包里面的签名是:" + signFromAPIResponse);
            // 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
            map.put("sign", "");
            // 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
            String signForAPIResponse = PayCommonUtil.createSignWithKey("UTF-8", map);
            if (!signForAPIResponse.equals(signFromAPIResponse)) {
                // 签名验不过，表示这个API返回的数据有可能已经被篡改了
                LOG.debug("微信API返回的数据签名验证不通过，有可能被第三方篡改!!!");
                return false;
            }
            LOG.debug("恭喜，微信API返回的数据签名验证通过!!!");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String createNonceStr = PayCommonUtil.createNonceString();
        System.out.println(createNonceStr); // debug
    }
}
