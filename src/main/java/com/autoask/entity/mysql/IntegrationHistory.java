package com.autoask.entity.mysql;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class IntegrationHistory {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String integrationHistoryId;

    private String userId;

    private Long preIntegration;

    private Long currentIntegration;

    private Long changeAmount;

    private String type;

    private String extraId;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntegrationHistoryId() {
        return integrationHistoryId;
    }

    public void setIntegrationHistoryId(String integrationHistoryId) {
        this.integrationHistoryId = integrationHistoryId == null ? null : integrationHistoryId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Long getPreIntegration() {
        return preIntegration;
    }

    public void setPreIntegration(Long preIntegration) {
        this.preIntegration = preIntegration;
    }

    public Long getCurrentIntegration() {
        return currentIntegration;
    }

    public void setCurrentIntegration(Long currentIntegration) {
        this.currentIntegration = currentIntegration;
    }

    public Long getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Long changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getExtraId() {
        return extraId;
    }

    public void setExtraId(String extraId) {
        this.extraId = extraId == null ? null : extraId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}