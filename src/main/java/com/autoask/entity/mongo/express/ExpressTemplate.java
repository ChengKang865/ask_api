package com.autoask.entity.mongo.express;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Document;

import com.autoask.entity.mongo.BaseEntity;

/**
 * 快递价格模板信息实体类
 * @author ck
 *
 * @Create 2017年3月31日下午4:41:45
 */
@Document
public class ExpressTemplate extends BaseEntity{
	/**
	 * 模板名称
	 */
	private String name;
	/**
	 * 快递类型Id
	 */
	private String expressTypeId;
	/**
	 * 模板名称
	 */
	private String expressTypeName;
	/**
	 * 运送范围
	 */
	private String range;
	/**
	 * 首重
	 */
	private BigDecimal firstHeavy;
	/**
	 * 首重价格
	 */
	private BigDecimal firstHeavyPrice;
	/**
	 * 续重
	 */
	private BigDecimal continuedHeavy;
	/**
	 * 续重价格
	 */
	private BigDecimal continuedHeavyPrice;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public BigDecimal getFirstHeavy() {
		return firstHeavy;
	}
	public void setFirstHeavy(BigDecimal firstHeavy) {
		this.firstHeavy = firstHeavy;
	}
	public BigDecimal getFirstHeavyPrice() {
		return firstHeavyPrice;
	}
	public void setFirstHeavyPrice(BigDecimal firstHeavyPrice) {
		this.firstHeavyPrice = firstHeavyPrice;
	}
	public BigDecimal getContinuedHeavy() {
		return continuedHeavy;
	}
	public void setContinuedHeavy(BigDecimal continuedHeavy) {
		this.continuedHeavy = continuedHeavy;
	}
	public BigDecimal getContinuedHeavyPrice() {
		return continuedHeavyPrice;
	}
	public void setContinuedHeavyPrice(BigDecimal continuedHeavyPrice) {
		this.continuedHeavyPrice = continuedHeavyPrice;
	}
	public String getExpressTypeId() {
		return expressTypeId;
	}
	public void setExpressTypeId(String expressTypeId) {
		this.expressTypeId = expressTypeId;
	}
	public String getExpressTypeName() {
		return expressTypeName;
	}
	public void setExpressTypeName(String expressTypeName) {
		this.expressTypeName = expressTypeName;
	}
}
