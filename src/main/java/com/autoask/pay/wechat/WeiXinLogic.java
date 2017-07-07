package com.autoask.pay.wechat;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.SortedMap;

/**
 * Created by hp on 16-9-7.
 */
public class WeiXinLogic {
    private static final Logger LOG = LoggerFactory.getLogger(WeiXinLogic.class);

    public static ResponseDo unifiedOrder(Map<String, Object> orderParams) throws ApiException {
        String ip = orderParams.get("ip").toString();
        Integer totalFee = Integer.parseInt(orderParams.get("totalFee").toString());
        String tradeType = orderParams.get("tradeType").toString();
        String orderId = orderParams.get("orderId").toString();
        if (StringUtils.isEmpty(ip)) {
            throw new ApiException("缺少IP信息!");
        }
        if (totalFee <= 0) {
            throw new ApiException("支付金额不正确!");
        }
        if (StringUtils.isEmpty(tradeType)) {
            throw new ApiException("缺少支付类型");
        }
        if (!WeixinConstant.TRADE_TYPE_LIST.contains(tradeType)) {
            throw new ApiException("超出服务的支付范围!请选择JSAPI或者NATIVE");
        }
        if (StringUtils.isEmpty(orderId)) {
            throw new ApiException("缺少订单号");
        }

        SortedMap<String, Object> parameters = prepareOrder(ip, orderId, totalFee, tradeType);
        String sign = PayCommonUtil.createSignWithKey("UTF-8", parameters);
        parameters.put("sign", sign);
        String requestXML = PayCommonUtil.getRequestXml(parameters);
        System.out.println(requestXML);
        String responseStr = HttpUtil.httpsRequest(ConfigUtil.UNIFIED_ORDER_URL, "POST", requestXML);
        // 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
        if (!PayCommonUtil.checkIsSignValidFromResponseString(responseStr)) {
            LOG.error("微信统一下单失败,签名可能被篡改");
            return ResponseDo.buildError("统一下单失败");
        }
        return null;
    }

    /**
     * 下单参数调试
     */
    public static SortedMap<String, Object> prepareOrder(String ip, String orderId, Integer totalFee, String tradeType) {
        Map<String, Object> orderParams = ImmutableMap.<String, Object>builder()
                .put("appid", ConfigUtil.APPID) // 服务号的应用号
                .put("body", WeixinConstant.PRODUCT_BODY)   // 商品描述
                .put("mch_id", ConfigUtil.MCH_ID)   // 商户号
                .put("nonce_str", PayCommonUtil.createNonceString())    // 16随机字符串(大小写字母加数字)
                .put("out_trade_no", orderId)   // 商户订单号
                .put("total_fee", totalFee)  // 银行币种 price
                .put("spbill_create_ip", ip)// IP地址
                .put("notify_url", ConfigUtil.NOTIFY_URL) // 微信回调地址
                .put("trade_type", WeixinConstant.TRADE_TYPE.get(tradeType))
                .build();
        return MapUtil.sortMap(orderParams);
    }

    public static void main(String[] args) throws ApiException {
        ImmutableMap<String, Object> map = ImmutableMap.<String, Object>builder()
                .put("ip", "192.168.1.82")
                .put("totalFee", 10)
                .put("tradeType", "NATIVE")
                .put("orderId", "2019960902").build();

        ResponseDo responseDo = unifiedOrder(map);

    }
}
