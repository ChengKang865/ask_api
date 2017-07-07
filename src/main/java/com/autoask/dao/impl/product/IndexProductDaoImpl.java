package com.autoask.dao.impl.product;

import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.product.IndexProductDao;
import com.autoask.entity.mongo.product.IndexProduct;
import org.springframework.stereotype.Repository;

/**
 * Created by hp on 16-10-15.
 */
@Repository("IndexProductDao")
public class IndexProductDaoImpl extends BaseDaoImpl<IndexProduct, String> implements IndexProductDao {

}
