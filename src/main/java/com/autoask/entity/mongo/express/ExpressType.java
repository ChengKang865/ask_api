package com.autoask.entity.mongo.express;

import org.springframework.data.mongodb.core.mapping.Document;

import com.autoask.entity.mongo.BaseEntity;

/**
 * 快递类型
 * @author ck
 *
 * @Create 2017年4月1日下午2:45:26
 */
@Document
public class ExpressType extends BaseEntity{
	/**
	 * 快递类型中文名称
	 */
	private String cnName;
	/**
	 * 英文名称
	 */
	private String ukName;
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getUkName() {
		return ukName;
	}
	public void setUkName(String ukName) {
		this.ukName = ukName;
	}
}
