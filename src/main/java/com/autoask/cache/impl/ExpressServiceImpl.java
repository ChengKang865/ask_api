package com.autoask.cache.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.autoask.cache.CacheService;
import com.autoask.cache.ExpressCacheService;
import com.autoask.cache.ExpressService;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.HttpClientUtil;
import com.autoask.entity.common.Express;
/**
 * 快递信息实现类
 * @author ck
 *
 * @Create 2017年3月29日下午4:49:33
 */
@Service("expressService")
public class ExpressServiceImpl implements ExpressService {
	private static Logger LOG = LoggerFactory.getLogger(ExpressServiceImpl.class);
	
    /**
     * 接口地址
     */
    private static final String EXPRESS_HOST="https://ali-deliver.showapi.com/showapi_expInfo";
    /**
     * Code
     */
    private static final String APP_CODE="5e0792b8b13d47e5831712b643d8fded";
    
    @Autowired
    private  CacheService cacheService;
    
    @Autowired
    private ExpressCacheService expressCacheService;
    
	@Override
	public Express queryExpress(String oddNumber,String com) throws ApiException{
		Express express = new Express();
		CloseableHttpResponse response = null;
		//根据单号查询缓存中是否存在
		Boolean exists=expressCacheService.exists(oddNumber);
		if(exists==false){
			try {
				Map<String, String> params = new HashMap<>(3, 1);
				params.put("com", com);
				params.put("nu", oddNumber);
			    List<Header> headers = new ArrayList<>();
			    headers.add(new BasicHeader("Authorization", "APPCODE " + APP_CODE));
		        response=HttpClientUtil.get(EXPRESS_HOST, params, headers, Constants.Charset.UTF8);
		    	InputStream content = response.getEntity().getContent();
	            JSONObject jsonContent = JSON.parseObject(IOUtils.toString(content));
	            express.setContent(jsonContent);
	            express.setOddNumber(oddNumber);
	            express.setTimeOut(DateUtil.getDate().getTime());
		    	//返回信息放入缓存中
	            expressCacheService.setExpress(oddNumber, jsonContent, DateUtil.getDate().getTime());
		    } catch (IOException e) {
		    	LOG.error(e.getMessage());
	            return null;
		    }finally {
		    	//关闭流
	            IOUtils.closeQuietly(response);
	        }
		}else{
			//获取缓存中的信息
			express=cacheService.get(oddNumber, Express.class);
		}
		return express;
	}

}
