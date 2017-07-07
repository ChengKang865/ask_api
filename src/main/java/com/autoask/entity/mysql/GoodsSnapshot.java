package com.autoask.entity.mysql;

import com.autoask.entity.mongo.product.GoodsSnapshotMeta;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class GoodsSnapshot {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String goodsSnapshotId;

    private String goodsId;

    private String productId;

    private String name;

    private String nameEn;

    private Integer popularRank;

    private BigDecimal onlinePrice;

    private BigDecimal offlinePrice;

    private String type;

    private String status;

    private String factoryId;

    private BigDecimal factoryFee;

    private BigDecimal adFee;

    private BigDecimal handleFee;

    private BigDecimal promoteFee;

    private Date createTime;

    private String creatorId;

    private Date modifyTime;

    private String modifyId;

    private Date checkTime;

    private String checkId;

    private Boolean deleteFlag;

    /**
     * checkStatus 是需要后台验证的
     */
    private String snapshotCheckStatus;

    private Date snapshotCreateTime;

    /**
     * 重量
     */
    private BigDecimal weight;

    @Transient
    private List<GoodsSnapshotInfo> goodsSnapshotInfoList;

    @Transient
    private Product product;

    @Transient
    private GoodsSnapshotMeta goodsSnapshotMeta;

    @Transient
    private Boolean goodsSaleFlag;

    public GoodsSnapshot() {

    }

    public GoodsSnapshot(Goods goods) {
        this.setGoodsId(goods.getGoodsId());
        this.setProductId(goods.getProductId());
        this.setName(goods.getName());
        this.setNameEn(goods.getNameEn());
        this.setPopularRank(goods.getPopularRank());
        this.setOnlinePrice(goods.getOnlinePrice());
        this.setOfflinePrice(goods.getOfflinePrice());
        this.setType(goods.getType());
        this.setStatus(goods.getStatus());
        this.setFactoryId(goods.getFactoryId());
        this.setFactoryFee(goods.getFactoryFee());
        this.setAdFee(goods.getAdFee());
        this.setHandleFee(goods.getHandleFee());
        this.setPromoteFee(goods.getPromoteFee());
        this.setCreateTime(goods.getCreateTime());
        this.setCreatorId(goods.getCreatorId());
        this.setModifyTime(goods.getModifyTime());
        this.setModifyId(goods.getModifyId());
        this.setCheckTime(goods.getCheckTime());
        this.setCheckId(goods.getCheckId());
        this.setDeleteFlag(goods.getDeleteFlag());
        this.setWeight(goods.getWeight());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsSnapshotId() {
        return goodsSnapshotId;
    }

    public void setGoodsSnapshotId(String goodsSnapshotId) {
        this.goodsSnapshotId = goodsSnapshotId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Integer getPopularRank() {
        return popularRank;
    }

    public void setPopularRank(Integer popularRank) {
        this.popularRank = popularRank;
    }

    public BigDecimal getOnlinePrice() {
        return onlinePrice;
    }

    public void setOnlinePrice(BigDecimal onlinePrice) {
        this.onlinePrice = onlinePrice;
    }

    public BigDecimal getOfflinePrice() {
        return offlinePrice;
    }

    public void setOfflinePrice(BigDecimal offlinePrice) {
        this.offlinePrice = offlinePrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public BigDecimal getFactoryFee() {
        return factoryFee;
    }

    public void setFactoryFee(BigDecimal factoryFee) {
        this.factoryFee = factoryFee;
    }

    public BigDecimal getAdFee() {
        return adFee;
    }

    public void setAdFee(BigDecimal adFee) {
        this.adFee = adFee;
    }

    public BigDecimal getHandleFee() {
        return handleFee;
    }

    public void setHandleFee(BigDecimal handleFee) {
        this.handleFee = handleFee;
    }

    public BigDecimal getPromoteFee() {
        return promoteFee;
    }

    public void setPromoteFee(BigDecimal promoteFee) {
        this.promoteFee = promoteFee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getSnapshotCheckStatus() {
        return snapshotCheckStatus;
    }

    public void setSnapshotCheckStatus(String snapshotCheckStatus) {
        this.snapshotCheckStatus = snapshotCheckStatus;
    }

    public Date getSnapshotCreateTime() {
        return snapshotCreateTime;
    }

    public void setSnapshotCreateTime(Date snapshotCreateTime) {
        this.snapshotCreateTime = snapshotCreateTime;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public List<GoodsSnapshotInfo> getGoodsSnapshotInfoList() {
        return goodsSnapshotInfoList;
    }

    public void setGoodsSnapshotInfoList(List<GoodsSnapshotInfo> goodsSnapshotInfoList) {
        this.goodsSnapshotInfoList = goodsSnapshotInfoList;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public GoodsSnapshotMeta getGoodsSnapshotMeta() {
        return goodsSnapshotMeta;
    }

    public void setGoodsSnapshotMeta(GoodsSnapshotMeta goodsSnapshotMeta) {
        this.goodsSnapshotMeta = goodsSnapshotMeta;
    }

    public Boolean getGoodsSaleFlag() {
        return goodsSaleFlag;
    }

    public void setGoodsSaleFlag(Boolean goodsSaleFlag) {
        this.goodsSaleFlag = goodsSaleFlag;
    }
}