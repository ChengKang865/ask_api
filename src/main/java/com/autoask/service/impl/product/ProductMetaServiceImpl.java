package com.autoask.service.impl.product;

import com.autoask.common.exception.ApiException;
import com.autoask.dao.BaseDao;
import com.autoask.dao.product.ProductMetaDao;
import com.autoask.entity.mongo.product.ProductMeta;
import com.autoask.entity.mysql.Product;
import com.autoask.service.impl.BaseMongoServiceImpl;
import com.autoask.service.product.ProductMetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


/**
 * Created by hp on 16-8-6.
 */
@Service("productMetaService")
public class ProductMetaServiceImpl extends BaseMongoServiceImpl<ProductMeta> implements ProductMetaService {
    private static Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    ProductMetaDao productMetaDao;

    @Override
    public ProductMeta getProductMeta(Product product) throws ApiException {
        if (null == product) {
            return null;
        }
        try {
            Criteria criteria = Criteria.where("productId").is(product.getProductId());
            ProductMeta productMeta = productMetaDao.findOne(Query.query(criteria));
            return productMeta;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public void saveProductMeta(String productId, ProductMeta productMeta) throws ApiException {
        if (null != productMeta) {
            try {
                Criteria criteria = Criteria.where("productId").is(productId);
                ProductMeta one = productMetaDao.findOne(Query.query(criteria));

                if (null != one) {
                    productMetaDao.delete(Query.query(Criteria.where("productId").is(productId)));
                    productMetaDao.save(productMeta);
                } else {
                    productMeta.setProductId(productId);
                    productMetaDao.save(productMeta);
                }
            } catch (Exception e) {
                LOG.warn(e.getMessage());
                throw new ApiException(e.getMessage());
            }
        }
    }

    @Override
    public BaseDao<ProductMeta, String> getMainDao() {
        return productMetaDao;
    }
}
