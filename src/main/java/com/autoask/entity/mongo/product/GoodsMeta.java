package com.autoask.entity.mongo.product;

import com.autoask.entity.mongo.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 商品的其他信息 全部存放在mongodb 中
 */
@Document
public class GoodsMeta extends BaseEntity {

    private String goodsId;

    /**
     * 多个图片 url
     */
    private List<String> picList;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }
}
