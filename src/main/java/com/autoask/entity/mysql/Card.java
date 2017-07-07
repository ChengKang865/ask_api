package com.autoask.entity.mysql;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.text.MessageFormat;
import java.util.Date;

@Entity
@Table
public class Card {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 卡的id
     */
    private String cardId;

    /**
     * 卡的密码，暂时不用
     */
    private String verifyCode;

    /**
     * 状态
     * to_check checked used
     */
    private String status;

    /**
     * 卡的类型id
     */
    private String cardTypeId;


    /**
     * 该序列号下的排序
     */
    private Long typeSort;


    /**
     * 使用时间
     */
    private Date useTime;

    /**
     * 用户id 暂且不用
     */
    private String userId;

    /**
     * 检查时间
     */
    private Date checkTime;

    /**
     * 检查人id
     */
    private String checkId;


    private Boolean deleteFlag;

    private Date deleteTime;

    private String deleteId;

    private String orderGoodsId;

    @Transient
    private CardType cardType;

    @Transient
    private String orderId;

    @Transient
    private String phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public Long getTypeSort() {
        return typeSort;
    }

    public void setTypeSort(Long typeSort) {
        this.typeSort = typeSort;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }

    public String getOrderGoodsId() {
        return orderGoodsId;
    }

    public void setOrderGoodsId(String orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static void canUse(Card card) throws ApiException {
        if (card.getDeleteFlag()) {
            throw new ApiException("卡已经失效");
        }
        if (StringUtils.equals(Constants.CardStatus.TO_CHECK, card.getStatus())) {
            throw new ApiException("卡未激活");
        } else if (StringUtils.equals(Constants.CardStatus.USED, card.getStatus())) {
            throw new ApiException(MessageFormat.format("卡{0}已经被使用", card.getCardId()));
        }
        if (null == card.getCardType()) {
            throw new ApiException("数据异常");
        }
        CardType cardType = card.getCardType();
        if (DateUtil.getDate().compareTo(cardType.getExpireTime()) > 0) {
            throw new ApiException("卡已经失效");
        }
    }
}