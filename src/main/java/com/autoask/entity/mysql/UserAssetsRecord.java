package com.autoask.entity.mysql;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table
public class UserAssetsRecord {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recordId;

    private String userId;

    private BigDecimal preAmount;

    private BigDecimal nowAmount;

    private BigDecimal changeAmount;

    private String relatedType;

    private String relatedId;

    private Date createTime;

    public UserAssetsRecord() {

    }

    public UserAssetsRecord(String recordId, String userId, BigDecimal preAmount, BigDecimal changeAmount,
                            String relatedType, String relatedId, Date createTime) {
        this.recordId = recordId;
        this.userId = userId;
        this.preAmount = preAmount;
        this.changeAmount = changeAmount;
        this.nowAmount = preAmount.add(changeAmount);
        this.relatedType = relatedType;
        this.relatedId = relatedId;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId == null ? null : recordId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public BigDecimal getPreAmount() {
        return preAmount;
    }

    public void setPreAmount(BigDecimal preAmount) {
        this.preAmount = preAmount;
    }

    public BigDecimal getNowAmount() {
        return nowAmount;
    }

    public void setNowAmount(BigDecimal nowAmount) {
        this.nowAmount = nowAmount;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType == null ? null : relatedType.trim();
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId == null ? null : relatedId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}