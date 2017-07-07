package com.autoask.dao.impl.product;

import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.product.ProductMetaDao;
import com.autoask.entity.mongo.product.ProductMeta;
import org.springframework.stereotype.Repository;

/**
 * Created by hp on 16-8-6.
 */

@Repository("productMetaDao")
public class ProductMetaDaoImpl extends BaseDaoImpl<ProductMeta, String> implements ProductMetaDao {

}
