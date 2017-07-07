package com.autoask.service.product;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.product.IndexProduct;
import com.autoask.service.BaseMongoService;

import java.util.List;

/**
 * Created by hp on 16-10-15.
 * M站首页的 productList 相关的 service
 */
public interface IndexProductService {

    void updateIndexProductList(String channel, String label, List<IndexProduct> indexProductList) throws ApiException;

    List<IndexProduct> getIndexProductList(String channel, String label) throws ApiException;
}
