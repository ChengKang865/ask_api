package com.autoask.cache;

import com.autoask.entity.common.Express;

/**
 * 快递缓存公共接口
 * @author ck
 *
 * @Create 2017年3月30日上午9:57:59
 */
public interface ExpressCacheService {
	/**
	 * 增加缓存
	 * @param oddNumber 
	 * @param content
	 * @param timeOut
	 */
	void setExpress(String oddNumber,Object content,Long timeOut);
	
    /**
     * 删除缓存
     * @param oddNumber 快递单号
     */
    void removeExpress(String oddNumber);

    /**
     * 获取缓存信息
     * @param oddNumber 快递单号
     * @return
     */
    Express getExpress(String oddNumber);
    
    /**
     * 是否存在
     * @param oddNumber 快递单号
     * @return
     */
    Boolean exists(String oddNumber);
    
}
