package com.autoask.service.impl.product;

import com.autoask.common.exception.ApiException;
import com.autoask.dao.BaseDao;
import com.autoask.dao.product.ProductBannerDao;
import com.autoask.entity.mongo.product.ProductBanner;
import com.autoask.service.impl.BaseMongoServiceImpl;
import com.autoask.service.product.ProductBannerService;
import com.autoask.service.product.ProductService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hp on 16-9-24.
 * <p>
 * implements ProductUserCommentService
 */
@Service("productBannerService")
public class ProductBannerServiceImpl extends BaseMongoServiceImpl<ProductBanner> implements ProductBannerService {
    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(ProductBannerServiceImpl.class);

    @Autowired
    ProductBannerDao productBannerDao;

    @Autowired
    ProductService productService;

    /**
     * 创建 ProductBanner
     *
     * @param productBanner ProductBanner object
     * @throws ApiException
     */
    @Override
    public void createProductBanner(ProductBanner productBanner) throws ApiException {
        LOG.info("create product banner.");

        // 基础校验
        if (null == productBanner) {
            throw new ApiException("商品banner图为空!");
        }
        if (StringUtils.isEmpty(productBanner.getPicUrl())) {
            throw new ApiException("商品banner链接为空!");
        }
        if (StringUtils.isEmpty(productBanner.getCreatorId())) {
            throw new ApiException("创建者id为空!");
        }

        // 校验链接是否已经存在
        Criteria criteria = getBaseCriteria().and("picUrl").is(productBanner.getPicUrl());
        List<ProductBanner> productBanners = productBannerDao.find(Query.query(criteria));
        if (CollectionUtils.isNotEmpty(productBanners)) {
            throw new ApiException("图片链接已经存在!");
        }

        try {
            // 默认是上线状态
            productBanner.setOnLine(true);
            // 初始化
            if (null == productBanner.getPriority()) {
                productBanner.setPriority(0);
            }
            super.create(productBanner);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * 根据 ProductBannerId 获取 banner
     *
     * @param productBannerId 轮播图id
     * @return ProductBanner
     * @throws ApiException
     */
    @Override
    public ProductBanner getProductBannerById(String productBannerId) throws ApiException {
        LOG.info("get product banner by id");

        if (StringUtils.isEmpty(productBannerId)) {
            throw new ApiException("Banner id 为空");
        }
        try {
            Criteria criteria = new Criteria("_id").is(productBannerId).and("deleteFlag").is(false);
            ProductBanner productBanner = productBannerDao.findOne(Query.query(criteria));
            if (null == productBanner) {
                throw new ApiException("未能找到对应的banner图.");
            }
            return productBanner;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * 获取轮播图列表
     *
     * @return List
     * @throws ApiException
     */
    @Override
    public List<ProductBanner> getProductBannerList() throws ApiException {
        LOG.info("get online product banner picture list.");

        try {
            Criteria criteria = getBaseCriteria().and("onLine").is(true);
            Query query = Query.query(criteria);
            // 按权重排序
            query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "priority")));

            return productBannerDao.find(query);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * 上线轮播图
     *
     * @param productBannerId 轮拨图 id
     * @throws ApiException
     */
    @Override
    public void onlineProductBanner(String productBannerId) throws ApiException {
        LOG.info("online product banner.");

        if (StringUtils.isEmpty(productBannerId)) {
            throw new ApiException("Banner id 为空");
        }

        try {
            Criteria criteria = new Criteria("_id").is(productBannerId)
                    .and("deleteFlag").is(false)
                    .and("onLine").is(false);
            ProductBanner productBanner = productBannerDao.findOne(Query.query(criteria));
            if (null == productBanner) {
                throw new ApiException("商品Banner图不存在!");
            }
            productBanner.setOnLine(true);
            super.updateSelective(productBannerId, productBanner);
            productBannerDao.save(productBanner);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * 下线轮播图
     *
     * @param productBannerId 轮播图 id
     * @throws ApiException
     */
    @Override
    public void offlineProductBanner(String productBannerId) throws ApiException {
        LOG.info("offline product banner.");

        if (StringUtils.isEmpty(productBannerId)) {
            throw new ApiException("Banner id 为空");
        }

        try {
            Criteria criteria = new Criteria("_id").is(productBannerId)
                    .and("deleteFlag").is(false)
                    .and("onLine").is(true);
            ProductBanner productBanner = productBannerDao.findOne(Query.query(criteria));
            if (null == productBanner) {
                throw new ApiException("在线的商品Banner图不存在或已经下线!");
            }
            productBanner.setOnLine(false);
            super.updateSelective(productBannerId, productBanner);
            productBannerDao.save(productBanner);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * 删除轮播图
     *
     * @param productBannerId 轮播图 id
     * @throws ApiException
     */
    @Override
    public void deleteProductBanner(String productBannerId) throws ApiException {
        LOG.info("delete product banner.");

        if (StringUtils.isEmpty(productBannerId)) {
            throw new ApiException("Banner id 为空");
        }

        try {
            Criteria criteria = new Criteria("_id").is(productBannerId)
                    .and("deleteFlag").is(false);
            ProductBanner productBanner = productBannerDao.findOne(Query.query(criteria));

            if (null == productBanner) {
                throw new ApiException("商品Banner图不存在");
            }
            super.deleteById(productBannerId);
        } catch (Exception e) {
            e.getMessage();
            LOG.warn(e.getMessage());
        }
    }

    /**
     * 获取在线的轮播图总数
     *
     * @return Integer
     */
    @Override
    public Integer getProductBannerCount() {
        LOG.info("get product banner count.");

        Criteria criteria = getBaseCriteria().and("onLine").is(true);
        return (int) productBannerDao.count(Query.query(criteria));
    }

    @Override
    public BaseDao<ProductBanner, String> getMainDao() {
        return productBannerDao;
    }
}
