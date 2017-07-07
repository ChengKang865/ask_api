package com.autoask.entity.mongo.express;

import java.math.BigDecimal;

import com.autoask.entity.mongo.BaseEntity;

/**
 * 设置免邮
 * @author ck
 *
 * @Create 2017年4月8日上午9:45:33
 */
public class MailFree extends BaseEntity{
	/**
	 * 免邮价格
	 */
	private BigDecimal price;
	/**
	 * 不包邮的省 多个以 "," 隔开
	 */
	private String province;
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
}
