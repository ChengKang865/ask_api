package com.autoask.service.product;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.product.IndexProduct;
import com.autoask.entity.mongo.product.ProductMeta;
import com.autoask.entity.mysql.Product;

import java.util.List;
import java.util.Map;

/**
 * Created by weid on 16-8-3.
 */


public interface ProductService {

    void addProduct(Product product) throws ApiException;

    void updateProduct(Product product) throws ApiException;

    void deleteProduct(String productId) throws ApiException;

    List<Product> getProductList(String productCategoryId) throws ApiException;

    Product getProduct(String productId) throws ApiException;

    Integer getProductCount(String productCategoryId) throws ApiException;

    Double getMinProductPrice(String productId) throws ApiException;

    Integer getProductOnSellGoodsCount(String productId) throws ApiException;

    List<Product> getRandom3ProductList(String productId) throws ApiException;

    List<Product> searchProductByName(String content) throws ApiException;

    void setIndexProductPriceStr(List<IndexProduct> indexProductList) throws ApiException;
}
