package com.autoask.entity.common.weiixn;
/**
 * 微信二维码信息
 * @author ck
 *
 * @Create 2017年6月8日下午2:15:35
 */
public class WeiXinQRCode {
	/**
	 * 二维码ticket，凭借此ticket可以在有效时间内换取二维码
	 */
	private String ticket;
	
	/**
	 * 效时间，以秒为单位。 最大不超过2592000（即30天）。
	 */
	private Long expireSeconds;
	
	/**
	 * 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
	 */
	private String url;
	/**
	 * 二维码类型
	 */
	private String type;
	/**
	 * 创建时间
	 */
	private Long createTime;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Long getExpireSeconds() {
		return expireSeconds;
	}

	public void setExpireSeconds(Long expireSeconds) {
		this.expireSeconds = expireSeconds;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
}
