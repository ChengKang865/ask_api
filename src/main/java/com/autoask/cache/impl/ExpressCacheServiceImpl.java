package com.autoask.cache.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autoask.cache.CacheService;
import com.autoask.cache.ExpressCacheService;
import com.autoask.entity.common.Express;
/**
 * 快递缓接口实现类
 * @author ck
 *
 * @Create 2017年3月30日上午10:19:34
 */
@Service("expressCacheService")
public class ExpressCacheServiceImpl implements ExpressCacheService {
private static Logger LOG = LoggerFactory.getLogger(ExpressServiceImpl.class);
	
    /**
     * 快递缓存信息默认的失效时间为7200秒
     */
    private static final int EXPIRE_TIME = 7200;
    
    @Autowired
    private  CacheService cacheService;
    
	@Override
	public void setExpress(String oddNumber, Object content, Long timeOut){
		Express expressInfo = new Express();
		expressInfo.setOddNumber(oddNumber);
    	expressInfo.setContent(content);
    	expressInfo.setTimeOut(timeOut);
		cacheService.set(oddNumber, expressInfo, EXPIRE_TIME);
	}

	@Override
	public void removeExpress(String oddNumber) {
		cacheService.del(oddNumber);
	}

	@Override
	public Express getExpress(String oddNumber) {
		return cacheService.get(oddNumber, Express.class);
	}

	@Override
	public Boolean exists(String oddNumber) {
		return cacheService.exists(oddNumber);
	}

}
