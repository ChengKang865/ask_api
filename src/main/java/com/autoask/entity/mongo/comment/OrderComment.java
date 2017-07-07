package com.autoask.entity.mongo.comment;

import com.autoask.entity.mongo.user.UserInfo;
import com.autoask.entity.mysql.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;
import java.util.List;

/**
 * @author hyy
 * @create 2016-11-03 18:29
 */
@Document
public class OrderComment {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String orderId;

    private String comment;

    private Integer rate;

    private List<String> picUrlList;

    @Indexed
    private String mechanicId;

    @Indexed
    private String serviceProviderId;

    @Indexed
    private Date createTime;

    @Transient
    private User user;

    @Transient
    private UserInfo userInfo;

    @Transient
    private List<GoodsComment> goodsCommentList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public List<String> getPicUrlList() {
        return picUrlList;
    }

    public void setPicUrlList(List<String> picUrlList) {
        this.picUrlList = picUrlList;
    }

    public String getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(String mechanicId) {
        this.mechanicId = mechanicId;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<GoodsComment> getGoodsCommentList() {
        return goodsCommentList;
    }

    public void setGoodsCommentList(List<GoodsComment> goodsCommentList) {
        this.goodsCommentList = goodsCommentList;
    }
}
