package com.autoask.service.merchant;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mongo.invoice.VatInvoice;
import com.autoask.entity.mongo.merchant.BaseMerchant;
import com.autoask.entity.mongo.merchant.Mechanic;
import com.autoask.entity.mongo.merchant.Outlets;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.entity.mysql.OrderInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by weid on 16-11-5.
 */
public interface MerchantService {

    /**
     * 申请提取金额
     *
     * @param merchantId
     * @param merchantType
     */
    void saveShareApply(String merchantId, String merchantType, BigDecimal amount, BigDecimal fee) throws ApiException;

    /**
     * 根据商户ID获取商户基本信息
     *
     * @param merchantType
     * @param merchantId
     * @return
     * @throws ApiException
     */
    BaseMerchant getMerchantInfo(String merchantType, String merchantId) throws ApiException;


    /**
     * 通过商户类型以及名称 获取商户的idList
     *
     * @param merchantType
     * @param merchantName
     * @return
     * @throws ApiException
     */
    List<String> searchMerchantIdByName(String merchantType, String merchantName) throws ApiException;

    /**
     * 获取商户的名称map key 为 merchantType+merchantId
     *
     * @param baseMerchantList
     * @return
     * @throws ApiException
     */
    Map<String, String> getMerchantNameMap(List<BaseMerchant> baseMerchantList) throws ApiException;


    /**
     * 初始化订单中的商户信息 包含了orderDelivery中的merchantName orderServe中的 serviceProviderName PartnerName mechanicName
     *
     * @param orderInfoList
     * @throws ApiException
     */
    void initOrderMerchantName(List<OrderInfo> orderInfoList) throws ApiException;

    /**
     * 激活商户
     *
     * @param phone
     * @param code
     * @param activeSessionId
     * @throws ApiException
     */
    void activeMerchant(String phone, String code, String activeSessionId) throws ApiException;

    //检查商户的手机号码是否冲突 创建商户 以及更新商户信息的时候需要检查
    void checkMerchantPhone(String merchantType, String merchantId, String phone) throws ApiException;

    Map<String, Object> getMerchantAccount() throws ApiException;

    /**
     * 更新商户的 支付宝账号
     *
     * @param aliPayAccount
     * @param aliPayUserName
     * @throws ApiException
     */
    void updateMerchantAliAccount(String aliPayAccount, String aliPayUserName) throws ApiException;

    void updateMerchantPassword(String password) throws ApiException;

    void updateMerchantVatInvoice(VatInvoice vatInvoice) throws ApiException;

    VatInvoice getMerchantVatInvoice() throws ApiException;

    String getSelfQRCode() throws ApiException;

    void updateSelfQRCode(String code) throws ApiException;
}
