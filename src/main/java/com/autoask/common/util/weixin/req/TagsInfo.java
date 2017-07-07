package com.autoask.common.util.weixin.req;
/**
 * 标签
 * @author ck
 *
 * @Create 2017年6月20日上午9:36:38
 */
public class TagsInfo {
	/**
	 * 标签id
	 */
	private Integer id;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 总数
	 */
	private Integer count;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
