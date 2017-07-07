package com.autoask.entity.common.weiixn;
/**
 * 图文消息信息
 * @author ck
 *
 * @Create 2017年6月8日上午10:33:19
 */
public class NewsItem {
	/**
	 * 头信息
	 */
	private String title;
	/**
	 * 封面显示内容
	 */
	private String digest;
	/**
	 * 封面图片id
	 */
	private String thumbMediaId;
	
	/**
	 * 图片地址
	 */
	private String thumbUrl;
	
	/**
	 * 图文文章地址
	 */
	private String url;
	
	/**
	 * 作者
	 */
	private String author;
	
	/**
	 * 是否显示封面
	 */
	private Integer showCoverPic;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getShowCoverPic() {
		return showCoverPic;
	}

	public void setShowCoverPic(Integer showCoverPic) {
		this.showCoverPic = showCoverPic;
	}

}
