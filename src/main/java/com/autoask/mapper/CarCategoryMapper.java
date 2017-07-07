package com.autoask.mapper;

import com.autoask.entity.mysql.CarCategory;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarCategoryMapper extends MyMapper<CarCategory> {

    List<CarCategory> selectCarCategories(@Param("parent") String parent);

}