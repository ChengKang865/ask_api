package com.autoask.entity.mysql;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by hyy on 2016/12/11.
 */
@Entity
@Table
public class AssetsRecordItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String recordItemId;

    private String recordId;

    private BigDecimal amount;

    private String incomeType;

    public AssetsRecordItem() {

    }

    public AssetsRecordItem(String recordItemId, String recordId, BigDecimal amount, String incomeType) {
        this.recordItemId = recordItemId;
        this.recordId = recordId;
        this.amount = amount;
        this.incomeType = incomeType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecordItemId() {
        return recordItemId;
    }

    public void setRecordItemId(String recordItemId) {
        this.recordItemId = recordItemId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }
}
