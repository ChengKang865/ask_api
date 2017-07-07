package com.autoask.mapper;

import com.autoask.entity.mysql.CityArea;
import com.autoask.mapper.base.MyMapper;

import java.util.List;

public interface CityAreaMapper extends MyMapper<CityArea> {

    List<String> getStreets(String province, String city, String region);

}