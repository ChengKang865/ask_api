package com.autoask.entity.common;
/**
 * 快递缓存信息
 * @author ck
 *
 * @Create 2017年3月29日下午3:34:41
 */
public class Express {
	/**
	 * 快递单号
	 */
	private String oddNumber;
	/**
	 * 信息内容
	 */
	private Object content;
	/**
	 * 最后更新时间
	 */
	private long timeOut;
	
	public String getOddNumber() {
		return oddNumber;
	}
	public void setOddNumber(String oddNumber) {
		this.oddNumber = oddNumber;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public long getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}
    
}
