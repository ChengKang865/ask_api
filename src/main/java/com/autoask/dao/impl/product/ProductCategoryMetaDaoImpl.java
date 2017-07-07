package com.autoask.dao.impl.product;

import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.product.ProductCategoryMetaDao;
import com.autoask.entity.mongo.product.ProductCategoryMeta;
import org.springframework.stereotype.Repository;

/**
 * Created by hp on 16-8-6.
 */

@Repository("productCategoryMetaDao")
public class ProductCategoryMetaDaoImpl extends BaseDaoImpl<ProductCategoryMeta, String> implements ProductCategoryMetaDao {

}
