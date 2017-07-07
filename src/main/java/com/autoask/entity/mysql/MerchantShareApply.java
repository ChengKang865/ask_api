package com.autoask.entity.mysql;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by weid on 16-11-5.
 */
@Entity
@Table
public class MerchantShareApply {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applyId;

    /**
     * 批次号
     */
    private String serialNo;

    /**
     * 流水号
     */
    private String batchNo;

    /**
     * 支付宝账户
     */
    private String account;

    /**
     * 支付宝账户名称
     */
    private String accountName;

    /**
     * 备注
     */
    private String remark;

    private String merchantId;

    /**
     * 商户类型
     */
    private String merchantType;

    /**
     * 体现额度
     */
    private BigDecimal amount;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 状态
     */
    private String status;

    private Date createTime;

    private Date updateTime;

    /**
     * 拓展字段，用于标识成功还是失败
     */
    @Transient
    private boolean paySuccess;

    @Transient
    private String merchantName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isPaySuccess() {
        return paySuccess;
    }

    public void setPaySuccess(boolean paySuccess) {
        this.paySuccess = paySuccess;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    @Override
    public String toString() {
        return "MerchantShareApply{" +
                "id=" + id +
                ", applyId='" + applyId + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", account='" + account + '\'' +
                ", accountName='" + accountName + '\'' +
                ", remark='" + remark + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", merchantType='" + merchantType + '\'' +
                ", amount=" + amount +
                ", fee=" + fee +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", paySuccess=" + paySuccess +
                '}';
    }
}
