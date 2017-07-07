package com.autoask.mapper;

import com.autoask.entity.mysql.ProductCategory;
import com.autoask.mapper.base.MyMapper;

import java.util.List;

public interface ProductCategoryMapper extends MyMapper<ProductCategory> {

    List<ProductCategory> getProductCategoryList();
}