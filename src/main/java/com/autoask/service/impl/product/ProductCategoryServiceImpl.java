package com.autoask.service.impl.product;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.*;
import com.autoask.dao.product.ProductMetaDao;
import com.autoask.entity.mongo.product.ProductCategoryMeta;
import com.autoask.entity.mongo.product.ProductMeta;
import com.autoask.entity.mysql.Product;
import com.autoask.entity.mysql.ProductCategory;
import com.autoask.entity.param.OrderParam;
import com.autoask.mapper.ProductCategoryMapper;
import com.autoask.mapper.ProductMapper;
import com.autoask.service.product.ProductCategoryMetaService;
import com.autoask.service.product.ProductCategoryService;
import com.autoask.service.product.ProductService;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by hp on 16-8-3.
 */

@Service("productCategoryService")
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private Logger LOG = LoggerFactory.getLogger(ProductCategoryServiceImpl.class);

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryMetaService productCategoryMetaService;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductCategory getProductCategory(String productCategoryId) throws ApiException {
        LOG.info("get product category by id");
        try {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setDeleteFlag(false);
            productCategory.setProductCategoryId(productCategoryId);
            ProductCategory category = productCategoryMapper.selectOne(productCategory);
            if (null == category) {
                throw new ApiException("找不到指定的产品类别");
            }
            ProductCategoryMeta productCategoryMeta = productCategoryMetaService.getProductCategoryMeta(category);
            category.setProductCategoryMeta(productCategoryMeta);

            return category;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public ListSlice getProductCategoryList(Integer start, Integer limit) throws ApiException {
        LOG.info("get product category list");
        try {
            Example example = new Example(ProductCategory.class);
            example.createCriteria().andEqualTo("deleteFlag", false);
            int totalNum = productCategoryMapper.selectCountByExample(example);
            example.setOrderByClause("modify_time");
            List<ProductCategory> productCategories = productCategoryMapper.selectByExampleAndRowBounds(example, new RowBounds(start, limit));
            return new ListSlice(productCategories, new Long(totalNum));
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public void updateProductCategory(ProductCategory productCategory) throws ApiException {
        LOG.info("update product category");
        Example example = new Example(ProductCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productCategoryId", productCategory.getProductCategoryId())
                .andEqualTo("deleteFlag", false);

        productCategory.setModifyTime(DateUtil.getDate());
        productCategory.setModifyId(LoginUtil.getLoginId());

        try {
            int updateNum = productCategoryMapper.updateByExampleSelective(productCategory, example);
            if (updateNum == 0) {
                throw new ApiException("更新失败,该种类不存在");
            }
            productCategoryMetaService.saveProductCategoryMeta(productCategory.getProductCategoryId(), productCategory.getProductCategoryMeta());
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public void deleteProductCategory(String productCategoryId) throws ApiException {
        LOG.info("delete product category");

        Map<String, Object> map = new HashMap<>();
        map.put("productCategoryId", productCategoryId);
        map.put("deleteFlag", false);
        Integer productCount = productService.getProductCount(productCategoryId);
        if (productCount != 0) {
            throw new ApiException("商品种类下已有商品,不可删除!");
        }
        ProductCategory toUpdate = new ProductCategory();
        toUpdate.setDeleteFlag(true);
        toUpdate.setModifyId(LoginUtil.getLoginId());
        toUpdate.setModifyTime(DateUtil.getDate());
        Example example = new Example(ProductCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleteFlag", false).andEqualTo("productCategoryId", productCategoryId);
        int updateNum = productCategoryMapper.updateByExampleSelective(toUpdate, example);
        if (0 == updateNum) {
            throw new ApiException("未找到需要删除的信息");
        }
    }

    @Override
    public void addProductCategory(ProductCategory productCategory) throws ApiException {
        LOG.info("add product category");

        if (StringUtils.isEmpty(productCategory.getName()) || StringUtils.isEmpty(productCategory.getNameEn())) {
            throw new ApiException("必须填写商品名称和名称缩写");
        }
        Example example = new Example(ProductCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleteFlag", false);
        criteria.andEqualTo("nameEn", productCategory.getNameEn());
        example.or(example.createCriteria().andEqualTo("name", productCategory.getName()));

        int num = productCategoryMapper.selectCountByExample(example);
        if (num != 0) {
            throw new ApiException("商品种类命名重复!");
        }
        try {
            productCategory.setProductCategoryId(CodeGenerator.uuid());
            productCategory.setDeleteFlag(false);
            productCategory.setCreateTime(DateUtil.getDate());
            productCategory.setModifyTime(DateUtil.getDate());
            productCategory.setCreatorId(LoginUtil.getSessionInfo().getLoginId());
            productCategoryMapper.insert(productCategory);
            // 保存到mongo中
            productCategoryMetaService.saveProductCategoryMeta(productCategory.getProductCategoryId(), productCategory.getProductCategoryMeta());
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getIndexProductCategoryList() {

        List<ProductCategory> productCategoryList = productCategoryMapper.getProductCategoryList();

        List<Map<String, Object>> resultList = new ArrayList<>(productCategoryList.size());

        for (ProductCategory productCategory : productCategoryList) {
            HashMap<String, Object> itemMap = new HashMap<>();
            itemMap.put("categoryName", productCategory.getName());
            ArrayList<Map<String, String>> productJsonArr = new ArrayList<>(productCategory.getProductList().size());
            for (Product product : productCategory.getProductList()) {
                HashMap<String, String> itemProductJson = new HashMap<>();
                itemProductJson.put("productId", product.getProductId());
                itemProductJson.put("logoUrl", product.getLogoUrl());
                itemProductJson.put("name", product.getName());
                itemProductJson.put("nameEn", product.getNameEn());
                itemProductJson.put("description", product.getDescription());
                itemProductJson.put("headStr", product.getHeadStr());

                productJsonArr.add(itemProductJson);
            }
            itemMap.put("productList", productJsonArr);

            resultList.add(itemMap);
        }

        return resultList;
    }

    @Override
    public BigDecimal getServiceFee(List<String> goodsIdSet) {
        List<ProductCategory> categoryList = productMapper.getProductCategoryList(goodsIdSet);
        BigDecimal totalServiceFee = new BigDecimal(0);
        for (ProductCategory category : categoryList) {
            totalServiceFee = totalServiceFee.add(category.getServiceFee());
        }
        return totalServiceFee;
    }
}
