package com.autoask.service.impl.common;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CodeGenerator;
import com.autoask.common.util.PropertiesUtil;
import com.autoask.common.util.QRCodeUtil;
import com.autoask.pay.wechat.*;
import com.autoask.service.common.WeiXinPayService;
import com.autoask.service.file.FileService;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by hp on 16-9-12.
 */
@Service("Wei Xin Pay Service.")
public class WeiXinPayServiceImpl implements WeiXinPayService {
    private static final Logger LOG = LoggerFactory.getLogger(WeiXinPayServiceImpl.class);

    @Autowired
    private FileService fileService;

    /**
     * 获取微信支付链接
     *
     * @param ip          IP地址
     * @param orderNumber 订单号
     * @param totalFee    支付金额(单位:分)
     * @param tradeType   支付类型(NATIVE, JSAPI, M)
     * @return 支付链接(String)
     * @throws ApiException
     * @throws JDOMException
     * @throws IOException
     */
    @Override
    public String getUnifiedOrderUrl(String ip, String orderNumber,
                                     Integer totalFee, String tradeType) throws ApiException, JDOMException, IOException {
        // 参数校验
        if (StringUtils.isEmpty(ip)) {
            throw new ApiException("缺少IP信息!");
        }
        if (totalFee <= 0) {
            throw new ApiException("支付金额不正确!");
        }
        if (StringUtils.isEmpty(tradeType)) {
            throw new ApiException("缺少支付类型!");
        }
        if (!WeixinConstant.TRADE_TYPE_LIST.contains(tradeType)) {
            throw new ApiException("超出服务的支付范围!");
        }
        if (StringUtils.isEmpty(orderNumber)) {
            throw new ApiException("缺少订单号!");
        }

        // 获取sign参数,拼接XML参数
        SortedMap<String, Object> parameters = prepareOrderParams(ip, orderNumber, totalFee, tradeType);
        String sign = PayCommonUtil.createSignWithKey("UTF-8", parameters);
        parameters.put("sign", sign);
        String requestXML = PayCommonUtil.getRequestXml(parameters);
        // 请求支付接口
        String responseStr = HttpUtil.httpsRequest(ConfigUtil.UNIFIED_ORDER_URL, "POST", requestXML);

        // 检验API返回的数据里面的签名是否合法
        if (!PayCommonUtil.checkIsSignValidFromResponseString(responseStr)) {
            LOG.error("微信统一下单失败,签名可能被篡改");
            return null;
        }

        try {
            SortedMap<String, Object> responseMap = XMLUtil.doXMLParse(responseStr);
            if (null != responseMap) {
                LOG.info(responseMap.toString());
                return (String) responseMap.get("code_url");
            }
            return null;
        } catch (Exception e) {
            e.getMessage();
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * 开始拼接下单参数
     *
     * @param ip          IP地址
     * @param orderNumber 订单号
     * @param totalFee    总支付金额(以分为单位,且是整数)
     * @param tradeType   交易类型(NATIVE, JSAPI, M)
     * @return Map
     */
    @Override
    public SortedMap<String, Object> prepareOrderParams(String ip, String orderNumber, Integer totalFee, String tradeType) {
        LOG.info("begin format unified url parameters.");

        Map<String, Object> orderParams = ImmutableMap.<String, Object>builder()
                .put("appid", ConfigUtil.APPID)
                .put("body", WeixinConstant.PRODUCT_BODY)
                .put("mch_id", ConfigUtil.MCH_ID)
                .put("nonce_str", PayCommonUtil.createNonceString())
                .put("out_trade_no", orderNumber)
                .put("total_fee", totalFee)
                .put("spbill_create_ip", ip)
                .put("notify_url", ConfigUtil.NOTIFY_URL)
                .put("trade_type", WeixinConstant.TRADE_TYPE.get(tradeType))
                .build();
        return MapUtil.sortMap(orderParams);
    }

    /**
     * 上传支付二维码图片到七牛服务器
     *
     * @param unifiedUrl 微信返回的支付链接
     * @return 七牛的图片地址(String)
     * @throws ApiException
     * @throws IOException
     */
    @Override
    public String getOrderQRCodeUploadUrl(String unifiedUrl) throws ApiException, IOException {
        LOG.info("upload image to qi niu server.");

        // 生成支付二维码图片
        BufferedImage qrCode = QRCodeUtil.createQRCode(unifiedUrl);
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(qrCode, "jpg", byteArrayOutputStream);
            byte[] imageByte = byteArrayOutputStream.toByteArray();
            String imageKey = CodeGenerator.uuid() + ".jpg";

            // 生成链接
            Map<String, String> upload = fileService.upload(imageByte, imageKey, false);
            if (null == upload || null == upload.get("key")) {
                throw new ApiException("上传支付图片出现问题!");
            }

            // 返回图片链接
            String key = upload.get("key");
            StringBuffer urlBuffer = new StringBuffer();
            urlBuffer.append(PropertiesUtil.getProperty("qiniu.server.url", "")).append("/").append(key);
            return urlBuffer.toString();
        } catch (IOException e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(byteArrayOutputStream);
        }
    }

    /**
     * 解析微信回调接口时返回给我方的信息
     *
     * @param xmlMessage 返回的XML信息
     * @return Map
     * @throws ApiException
     * @throws IOException
     */
    @Override
    public Map<String, Object> parseWeiXinCallBackMessage(String xmlMessage) throws ApiException, IOException, JDOMException {
        if (StringUtils.isEmpty(xmlMessage)) {
            throw new ApiException("XML信息返回为空.");
        }
        try {
            if (!PayCommonUtil.checkIsSignValidFromResponseString(xmlMessage)) {
                throw new ApiException("微信返回的sign有误!");
            } else {
                return XMLUtil.doXMLParse(xmlMessage);
            }
        } catch (JDOMException e) {
            LOG.warn(e.getMessage());
            throw new ApiException("XML返回的解析信息出错: " + e.getMessage());
        }
    }

    /**
     * 告知微信通知我方的状态
     *
     * @param responseParams 返回的参数(Map)
     * @return String
     * @throws ApiException
     * @throws IOException
     * @throws JDOMException
     */
    @Override
    public String getWeiXinNotificationMessage(Map<String, String> responseParams)
            throws ApiException, IOException, JDOMException {
        LOG.info("get notify wei xin notification status's message.");

        if (null == responseParams) {
            throw new ApiException("请求接口参数为空");
        }

        try {
            String status = responseParams.get("status");
            String message = responseParams.get("message");
            if (null == status) {
                throw new ApiException("status缺失");
            }
            if (!WeixinConstant.STATUS.contains(status)) {
                throw new ApiException("status 错误!传 SUCCESS 或 FAIL");
            }

            return PayCommonUtil.setXML(status, message);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }
}
