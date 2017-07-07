package com.autoask.service.express;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CheckBoxUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mongo.express.ExpressTemplate;

/**
 * 快递模板信息
 * @author ck
 *
 * @Create 2017年3月31日下午5:27:44
 */
public interface ExpressTemplateService {
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	ExpressTemplate getById(String id) throws ApiException;
	/**
	 * list列表信息
	 * @param name 模板名称
	 * @param expressType 模板类型
	 * @param start
	 * @param limit
	 * @return
	 * @throws ApiException
	 */
	ListSlice<ExpressTemplate> getExpressTemplateList(String name,String expressTypeId,Integer start, Integer limit)throws ApiException;
	/**
	 * 增加
	 * @param expressTemplate
	 * @throws ApiException
	 */
	void saveExpressTemplate(ExpressTemplate expressTemplate) throws ApiException;
	/**
	 * 更新
	 * @param expressTemplate
	 * @throws ApiException
	 */
    void updateExpressTemplate(ExpressTemplate expressTemplate) throws ApiException;
    /**
     * 删除
     * @param id 根据id删除
     * @throws ApiException
     */
    void deleteExpressTemplateById(String id) throws ApiException;
    
    /**
     * 根据价格、地区、重量计算快递价格
     * @param price 价格
     * @param province 地区
     * @param totalWeight 重量
     * @return
     * @throws ApiException
     */
    public BigDecimal getExpressPrice(String price, String province, String totalWeight) throws ApiException;
    
    public Map<String, BigDecimal> getOriginalPrice(String price, String province, String totalWeight) throws ApiException;
    
	/**
	 * 返回封装的省 checkBox
	 * @param province
	 * @return
	 */
	List<CheckBoxUtil> getTemplateProvinceList(String id) throws ApiException;
	
	void isProvinceRepeat(String province, int type, String id) throws ApiException;
	
}
