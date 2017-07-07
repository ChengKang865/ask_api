package com.autoask.entity.mongo.store;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by hyy on 2016/12/3.
 */
@Document
public class GoodsNumRecord {
    @Indexed
    private String id;

    /**
     * 单号
     */
    @NotBlank
    private String serial;

    /**
     * 发货时间
     */
    @NotNull
    private Date recordTime;

    /**
     * true 是 发货
     * false 是 退货
     */
    @NotNull
    private Boolean addType;

    /**
     * 各个商品的数量
     */
    @NotEmpty
    private List<GoodsInfo> goodsInfoList;

    /**
     * 商户类型
     */
    @NotEmpty
    private String merchantType;

    /**
     * 商户id
     */
    @NotEmpty
    private String merchantId;

    /**
     * 创建人类型
     */
    private String creatorType;

    /**
     * 创建人id
     */
    private String creatorId;

    private Date createTime;

    @Transient
    private String merchantName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Boolean getAddType() {
        return addType;
    }

    public void setAddType(Boolean addType) {
        this.addType = addType;
    }

    public List<GoodsInfo> getGoodsInfoList() {
        return goodsInfoList;
    }

    public void setGoodsInfoList(List<GoodsInfo> goodsInfoList) {
        this.goodsInfoList = goodsInfoList;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(String creatorType) {
        this.creatorType = creatorType;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public static class GoodsInfo {
        private String productCategoryName;
        private String productName;
        private String goodsName;
        @Indexed
        private String goodsId;
        private Long num;

        public String getProductCategoryName() {
            return productCategoryName;
        }

        public void setProductCategoryName(String productCategoryName) {
            this.productCategoryName = productCategoryName;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public Long getNum() {
            return num;
        }

        public void setNum(Long num) {
            this.num = num;
        }
    }
}
