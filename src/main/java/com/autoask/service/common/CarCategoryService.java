package com.autoask.service.common;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mysql.CarCategory;
import com.autoask.entity.mysql.CarModelDetail;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/10/23.
 */
public interface CarCategoryService {

    List<CarCategory> queryCarCategories(String parent);

    List<String> getYearList(String model) throws ApiException;


    List<String> getDetailList(String model, String year) throws ApiException;
}
