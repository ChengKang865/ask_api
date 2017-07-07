package com.autoask.entity.mongo.product;

import com.autoask.entity.mongo.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hp on 16-9-24.
 */
@Document
public class ProductBanner extends BaseEntity {
    /**
     * 产品 id
     */
    String productId;

    /**
     * 图片链接
     */
    String picUrl;

    /**
     * 是否上线
     */
    Boolean onLine;

    /**
     * 排序
     */
    Integer priority;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Boolean getOnLine() {
        return onLine;
    }

    public void setOnLine(Boolean onLine) {
        this.onLine = onLine;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
