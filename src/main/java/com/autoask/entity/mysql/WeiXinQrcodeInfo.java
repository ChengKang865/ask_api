package com.autoask.entity.mysql;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 微信二维码关注来源
 * @author ck
 *
 * @Create 2017年6月8日下午2:43:31
 */
public class WeiXinQrcodeInfo{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编号
	 */
	private String qrCodeInfoId;
	/**
	 * 二维码类型
	 */
	private String type;
	/**
	 * 二维码失效时间
	 */
	private Long expireSeconds;
	/**
	 * 录入时间
	 */
	private Date createDate;
	
	/**
	 * 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
	 */
	private String url;
	/**
	 * 订阅次数
	 */
	private Long subscribeCount;
	/**
	 * 创建二维码时间
	 */
	private Long qrCreateTime;
	/**
	 * 删除标示
	 */
	private Boolean deleteFlag;

	/**
	 * 换取二维码的tick
	 */
	private String ticket;
	/**
	 * 订阅占总订阅百分比
	 */
    @Transient
    private String subscribePercentage;
    
    /**
     * 二维码类型名称
     */
    @Transient
    private String typeName;
    /**
     * 二维码换取地址
     */
    @Transient
    private String imgUrl;
    /**
     * 是否失效
     */
    @Transient
    private String expireSecondsName;
    
    
	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQrCodeInfoId() {
		return qrCodeInfoId;
	}

	public void setQrCodeInfoId(String qrCodeInfoId) {
		this.qrCodeInfoId = qrCodeInfoId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getExpireSeconds() {
		return expireSeconds;
	}

	public void setExpireSeconds(Long expireSeconds) {
		this.expireSeconds = expireSeconds;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public Long getSubscribeCount() {
		return subscribeCount;
	}

	public void setSubscribeCount(Long subscribeCount) {
		this.subscribeCount = subscribeCount;
	}

	public String getSubscribePercentage() {
		return subscribePercentage;
	}

	public void setSubscribePercentage(String subscribePercentage) {
		this.subscribePercentage = subscribePercentage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQrCreateTime() {
		return qrCreateTime;
	}

	public void setQrCreateTime(Long qrCreateTime) {
		this.qrCreateTime = qrCreateTime;
	}

	public Boolean getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getExpireSecondsName() {
		return expireSecondsName;
	}

	public void setExpireSecondsName(String expireSecondsName) {
		this.expireSecondsName = expireSecondsName;
	}
	
}
