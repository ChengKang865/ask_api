package com.autoask.common.phone;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.MessageFormat;

/**
 * @author licheng
 */
public class MessageService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    /**
     * 发送手机验证码消息
     *
     * @param phone      手机消息
     * @param verifyCode 验证码消息
     * @return 返回发送结果，发送成功返回true，发送失败或者报错返回false
     */
    public static boolean sendCode(String phone, String verifyCode) {
        return sendChuangLanCode(phone, verifyCode);
    }

    public static void sendRefundMsg(String phone, String orderId) {
        try {
            MessageService.sendMessage(phone,
                    MessageFormat.format(PropertiesUtil.getProperty("message.send.chuanglan.refused", ""), orderId));
        } catch (ApiException e) {
            LOG.error(e.getMessage());
        }
    }

    public static void sendValidateMsg(String userPhone, String serviceProviderName, String code) {
        try {
            MessageService.sendMessage(userPhone,
                    MessageFormat.format(PropertiesUtil.getProperty("message.send.chuanglan.validate", ""), serviceProviderName, code));
        } catch (ApiException e) {
            LOG.error(e.getMessage());
        }
    }

    public static void sendMessage(String phone, String message) throws ApiException {
        SendMessageByChuanglan.sendMessage(phone, message);
    }

    private static boolean sendChuangLanCode(String phone, String verifyCode) {
        boolean sendResult = true;
        try {
            SendMessageByChuanglan.sendCodeMessage(phone, verifyCode);
        } catch (Exception e) {
            LOG.warn("Send message by Chuanglan failure");
            LOG.error(e.getMessage(), e);
            sendResult = false;
        }
        return sendResult;
    }


    public static boolean sendMechanicShareMessage(String phone, BigDecimal amount, BigDecimal balance, String incomeTime) throws ApiException {
        SendMessageByChuanglan.sendMessage(phone, MessageFormat.format(PropertiesUtil.getProperty("message.send.mechanic.share", ""), amount, balance, incomeTime));
        return true;
    }

    public static boolean sendIncomeMessage(String phone, BigDecimal amount) throws ApiException {
        SendMessageByChuanglan.sendMessage(phone, MessageFormat.format(PropertiesUtil.getProperty("message.send.income", ""), amount));
        return true;
    }

    public static boolean sendOutletsShareMessage(String phone, BigDecimal amount, BigDecimal balance, String incomeTime) throws ApiException {
        SendMessageByChuanglan.sendMessage(phone, MessageFormat.format(PropertiesUtil.getProperty("message.send.outlets.share", ""), amount, balance, incomeTime));
        return true;
    }

    //发送给修理厂通知新订单
    public static boolean sendNoticeMsg(String phone) throws ApiException {
    	MessageService.sendMessage(phone,
                MessageFormat.format(PropertiesUtil.getProperty("message.send.chuanglan.notice", ""),""));
        return true;
    }

}