package com.autoask.entity.mysql;

import com.autoask.common.util.Constants;
import com.autoask.entity.mongo.product.GoodsMeta;
import com.autoask.entity.mongo.product.GoodsSnapshotMeta;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class Goods {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String goodsId;

    private String goodsSnapshotId;

    private String productId;

    private String name;

    private String nameEn;

    private Integer popularRank;

    private BigDecimal onlinePrice;

    private BigDecimal offlinePrice;

    /**
     * 销售方式
     * all online offline
     * 现在只能是all 跟 online
     */
    private String type;

    /**
     * 审核状态
     * to_check: 待审核 / checked: 审核通过  failure: 未通过审核
     */
    private String status;

    /**
     * 在售状态
     * True: 在售 / False: 下线
     */
    private Boolean saleFlag;

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
     * 等待审核的商品快照id
     */
    private String checkSnapshotId;

    /**
     * 重量
     */
    private BigDecimal weight;

    @Transient
    private Product product;

    @Transient
    private GoodsSnapshot goodsSnapshot;

    public Goods() {
    }

    public Goods(GoodsSnapshot goodsSnapshot) {
        // 生成商品的快照时,即已生成商品的id号
        this.setGoodsId(goodsSnapshot.getGoodsId());
        this.setGoodsSnapshotId(goodsSnapshot.getGoodsSnapshotId());
        this.setDeleteFlag(goodsSnapshot.getDeleteFlag());
        this.setStatus(goodsSnapshot.getStatus());
        this.setProductId(goodsSnapshot.getProductId());
        this.setName(goodsSnapshot.getName());
        this.setNameEn(goodsSnapshot.getNameEn());
        this.setPopularRank(goodsSnapshot.getPopularRank());
        this.setOnlinePrice(goodsSnapshot.getOnlinePrice());
        this.setOfflinePrice(goodsSnapshot.getOfflinePrice());
        this.setType(goodsSnapshot.getType());
        this.setFactoryId(goodsSnapshot.getFactoryId());
        this.setFactoryFee(goodsSnapshot.getFactoryFee());
        this.setAdFee(goodsSnapshot.getAdFee());
        this.setHandleFee(goodsSnapshot.getHandleFee());
        this.setPromoteFee(goodsSnapshot.getPromoteFee());
        this.setCreateTime(goodsSnapshot.getCreateTime());
        this.setCreatorId(goodsSnapshot.getCreatorId());
        this.setModifyTime(goodsSnapshot.getModifyTime());
        this.setModifyId(goodsSnapshot.getModifyId());
        this.setCheckTime(goodsSnapshot.getCheckTime());
        this.setCheckId(goodsSnapshot.getCheckId());
        this.setWeight(goodsSnapshot.getWeight());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsSnapshotId() {
        return goodsSnapshotId;
    }

    public void setGoodsSnapshotId(String goodsSnapshotId) {
        this.goodsSnapshotId = goodsSnapshotId;
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

    public Boolean getSaleFlag() {
        return saleFlag;
    }

    public void setSaleFlag(Boolean saleFlag) {
        this.saleFlag = saleFlag;
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

    public String getCheckSnapshotId() {
        return checkSnapshotId;
    }

    public void setCheckSnapshotId(String checkSnapshotId) {
        this.checkSnapshotId = checkSnapshotId;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public GoodsSnapshot getGoodsSnapshot() {
        return goodsSnapshot;
    }

    public void setGoodsSnapshot(GoodsSnapshot goodsSnapshot) {
        this.goodsSnapshot = goodsSnapshot;
    }
}