package com.autoask.dao.impl.product;

import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.product.GoodsMetaDao;
import com.autoask.entity.mongo.product.GoodsMeta;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 * @create 2016-08-07 14:59
 */
@Repository("goodsMetaDao")
public class GoodsMetaDaoImpl extends BaseDaoImpl<GoodsMeta, String> implements GoodsMetaDao {
}
