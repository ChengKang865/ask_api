package com.autoask.service.product;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.product.ProductCategoryMeta;
import com.autoask.entity.mysql.ProductCategory;
import com.autoask.service.BaseMongoService;
import org.springframework.stereotype.Service;

/**
 * Created by hp on 16-8-6.
 */

@Service("productCategoryMetaService")
public interface ProductCategoryMetaService extends BaseMongoService<ProductCategoryMeta> {

    ProductCategoryMeta getProductCategoryMeta(ProductCategory productCategory) throws ApiException;

    void saveProductCategoryMeta(String productCategoryId, ProductCategoryMeta productCategoryMeta) throws ApiException;

}
