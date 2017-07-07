package com.autoask.service.statistics;

import com.autoask.common.exception.ApiException;

import java.util.Map;

/**
 * @author hyy
 * @create 16/11/7 03:12
 */
public interface StatisticsService {

    Map<String, Object> getAutoASKStatisticsIndex() throws ApiException;

    Map<String, Object> getServiceProviderIndex() throws ApiException;

    Map<String, Object> getFactoryIndex() throws ApiException;
}
