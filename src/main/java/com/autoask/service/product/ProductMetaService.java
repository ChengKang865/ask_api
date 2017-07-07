package com.autoask.service.product;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.product.ProductMeta;
import com.autoask.entity.mysql.Product;
import com.autoask.service.BaseMongoService;

/**
 * Created by hp on 16-8-6.
 */
public interface ProductMetaService extends BaseMongoService<ProductMeta> {

    ProductMeta getProductMeta(Product product) throws ApiException;

    void saveProductMeta(String productId, ProductMeta productMeta) throws ApiException;

}
