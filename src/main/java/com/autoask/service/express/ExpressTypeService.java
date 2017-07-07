package com.autoask.service.express;

import java.util.List;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CheckBoxUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mongo.express.ExpressType;

/**
 * 快递类型
 * @author ck
 *
 * @Create 2017年4月1日下午2:53:35
 */
public interface ExpressTypeService {
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	ExpressType getById(String id) throws ApiException;
	
	/**
	 * list列表信息
	 * @param cnName 中文名称
	 * @param ukName 英文名称
	 * @param start
	 * @param limit
	 * @return
	 * @throws ApiException
	 */
	ListSlice<ExpressType> getExpressTypeList(String cnName,String ukName,Integer start, Integer limit)throws ApiException;
	
	/**
	 * 没有分页的list列表
	 * @return
	 * @throws ApiException
	 */
	ListSlice<ExpressType> getNotPageExpressTypeList()throws ApiException;
	
	/**
	 * 增加
	 * @param ExpressType
	 * @throws ApiException
	 */
	void saveExpressType(ExpressType expressType) throws ApiException;
	
	/**
	 * 更新
	 * @param expressTemplate
	 * @throws ApiException
	 */
    void updateExpressType(ExpressType expressType) throws ApiException;
    
    /**
     * 删除
     * @param id 根据id删除
     * @throws ApiException
     */
    void deleteExpressTypeById(String id) throws ApiException;
    
}
