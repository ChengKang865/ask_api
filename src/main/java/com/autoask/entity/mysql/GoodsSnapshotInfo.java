package com.autoask.entity.mysql;

import com.autoask.common.util.CodeGenerator;

import javax.persistence.*;

@Entity
@Table
public class GoodsSnapshotInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String goodsSnapshotInfoId;

    private String goodsSnapshotId;

    private String keyName;

    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsSnapshotInfoId() {
        return goodsSnapshotInfoId;
    }

    public void setGoodsSnapshotInfoId(String goodsSnapshotInfoId) {
        this.goodsSnapshotInfoId = goodsSnapshotInfoId == null ? null : goodsSnapshotInfoId.trim();
    }

    public String getGoodsSnapshotId() {
        return goodsSnapshotId;
    }

    public void setGoodsSnapshotId(String goodsSnapshotId) {
        this.goodsSnapshotId = goodsSnapshotId == null ? null : goodsSnapshotId.trim();
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName == null ? null : keyName.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public static GoodsSnapshotInfo generatorNew(GoodsInfo goodsInfo, String goodsSnapshotId) {
        GoodsSnapshotInfo goodsSnapshotInfo = new GoodsSnapshotInfo();
        goodsSnapshotInfo.setGoodsSnapshotInfoId(CodeGenerator.uuid());
        goodsSnapshotInfo.setGoodsSnapshotId(goodsSnapshotId);
        goodsSnapshotInfo.setKeyName(goodsInfo.getKeyName());
        goodsSnapshotInfo.setValue(goodsInfo.getValue());
        return goodsSnapshotInfo;
    }
}