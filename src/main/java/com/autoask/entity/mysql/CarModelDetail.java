package com.autoask.entity.mysql;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table
public class CarModelDetail {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer sourceId;

    private Integer checkerRuntimeId;

    private String oldDmodel;

    private String detailModel;

    private String detailModelSlug;

    private BigDecimal priceBn;

    private BigDecimal contVprice;

    private String url;

    private String globalSlug;

    private String domain;

    private String status;

    private Integer year;

    private String hasParam;

    private BigDecimal volume;

    private BigDecimal vv;

    private Integer listedYear;

    private Integer delistedYear;

    private String control;

    private String emissionStandard;

    private String volumeExtend;

    private String simpleModel;

    private Integer continuityId;

    private String bodyModel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getCheckerRuntimeId() {
        return checkerRuntimeId;
    }

    public void setCheckerRuntimeId(Integer checkerRuntimeId) {
        this.checkerRuntimeId = checkerRuntimeId;
    }

    public String getOldDmodel() {
        return oldDmodel;
    }

    public void setOldDmodel(String oldDmodel) {
        this.oldDmodel = oldDmodel == null ? null : oldDmodel.trim();
    }

    public String getDetailModel() {
        return detailModel;
    }

    public void setDetailModel(String detailModel) {
        this.detailModel = detailModel == null ? null : detailModel.trim();
    }

    public String getDetailModelSlug() {
        return detailModelSlug;
    }

    public void setDetailModelSlug(String detailModelSlug) {
        this.detailModelSlug = detailModelSlug == null ? null : detailModelSlug.trim();
    }

    public BigDecimal getPriceBn() {
        return priceBn;
    }

    public void setPriceBn(BigDecimal priceBn) {
        this.priceBn = priceBn;
    }

    public BigDecimal getContVprice() {
        return contVprice;
    }

    public void setContVprice(BigDecimal contVprice) {
        this.contVprice = contVprice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getGlobalSlug() {
        return globalSlug;
    }

    public void setGlobalSlug(String globalSlug) {
        this.globalSlug = globalSlug == null ? null : globalSlug.trim();
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain == null ? null : domain.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getHasParam() {
        return hasParam;
    }

    public void setHasParam(String hasParam) {
        this.hasParam = hasParam == null ? null : hasParam.trim();
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getVv() {
        return vv;
    }

    public void setVv(BigDecimal vv) {
        this.vv = vv;
    }

    public Integer getListedYear() {
        return listedYear;
    }

    public void setListedYear(Integer listedYear) {
        this.listedYear = listedYear;
    }

    public Integer getDelistedYear() {
        return delistedYear;
    }

    public void setDelistedYear(Integer delistedYear) {
        this.delistedYear = delistedYear;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control == null ? null : control.trim();
    }

    public String getEmissionStandard() {
        return emissionStandard;
    }

    public void setEmissionStandard(String emissionStandard) {
        this.emissionStandard = emissionStandard == null ? null : emissionStandard.trim();
    }

    public String getVolumeExtend() {
        return volumeExtend;
    }

    public void setVolumeExtend(String volumeExtend) {
        this.volumeExtend = volumeExtend == null ? null : volumeExtend.trim();
    }

    public String getSimpleModel() {
        return simpleModel;
    }

    public void setSimpleModel(String simpleModel) {
        this.simpleModel = simpleModel == null ? null : simpleModel.trim();
    }

    public Integer getContinuityId() {
        return continuityId;
    }

    public void setContinuityId(Integer continuityId) {
        this.continuityId = continuityId;
    }

    public String getBodyModel() {
        return bodyModel;
    }

    public void setBodyModel(String bodyModel) {
        this.bodyModel = bodyModel == null ? null : bodyModel.trim();
    }
}