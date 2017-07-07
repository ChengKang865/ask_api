package com.autoask.entity.mysql;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import javax.persistence.Transient;
/**
 * 购物车下所述商品信息
 * @author ck
 *
 * @Create 2017年5月23日上午9:59:42
 */
@Entity
@Table
public class ShoppingCartGoods {
	
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 用户id
     */
    private String userId;
    
    /**
     * 商品快照id
     */
    private String goodsSnapshotId;
    
    /**
     * 逻辑id
     */
    private String shoppingCartGoodsId;
    
    /**
     * 录入时间
     */
    private Date createTime;
    
    /**
     * 最后修改时间
     */
    private Date lastUpdateTime;
    
    /**
     * 删除标示
     */
    private Boolean deleteFlag;
    
    /**
     * 删除时间
     */
    private Date deleteTime;
    
    /**
     * 商品数量
     */
    private Long shoppingCartNum;
    
    @Transient
    private GoodsSnapshot goodsSnapshot;
    //是否失效
    @Transient
    private Boolean isInvalid;
    //产品类型id
    @Transient
    private String productCategoryId;
    //服务费
    @Transient
    private  BigDecimal categoryServiceFee;
    //产品类型名称
    @Transient
    private String productCategoryName;
    
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getShoppingCartGoodsId() {
		return shoppingCartGoodsId;
	}

	public void setShoppingCartGoodsId(String shoppingCartGoodsId) {
		this.shoppingCartGoodsId = shoppingCartGoodsId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getGoodsSnapshotId() {
		return goodsSnapshotId;
	}

	public void setGoodsSnapshotId(String goodsSnapshotId) {
		this.goodsSnapshotId = goodsSnapshotId;
	}

	public Boolean getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public Long getShoppingCartNum() {
		return shoppingCartNum;
	}

	public void setShoppingCartNum(Long shoppingCartNum) {
		this.shoppingCartNum = shoppingCartNum;
	}

	public GoodsSnapshot getGoodsSnapshot() {
		return goodsSnapshot;
	}

	public void setGoodsSnapshot(GoodsSnapshot goodsSnapshot) {
		this.goodsSnapshot = goodsSnapshot;
	}

	public Boolean getIsInvalid() {
		return isInvalid;
	}

	public void setIsInvalid(Boolean isInvalid) {
		this.isInvalid = isInvalid;
	}

	public String getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public BigDecimal getCategoryServiceFee() {
		return categoryServiceFee;
	}

	public void setCategoryServiceFee(BigDecimal categoryServiceFee) {
		this.categoryServiceFee = categoryServiceFee;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}
	
}
