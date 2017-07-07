package com.autoask.common.util;
/**
 * checkBox公共类
 * @author ck
 *
 * @Create 2017年4月14日下午3:32:50
 */
public class CheckBoxUtil {
	/**
	 * id
	 */
	private Integer id;
	/**
	 * 名称 多个使用"," 隔开
	 */
	private String name;
	/**
	 * 是否选择 
	 */
	private Boolean isChoice;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getIsChoice() {
		return isChoice;
	}
	public void setIsChoice(Boolean isChoice) {
		this.isChoice = isChoice;
	}
	
}
