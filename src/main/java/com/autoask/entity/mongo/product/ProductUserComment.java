package com.autoask.entity.mongo.product;

import com.autoask.entity.mongo.BaseEntity;
import com.autoask.entity.mysql.GoodsSnapshot;
import com.autoask.entity.mysql.User;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by hp on 16-9-24.
 */
@Document
public class ProductUserComment extends BaseEntity {
    /**
     * 用户 id
     */
    private String userId;

    private String userPhone;

    /**
     * 用户地址信息
     */
    private String userAddress;

    /**
     * product id
     */
    private String productId;

    /**
     * goodsSnapshotId
     */
    private String goodsSnapshotId;

    /**
     * goodsInfo 信息 value 值
     */
    private List<String> goodsInfoValueList;

    /**
     * 评论信息
     */
    private String comment;

    /**
     * 上传的图片列表
     */
    private List<String> picUrlList;

    @Transient
    private String goodsName;

    @Transient
    private String goodsNameEn;

    public ProductUserComment() {

    }

    /**
     * 构造函数
     *
     * @param user          用户
     * @param goodsSnapshot 商品快照
     */
    public ProductUserComment(User user, GoodsSnapshot goodsSnapshot, String comment) {
        StringBuffer userAddress = new StringBuffer();
        userAddress.append(user.getProvince()).append(" ")
                .append(user.getCity()).append(" ").append(user.getRegion());

        this.userId = user.getUserId();
        this.userPhone = user.getPhone();
        this.userAddress = userAddress.toString();
        this.productId = goodsSnapshot.getProductId();
        this.goodsSnapshotId = goodsSnapshot.getGoodsSnapshotId();
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getGoodsSnapshotId() {
        return goodsSnapshotId;
    }

    public void setGoodsSnapshotId(String goodsSnapshotId) {
        this.goodsSnapshotId = goodsSnapshotId;
    }

    public List<String> getGoodsInfoValueList() {
        return goodsInfoValueList;
    }

    public void setGoodsInfoValueList(List<String> goodsInfoValueList) {
        this.goodsInfoValueList = goodsInfoValueList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsNameEn() {
        return goodsNameEn;
    }

    public void setGoodsNameEn(String goodsNameEn) {
        this.goodsNameEn = goodsNameEn;
    }

    public List<String> getPicUrlList() {
        return picUrlList;
    }

    public void setPicUrlList(List<String> picUrlList) {
        this.picUrlList = picUrlList;
    }
}
