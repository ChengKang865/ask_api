package com.autoask.service.common;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mysql.CityArea;

import java.util.List;

/**
 * Created by hp on 16-8-8.
 */
public interface CityAreaService {

    List<CityArea> getCityAreaByParentId(Integer parentId) throws ApiException;

    List<String> getStreets(String province, String city, String region);
}
