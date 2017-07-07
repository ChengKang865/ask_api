package com.autoask.service.impl.product;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.dao.BaseDao;
import com.autoask.dao.product.IndexProductDao;
import com.autoask.entity.mongo.product.IndexProduct;
import com.autoask.entity.mysql.Product;
import com.autoask.mapper.GoodsMapper;
import com.autoask.mapper.ProductMapper;
import com.autoask.service.impl.BaseMongoServiceImpl;
import com.autoask.service.product.IndexProductService;
import com.autoask.service.product.ProductService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by hp on 16-10-15.
 */
@Service
public class IndexProductServiceImpl implements IndexProductService {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(IndexProductServiceImpl.class);

    @Autowired
    private IndexProductDao indexProductDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;


    @Override
    public void updateIndexProductList(String channel, String label, List<IndexProduct> indexProductList) throws ApiException {
        if (!Constants.IndexProductChannel.CHANNEL_LIST.contains(channel)) {
            throw new ApiException("参数非法");
        }
        if (!Constants.IndexProductLabel.LABEL_LIST.contains(label)) {
            throw new ApiException("参数非法");
        }

        //删除所有的符合条件的
        indexProductDao.delete(Query.query(Criteria.where("channel").is(channel).and("label").is(label)));

        for (IndexProduct indexProduct : indexProductList) {
            indexProductDao.save(indexProduct);
        }
    }

    /**
     * 获取首页的 product 列表接口
     *
     * @param channel 商城渠道(pc, h5...)
     * @param label   标签(new_gen, classic...)
     * @return 列表
     * @throws ApiException
     */
    @Override
    public List<IndexProduct> getIndexProductList(String channel, String label) throws ApiException {
        Query query = new Query();

        if (!Constants.IndexProductChannel.CHANNEL_LIST.contains(channel)) {
            throw new ApiException("参数非法");
        }
        if (!Constants.IndexProductLabel.LABEL_LIST.contains(label)) {
            throw new ApiException("参数非法");
        }

        Criteria criteria = new Criteria();
        criteria.and("channel").is(channel);
        criteria.and("label").is(label);

        query.addCriteria(criteria);
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "weight")));

        List<IndexProduct> indexProducts = indexProductDao.find(query);

        for (int index = 0; index < indexProducts.size(); index++) {
            IndexProduct indexProduct = indexProducts.get(index);
            Product product = productService.getProduct(indexProduct.getProductId());
            if (null == product) {
                indexProducts.remove(index);
                indexProductDao.deleteById(indexProduct.getId());
            } else {
                if (StringUtils.equals(indexProduct.getLabel(), Constants.IndexProductLabel.HOT)) {
                    indexProduct.setLogoUrl(indexProduct.getPicUrl());
                } else {
                    indexProduct.setLogoUrl(product.getLogoUrl());
                }
                indexProduct.setProductName(product.getName());
                indexProduct.setProductNameEn(product.getNameEn());
                indexProduct.setDescription(product.getDescription());
            }
        }

        //设置产品的价格信息
        ArrayList<String> productIds = new ArrayList<>(indexProducts.size());
        for (IndexProduct indexProduct : indexProducts) {
            productIds.add(indexProduct.getProductId());
        }
        if (CollectionUtils.isNotEmpty(productIds)) {
            List<Product> productPriceList = productMapper.getProductListPriceStr(productIds);
            HashMap<String, String> id2PriceMap = new HashMap<>();
            for (Product product : productPriceList) {
                id2PriceMap.put(product.getProductId(), product.getPriceStr());
            }
            for (IndexProduct indexProduct : indexProducts) {
                indexProduct.setPriceStr(id2PriceMap.get(indexProduct.getProductId()));
            }
        }
        return indexProducts;
    }
}
