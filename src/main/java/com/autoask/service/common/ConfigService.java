package com.autoask.service.common;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.config.Config;

/**
 * Created by hp on 16-10-10.
 */
public interface ConfigService {
    /**
     * 新增
     */
    void insertConfigData(String type,String name,String val,String mark) throws ApiException;
    
    /**
     * 更新
     */
    void updateConfigData(Config config) throws ApiException;

}
