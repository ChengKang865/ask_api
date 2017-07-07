package com.autoask.entity.common.weiixn;
/**
 * 子菜单项 :没有子菜单的菜单项，有可能是二级菜单项，也有可能是不含二级菜单的一级菜单
 * @author ck
 *
 * @Create 2017年6月7日下午5:03:14
 */
public class CommonButton extends Button {

	/**
	 * 菜单类型
	 */
	private String type;

	/**
	 * KEY值，最大不超过128字节。
	 */
	private String key;


	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}


}
