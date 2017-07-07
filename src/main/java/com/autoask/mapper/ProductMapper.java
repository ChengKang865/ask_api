package com.autoask.mapper;

import com.autoask.entity.mysql.Product;
import com.autoask.entity.mysql.ProductCategory;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ProductMapper extends MyMapper<Product> {

    List<Product> selectByMap(Map<String, Object> queryMap);

    List<Product> searchProductList(@Param("content") String content);

    String getProductPriceStr(@Param("productId") String productId);

    List<Product> getProductListPriceStr(@Param("productIdList") List<String> productIdList);

    List<ProductCategory> getProductCategoryList(@Param("goodsIdList") List<String> goodsIdList);
}