package com.autoask.pay.wechat;

import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 微信支付常量类
 */
public class WeixinConstant {

    public static final String SUCCESS = "SUCCESS";

    public static final String FAIL = "FAIL";

    public static final String PRODUCT_BODY = "AutoASK商城订单支付";

    public static final List<String> STATUS = Arrays.asList(SUCCESS, FAIL);


    public static Map<String, String> TRADE_TYPE = ImmutableMap.<String, String>builder()
            .put("JSAPI", "JSAPI").put("NATIVE", "NATIVE").build();

    public static List<String> TRADE_TYPE_LIST = Arrays.asList("JSAPI", "NATIVE");
}
