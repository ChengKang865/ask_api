package com.autoask.service.impl.product;

import com.autoask.common.exception.ApiException;
import com.autoask.dao.BaseDao;
import com.autoask.dao.product.ProductCategoryMetaDao;
import com.autoask.entity.mongo.product.ProductCategoryMeta;
import com.autoask.entity.mysql.ProductCategory;
import com.autoask.service.impl.BaseMongoServiceImpl;
import com.autoask.service.product.ProductCategoryMetaService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * Created by hp on 16-8-6.
 */

@Service("productCategoryMetaService")
public class ProductCategoryMetaServiceImpl extends BaseMongoServiceImpl<ProductCategoryMeta> implements ProductCategoryMetaService {
    private static Logger LOG = org.slf4j.LoggerFactory.getLogger(ProductCategoryMetaServiceImpl.class);

    @Autowired
    private ProductCategoryMetaDao productCategoryMetaDao;

    @Override
    public ProductCategoryMeta getProductCategoryMeta(ProductCategory productCategory) throws ApiException {
        if (null == productCategory) {
            return null;
        }
        try {
            Criteria criteria = Criteria.where("productCategoryId").is(productCategory.getProductCategoryId());
            ProductCategoryMeta one = productCategoryMetaDao.findOne(Query.query(criteria));
            return one;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public void saveProductCategoryMeta(String productCategoryId, ProductCategoryMeta productCategoryMeta) throws ApiException {
        if (null != productCategoryMeta) {
            try {
                Criteria criteria = Criteria.where("productCategoryId").is(productCategoryId);
                ProductCategoryMeta one = productCategoryMetaDao.findOne(Query.query(criteria));
                if (null != one) {
                    productCategoryMetaDao.delete(Query.query(criteria));
                    productCategoryMetaDao.save(productCategoryMeta);
                } else {
                    productCategoryMeta.setProductCategoryId(productCategoryId);
                    productCategoryMetaDao.save(productCategoryMeta);
                }
            } catch (Exception e) {
                LOG.warn(e.getMessage());
                throw new ApiException(e.getMessage());
            }
        }
    }

    @Override
    public BaseDao<ProductCategoryMeta, String> getMainDao() {
        return productCategoryMetaDao;
    }
}
