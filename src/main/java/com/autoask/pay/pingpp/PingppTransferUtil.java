package com.autoask.pay.pingpp;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.BigDecimalUtil;
import com.autoask.pay.pingpp.config.PingppConfig;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.model.Transfer;
import com.pingplusplus.model.TransferCollection;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp on 16-10-29.
 * <p>
 * Ping++ 批量转账模块。
 * 企业付款流程参考开发者中心：https://www.pingxx.com/docs/server/transfer
 * openid 是发送红包的对象在公共平台下的 openid
 */
public class PingppTransferUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PingppTransferUtil.class);

    private static String appId = PingppConfig.appId;

    /**
     * 转账申请接口
     * 注意:　extra 参数参考: https://www.pingxx.com/api?language=Java#企业付款-extra-参数说明
     *
     * @param orderNo     订单 id
     * @param channel     支付渠道（目前支持 wx_pub 和  unionpay 企业付款（银行卡)， wx(新渠道)）
     * @param amount      转账金额(单位：元)
     * @param recipient   接收者 id，为用户在 wx_pub 下的  open_id ;渠道为  unionpay 时，不需要传该参数。
     * @param description 描述信息
     * @param extra       额外信息
     * @return Transfer instance
     * @throws APIException
     */
    public static Transfer transfer(String orderNo, String channel, BigDecimal amount,
                                    String recipient, String description, Map<String, Object> extra) throws ApiException {
        verifyChannel(channel);
        verifyExtraParam(channel, extra);
        verifyRecipient(channel, recipient);
        verifyAmount(amount);

        Pingpp.apiKey = PingppConfig.liveSecretKey;

        Transfer transfer = null;
        Map<String, Object> transferMap = new HashMap<String, Object>();
        transferMap.put("channel", channel);               // 目前支持 wx(新渠道)、 wx_pub
        transferMap.put("order_no", orderNo);               // 企业转账使用的商户内部订单号。wx(新渠道)、wx_pub 规定为 1 ~ 50 位不能重复的数字字母组合
        transferMap.put("amount", BigDecimalUtil.decimal2Int(amount));       // 订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100,企业付款最小发送金额为 1 元）
        transferMap.put("type", "b2c");                     // 付款类型，当前仅支持 b2c 企业付款
        transferMap.put("currency", "cny");                 // 付款类型
        transferMap.put("recipient", recipient);
        transferMap.put("description", description);

        Map<String, String> app = new HashMap<String, String>();
        app.put("id", appId);
        transferMap.put("app", app);

        try {
            return Transfer.create(transferMap);
        } catch (Exception e) {
            LOG.error("Something error while trying to transfer money", e);
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * 校验金额
     *
     * @param amount
     * @throws ApiException
     */
    private static void verifyAmount(BigDecimal amount) throws ApiException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException("金额不能小于零");
        }
    }

    /**
     * 根据 ID 查询
     * <p>
     * 根据 ID 查询企业转账记录。
     * 参考文档：https://pingxx.com/document/api#api-t-inquiry
     *
     * @param id
     */
    public static Transfer transferQuery(String id) throws ApiException {
        Map<String, Object> param = new HashMap<String, Object>();
        try {
            return Transfer.retrieve(id, param);
        } catch (Exception e) {
            LOG.error("Call ping++ transfer failure", e);
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * 批量查询
     * 默认一次查询 10 条，用户可以使用 limit 自定义查询数目，但是最多不超过 100 条
     *
     * @param batchTransferId
     * @return
     * @throws ApiException
     */
    public static List<Transfer> batchTransferQuery(List<String> batchTransferId) throws ApiException {
        Map<String, Object> param = new HashedMap();
        try {
            TransferCollection transferCollection = Transfer.all(param);
            return (List<Transfer>) transferCollection;
        } catch (Exception e) {
            LOG.error("Something error while trying to get batch transfer", e);
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * 校验支付渠道
     *
     * @param channel
     * @throws ApiException
     */
    private static void verifyChannel(String channel) throws ApiException {
        if (StringUtils.isEmpty(channel)) {
            throw new ApiException("支付渠道为空!");
        } else {
            if (!PingppConfig.TransferChannel.CHANNEL_LIST.contains(channel)) {
                throw new ApiException("支付渠道不在支持范围!");
            }
        }
    }

    /**
     * 校验支付渠道对应的 extra 参数
     *
     * @param channel
     * @param extraMap
     * @throws ApiException
     */
    private static void verifyExtraParam(String channel, Map<String, Object> extraMap) throws ApiException {
        if (StringUtils.equals(channel, PingppConfig.TransferChannel.UNION_PAY)) {
            // 银联支付
            String cardNumber = (String) extraMap.get("card_number");
            String userName = (String) extraMap.get("user_name");

            if (StringUtils.isEmpty(cardNumber)) {
                throw new ApiException("转账卡号为空!");
            }

            if (StringUtils.isEmpty(userName)) {
                throw new ApiException("转账用户名为空!");
            }
        }
    }

    /**
     * 校验接受者 id
     *
     * @param channel
     * @param recipient
     * @throws ApiException
     */
    private static void verifyRecipient(String channel, String recipient) throws ApiException {
        if (Arrays.asList(PingppConfig.TransferChannel.WX, PingppConfig.TransferChannel.WX_PUB).contains(channel)) {
            if (StringUtils.isEmpty(recipient)) {
                throw new ApiException("微信支付必须要传用户的 openId");
            }
        }
    }
}