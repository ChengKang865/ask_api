package com.autoask.entity.mongo.merchant;

import com.autoask.entity.mongo.BaseEntity;

/**
 * Created by weid on 16-8-13.
 */
public class BaseMerchant extends BaseEntity {

    private String loginType;

    private String name;

    //手机号码 (登录帐号)
    private String phone;

    private String password;

    private String aliPayAccount;

    private String aliPayUserName;

    private String wxPayAccount;

    private String wxPayUserName;

    private String unionPayAccount;

    private String unionPayUserName;

    private String payType;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 激活状态, true标识已经激活，false标识未激活
     */
    private Boolean activated;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAliPayAccount() {
        return aliPayAccount;
    }

    public void setAliPayAccount(String aliPayAccount) {
        this.aliPayAccount = aliPayAccount;
    }

    public String getAliPayUserName() {
        return aliPayUserName;
    }

    public void setAliPayUserName(String aliPayUserName) {
        this.aliPayUserName = aliPayUserName;
    }

    public String getWxPayAccount() {
        return wxPayAccount;
    }

    public void setWxPayAccount(String wxPayAccount) {
        this.wxPayAccount = wxPayAccount;
    }

    public String getWxPayUserName() {
        return wxPayUserName;
    }

    public void setWxPayUserName(String wxPayUserName) {
        this.wxPayUserName = wxPayUserName;
    }

    public String getUnionPayAccount() {
        return unionPayAccount;
    }

    public void setUnionPayAccount(String unionPayAccount) {
        this.unionPayAccount = unionPayAccount;
    }

    public String getUnionPayUserName() {
        return unionPayUserName;
    }

    public void setUnionPayUserName(String unionPayUserName) {
        this.unionPayUserName = unionPayUserName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }
}
