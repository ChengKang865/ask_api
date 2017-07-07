package com.autoask.service.product;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mysql.ProductCategory;
import com.autoask.entity.param.OrderParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hp on 16-8-3.
 */
public interface ProductCategoryService {

    ProductCategory getProductCategory(String productCategoryId) throws ApiException;

    ListSlice getProductCategoryList(Integer start, Integer length) throws ApiException;

    void updateProductCategory(ProductCategory productCategory) throws ApiException;

    void deleteProductCategory(String productCategoryId) throws ApiException;

    void addProductCategory(ProductCategory productCategory) throws ApiException;

    /**
     * 获取首页的展示样式
     *
     * @return
     */
    List<Map<String, Object>> getIndexProductCategoryList();

    BigDecimal getServiceFee(List<String> goodsIds);

}
