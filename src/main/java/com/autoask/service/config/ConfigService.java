package com.autoask.service.config;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CheckBoxUtil;
import com.autoask.entity.mongo.config.Banner;

import java.util.List;
import java.util.Map;

/**
 * @author hyy
 * @create 16/10/23 20:55
 */
public interface ConfigService {

    /**
     * 获取免邮费的价格
     *
     * @return
     */
    long getDeliveryFreeAmount();

    void updateDeliveryFreeAmount(Long amount, String province) throws ApiException;


    /**
     * 获取 bannerList
     *
     * @param channel
     * @return
     * @throws ApiException
     */
    List<Banner> getBannerList(String channel) throws ApiException;


    /**
     * 设置 bannerList
     *
     * @param bannerList
     * @param channel
     * @return
     * @throws ApiException
     */
    List<Banner> insertOrUpdateBannerList(List<Banner> bannerList, String channel) throws ApiException;

    /**
     * 设置线上订单搜索分成修理厂的范围
     *
     * @param distance
     * @throws ApiException
     */
    void setAdFeeCircleDistance(Long distance) throws ApiException;
    
	/**
	 * 根据地区查询免邮价格
	 * @param province
	 * @return
	 * @throws ApiException
	 */
	int getByProvince(String province) throws ApiException;
	
	/**
	 * 返回封装的省 checkBox
	 * @param province
	 * @return
	 */
	List<CheckBoxUtil> provinceList() throws ApiException;
	
	Map provinceName() throws ApiException;
		

}
