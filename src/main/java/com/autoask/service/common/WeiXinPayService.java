package com.autoask.service.common;

import com.autoask.common.exception.ApiException;
import org.jdom.JDOMException;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by hp on 16-9-12.
 */
public interface WeiXinPayService {
    String getUnifiedOrderUrl(String ip, String orderNumber,
                              Integer totalFee, String tradeType) throws ApiException, JDOMException, IOException;

    SortedMap<String, Object> prepareOrderParams(String ip, String orderId, Integer totalFee, String tradeType);

    String getOrderQRCodeUploadUrl(String unifiedUrl) throws ApiException, IOException;

    Map<String, Object> parseWeiXinCallBackMessage(String xmlMessage) throws ApiException, IOException, JDOMException;

    String getWeiXinNotificationMessage(Map<String, String> responseParams) throws ApiException, IOException, JDOMException;
}
