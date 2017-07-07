package com.autoask.service.express;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.express.MailFree;

/**
 * 快递免邮费
 * @author ck
 *
 * @Create 2017年4月8日上午9:52:32
 */
public interface MailFreeService {
	
	/**
	 * 根据地区查询免邮价格
	 * @param province
	 * @return
	 * @throws ApiException
	 */
	String getByProvince(String province) throws ApiException;
	
	/**
	 * 增加
	 * @param mailFree
	 * @throws ApiException
	 */
	void saveMailFree(MailFree mailFree) throws ApiException;
	
	/**
	 * 修改
	 * @param mailfree
	 * @throws ApiException
	 */
	void updateMailFree(MailFree mailfree) throws ApiException;
}
