package com.autoask.service.impl.common;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.entity.mysql.CarCategory;
import com.autoask.entity.mysql.CarModelDetail;
import com.autoask.mapper.CarCategoryMapper;
import com.autoask.mapper.CarModelDetailMapper;
import com.autoask.service.common.CarCategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/10/23.
 */
@Service("carCategoryService")
public class CarCategoryServiceImpl implements CarCategoryService {

    @Autowired
    private CarCategoryMapper carCategoryMapper;

    @Autowired
    private CarModelDetailMapper carModelDetailMapper;

    @Override
    public List<CarCategory> queryCarCategories(String parent) {
        return carCategoryMapper.selectCarCategories(parent);
    }

    @Override
    public List<String> getYearList(String model) throws ApiException {
        List<String> yearList = carModelDetailMapper.getYearList(model);
        return yearList;
    }

    @Override
    public List<String> getDetailList(String model, String year) throws ApiException {

        return carModelDetailMapper.getDetailList(model,year);
    }
}
