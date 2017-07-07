package com.autoask.dao.impl.product;

import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.product.ProductBannerDao;
import com.autoask.entity.mongo.product.ProductBanner;
import org.springframework.stereotype.Repository;

/**
 * Created by hp on 16-9-24.
 */
@Repository("productBannerDao")
public class ProductBannerDaoImpl extends BaseDaoImpl<ProductBanner, String> implements ProductBannerDao {
}
