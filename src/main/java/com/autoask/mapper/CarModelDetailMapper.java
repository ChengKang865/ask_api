package com.autoask.mapper;

import com.autoask.entity.mysql.CarModelDetail;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarModelDetailMapper extends MyMapper<CarModelDetail> {

    List<String> getYearList(@Param("model") String model);

    List<String> getDetailList(@Param("model") String model, @Param("year") String year);
}