package com.autoask.entity.mysql;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table
public class OrderGoodsShare {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderGoodsId;

    private String orderId;

    //广告费现在只会给修理厂
    private BigDecimal adFee;

    private String adType;

    private String adId;

    //暂且保留
    private BigDecimal handleFee;

    private String handleId;

    private BigDecimal promoteFee;

    private String promoteType;

    private String promoteId;

    private BigDecimal factoryFee;

    private String factoryId;

    private Date createTime;

    //原始广告费用
    private BigDecimal originAdFee;

//    private BigDecimal originFactoryFee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderGoodsId() {
        return orderGoodsId;
    }

    public void setOrderGoodsId(String orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAdFee() {
        return adFee;
    }

    public void setAdFee(BigDecimal adFee) {
        this.adFee = adFee;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public BigDecimal getHandleFee() {
        return handleFee;
    }

    public void setHandleFee(BigDecimal handleFee) {
        this.handleFee = handleFee;
    }

    public String getHandleId() {
        return handleId;
    }

    public void setHandleId(String handleId) {
        this.handleId = handleId;
    }

    public BigDecimal getPromoteFee() {
        return promoteFee;
    }

    public void setPromoteFee(BigDecimal promoteFee) {
        this.promoteFee = promoteFee;
    }

    public String getPromoteType() {
        return promoteType;
    }

    public void setPromoteType(String promoteType) {
        this.promoteType = promoteType;
    }

    public String getPromoteId() {
        return promoteId;
    }

    public void setPromoteId(String promoteId) {
        this.promoteId = promoteId;
    }

    public BigDecimal getFactoryFee() {
        return factoryFee;
    }

    public void setFactoryFee(BigDecimal factoryFee) {
        this.factoryFee = factoryFee;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getOriginAdFee() {
        return originAdFee;
    }

    public void setOriginAdFee(BigDecimal originAdFee) {
        this.originAdFee = originAdFee;
    }

//    public BigDecimal getOriginFactoryFee() {
//        return originFactoryFee;
//    }

//    public void setOriginFactoryFee(BigDecimal originFactoryFee) {
//        this.originFactoryFee = originFactoryFee;
//    }
}