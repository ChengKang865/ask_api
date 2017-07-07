package com.autoask.entity.mongo.product;

import com.autoask.entity.mongo.BaseEntity;

import java.util.List;

/**
 * @author hyy
 * @create 2016-08-07 16:39
 */
public class GoodsSnapshotMeta extends BaseEntity {

    private String goodsSnapshotId;

    /**
     * 多个图片 url
     */
    private List<String> picUrlList;

    public GoodsSnapshotMeta() {

    }

    public GoodsSnapshotMeta(GoodsMeta goodsMeta, String goodsSnapshotId) {
        this.setGoodsSnapshotId(goodsSnapshotId);
        this.setPicUrlList(goodsMeta.getPicList());
    }

    public String getGoodsSnapshotId() {
        return goodsSnapshotId;
    }

    public void setGoodsSnapshotId(String goodsSnapshotId) {
        this.goodsSnapshotId = goodsSnapshotId;
    }

    public List<String> getPicUrlList() {
        return picUrlList;
    }

    public void setPicUrlList(List<String> picUrlList) {
        this.picUrlList = picUrlList;
    }
}
