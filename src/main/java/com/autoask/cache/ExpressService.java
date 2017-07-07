package com.autoask.cache;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.common.Express;

/**
 * 快递信息
 * @author ck
 *
 * @Create 2017年3月29日下午3:56:18
 */
public interface ExpressService {
	/**
	 * 查询
	 * @param oddNumber 快递单号
	 * @param com 快递类型  auto为所有类型
	 * @throws ApiException 
	 */
	Express queryExpress(String oddNumber,String com) throws ApiException;
	
}
