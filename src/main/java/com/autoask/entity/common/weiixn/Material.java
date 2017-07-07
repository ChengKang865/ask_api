package com.autoask.entity.common.weiixn;

import java.util.List;

/**
 * 素材信息
 * @author ck
 *
 * @Create 2017年6月7日下午5:02:58
 */
public class Material {
	
	/**
	 * 所属id
	 */
	private String mediaId;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 更新时间
	 */
	private Long updateTime;
	
	/**
	 * 类型
	 */
	private String type;
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 图片地址
	 */
	private String url;
	
	/**
	 * 图文信息列表
	 */
	private List<NewsItem> list;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<NewsItem> getList() {
		return list;
	}

	public void setList(List<NewsItem> list) {
		this.list = list;
	}
	
}
