package com.autoask.entity.mysql;


import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class OrderShare {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    //广告费
    private BigDecimal adFee;

    //广告费方现在只会有serviceProvider
    private String adType;

    private String adId;

    //服务费
    private BigDecimal serviceFee;

    //serviceType 现在只会对应 serviceProvider
    private String serviceType;

    //现在只对应serviceProviderId
    private String serviceId;

    //手工费现在保留
    private BigDecimal handleFee;

    //修理师傅id
    private String handleId;

    //引流费用
    private BigDecimal promoteFee;

    private String promoteType;

    private String promoteId;

    //工厂费
    private BigDecimal factoryFee;

    private String factoryId;

    private Date createTime;

    private BigDecimal originAdFee;

    private BigDecimal originServiceFee;

//    private BigDecimal originFactoryFee;

    @Transient
    private String promoteName;

    @Transient
    private String handelName;

    @Transient
    private String adName;

    @Transient
    private String serviceName;

    @Transient
    private String factoryName;

    @Transient
    private List<OrderGoodsShare> orderGoodsShareList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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

    public String getPromoteName() {
        return promoteName;
    }

    public void setPromoteName(String promoteName) {
        this.promoteName = promoteName;
    }

    public String getHandelName() {
        return handelName;
    }

    public void setHandelName(String handelName) {
        this.handelName = handelName;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public List<OrderGoodsShare> getOrderGoodsShareList() {
        return orderGoodsShareList;
    }

    public void setOrderGoodsShareList(List<OrderGoodsShare> orderGoodsShareList) {
        this.orderGoodsShareList = orderGoodsShareList;
    }

    public BigDecimal getOriginAdFee() {
        return originAdFee;
    }

    public void setOriginAdFee(BigDecimal originAdFee) {
        this.originAdFee = originAdFee;
    }

    public BigDecimal getOriginServiceFee() {
        return originServiceFee;
    }

    public void setOriginServiceFee(BigDecimal originServiceFee) {
        this.originServiceFee = originServiceFee;
    }

//    public BigDecimal getOriginFactoryFee() {
//        return originFactoryFee;
//    }

//    public void setOriginFactoryFee(BigDecimal originFactoryFee) {
//        this.originFactoryFee = originFactoryFee;
//    }
}