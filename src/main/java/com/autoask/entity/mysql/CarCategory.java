package com.autoask.entity.mysql;

import javax.persistence.*;
import java.math.BigDecimal;

@Table
@Entity
public class CarCategory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer sourceId;

    private String name;

    private String alias;

    private String slug;

    private String url;

    private String parent;

    private Integer checkerRuntimeId;

    private String keywords;

    private String classified;

    private String classifiedUrl;

    private String slugGlobal;

    private String logoImg;

    private String mum;

    private String firstLetter;

    private Integer hasDetailModel;

    private BigDecimal startingPrice;

    private String classifiedSlug;

    private String thumbnail;

    private String pinyin;

    private String status;

    private String attribute;

    private Integer units;

    private String popular;

    private Boolean onSale;

    private Integer score;

    private String normalizedName;

    private String brandArea;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug == null ? null : slug.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent == null ? null : parent.trim();
    }

    public Integer getCheckerRuntimeId() {
        return checkerRuntimeId;
    }

    public void setCheckerRuntimeId(Integer checkerRuntimeId) {
        this.checkerRuntimeId = checkerRuntimeId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }

    public String getClassified() {
        return classified;
    }

    public void setClassified(String classified) {
        this.classified = classified == null ? null : classified.trim();
    }

    public String getClassifiedUrl() {
        return classifiedUrl;
    }

    public void setClassifiedUrl(String classifiedUrl) {
        this.classifiedUrl = classifiedUrl == null ? null : classifiedUrl.trim();
    }

    public String getSlugGlobal() {
        return slugGlobal;
    }

    public void setSlugGlobal(String slugGlobal) {
        this.slugGlobal = slugGlobal == null ? null : slugGlobal.trim();
    }

    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg == null ? null : logoImg.trim();
    }

    public String getMum() {
        return mum;
    }

    public void setMum(String mum) {
        this.mum = mum == null ? null : mum.trim();
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter == null ? null : firstLetter.trim();
    }

    public Integer getHasDetailmodel() {
        return hasDetailModel;
    }

    public void setHasDetailModel(Integer hasDetailmodel) {
        this.hasDetailModel = hasDetailmodel;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getClassifiedSlug() {
        return classifiedSlug;
    }

    public void setClassifiedSlug(String classifiedSlug) {
        this.classifiedSlug = classifiedSlug == null ? null : classifiedSlug.trim();
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail == null ? null : thumbnail.trim();
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin == null ? null : pinyin.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute == null ? null : attribute.trim();
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public String getPopular() {
        return popular;
    }

    public void setPopular(String popular) {
        this.popular = popular == null ? null : popular.trim();
    }

    public Boolean getOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName == null ? null : normalizedName.trim();
    }

    public String getBrandArea() {
        return brandArea;
    }

    public void setBrandArea(String brandArea) {
        this.brandArea = brandArea == null ? null : brandArea.trim();
    }
}