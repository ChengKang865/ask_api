package com.autoask.entity.mongo.product;

import com.autoask.entity.mongo.BaseEntity;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hp on 16-10-15.
 * M站 首页的 product.
 */
@Document
public class IndexProduct extends BaseEntity {
    /**
     * 产品 id
     */
    @Indexed
    private String productId;

    /**
     * 权重
     * 根据weight排序
     */
    private Integer weight;

    /**
     * 标签
     * Constants.IndexProductLabel
     * new_gen
     * classic
     */
    private String label;

    /**
     * 展示的渠道
     * <p>
     * Constants.IndexProductLabel
     * pc h5
     */
    private String channel;


    private String picUrl;

    @Transient
    private String logoUrl;

    @Transient
    private String productName;

    @Transient
    private String productNameEn;

    @Transient
    private String description;

    @Transient
    private String priceStr;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameEn() {
        return productNameEn;
    }

    public void setProductNameEn(String productNameEn) {
        this.productNameEn = productNameEn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }
}
