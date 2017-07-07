package com.autoask.common.util.weixin;

/**
 * 微信接入客服
 * 
 * @author ck
 *
 * @Create 2017年6月13日下午3:21:52
 */
public class MessageTransInfo {
	private String ToUserName;
	private String FromUserName;
	private long CreateTime;
	private String MsgType;
	private KKTransInfo TransInfo;

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public KKTransInfo getTransInfo() {
		return TransInfo;
	}

	public void setTransInfo(KKTransInfo transInfo) {
		TransInfo = transInfo;
	}
}
