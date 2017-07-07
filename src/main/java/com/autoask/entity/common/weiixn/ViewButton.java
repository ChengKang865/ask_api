package com.autoask.entity.common.weiixn;
/**
 * 二级菜单
 * @author ck
 *
 * @Create 2017年6月7日下午5:06:19
 */
public class ViewButton extends Button {

	/**
	 * 菜单类型
	 */
	private String type;

	/**
	 * view路径值
	 */
	private String url;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
