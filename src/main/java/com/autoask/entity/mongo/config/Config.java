package com.autoask.entity.mongo.config;

import com.autoask.entity.mongo.BaseEntity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Config {
	
	@Id
	private String id;
	
	 /**
     *大类
     */
	private String type;
	
	/**
	 * 属性
	 */
	private String name;
	
	/**
	 * 值
	 */
	private String val;
	
	/**
	 * 备注
	 */
	private String mark;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
   
	
	
}
