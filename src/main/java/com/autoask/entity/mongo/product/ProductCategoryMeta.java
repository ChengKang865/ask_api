package com.autoask.entity.mongo.product;

import com.autoask.entity.mongo.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @auth hyy
 * @
 */
@Document
public class ProductCategoryMeta extends BaseEntity {

    /**
     * 对应mysql中的product_category 中的 product_category_id
     */
    private String productCategoryId;

    private String picUrl;

    private List<Meta> metaList;

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public List<Meta> getMetaList() {
        return metaList;
    }

    public void setMetaList(List<Meta> metaList) {
        this.metaList = metaList;
    }
}
