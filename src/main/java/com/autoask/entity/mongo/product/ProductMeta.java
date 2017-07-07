package com.autoask.entity.mongo.product;

import com.autoask.entity.mongo.BaseEntity;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @auth hyy
 * @
 */
@Document
public class ProductMeta extends BaseEntity {

    /**
     * 对应 mysql中的 product product_id
     */
    @Indexed
    private String productId;

    /**
     * 产品图片
     */
    private List<String> picUrlList;

    /**
     * 参数图片
     */
    private List<String> infoUrlList;

    private List<String> pcBuyUrlList;

    private List<String> h5BuyUrlList;

    private String h5DetailPicture;

    private List<Meta> metaList;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<String> getPicUrlList() {
        return picUrlList;
    }

    public void setPicUrlList(List<String> picUrlList) {
        this.picUrlList = picUrlList;
    }

    public List<String> getInfoUrlList() {
        return infoUrlList;
    }

    public void setInfoUrlList(List<String> infoUrlList) {
        this.infoUrlList = infoUrlList;
    }

    public List<String> getPcBuyUrlList() {
        return pcBuyUrlList;
    }

    public void setPcBuyUrlList(List<String> pcBuyUrlList) {
        this.pcBuyUrlList = pcBuyUrlList;
    }

    public List<String> getH5BuyUrlList() {
        return h5BuyUrlList;
    }

    public void setH5BuyUrlList(List<String> h5BuyUrlList) {
        this.h5BuyUrlList = h5BuyUrlList;
    }

    public String getH5DetailPicture() {
        return h5DetailPicture;
    }

    public void setH5DetailPicture(String h5DetailPicture) {
        this.h5DetailPicture = h5DetailPicture;
    }

    public List<Meta> getMetaList() {
        return metaList;
    }

    public void setMetaList(List<Meta> metaList) {
        this.metaList = metaList;
    }




}
