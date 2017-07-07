package com.autoask.entity.mysql;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class CardType {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardTypeId;

    @NotBlank
    private String name;

    @Min(1)
    @NotNull
    private Long num;

    /**
     * 卡的状态： to_check checked
     */
    private String status;


    /**
     * 工厂分成比例
     */
    @NotNull
    @Min(0)
    private BigDecimal factoryFee;

    /**
     * 广告费        只会分给修理厂
     */
    @NotNull
    @Min(0)
    private BigDecimal adFee;


    /**
     * 修理工分成比例
     */
    @NotNull
    @Min(0)
    private BigDecimal handleFee;

    /**
     * 引流费分成比例
     */
    @NotNull
    @Min(0)
    private BigDecimal promoteFee;



    /**
     * 删除标志位 废弃不用
     */
    private Boolean deleteFlag;

    private Date createTime;

    @NotNull
    private Date expireTime;

    private String creatorId;

    private Date modifyTime;

    private String modifyId;

    @Transient
    @Valid
    private List<GoodsCardType> goodsCardTypeList;

    /**
     * 对应的兑换的商品列表的名称拼接
     */
    @Transient
    private String bindGoodsNames;

    /**
     * 统计使用数量
     */
    @Transient
    private int useNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
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

    public List<GoodsCardType> getGoodsCardTypeList() {
        return goodsCardTypeList;
    }

    public void setGoodsCardTypeList(List<GoodsCardType> goodsCardTypeList) {
        this.goodsCardTypeList = goodsCardTypeList;
    }

    public String getBindGoodsNames() {
        return bindGoodsNames;
    }

    public void setBindGoodsNames(String bindGoodsNames) {
        this.bindGoodsNames = bindGoodsNames;
    }

    public int getUseNum() {
        return useNum;
    }

    public void setUseNum(int useNum) {
        this.useNum = useNum;
    }
}