package com.autoask.service.impl.product;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CodeGenerator;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.LoginUtil;
import com.autoask.entity.mongo.product.IndexProduct;
import com.autoask.entity.mongo.product.ProductMeta;
import com.autoask.entity.mysql.Goods;
import com.autoask.entity.mysql.Product;
import com.autoask.entity.mysql.ProductCategory;
import com.autoask.mapper.GoodsMapper;
import com.autoask.mapper.ProductMapper;
import com.autoask.service.product.GoodsService;
import com.autoask.service.product.ProductCategoryService;
import com.autoask.service.product.ProductMetaService;
import com.autoask.service.product.ProductService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by weid on 16-8-3.
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {

    private Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private GoodsMapper goodsMapper;


    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductMetaService productMetaService;

    @Override
    public void addProduct(Product product) throws ApiException {
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleteFlag", false);
//        criteria.andEqualTo("nameEn", product.getNameEn());
        example.or(example.createCriteria().andEqualTo("name", product.getName()));

        int num = productMapper.selectCountByExample(example);
        if (num != 0) {
            throw new ApiException("名字重名");
        }
        if (StringUtil.isEmpty(product.getProductCategoryId())) {
            throw new ApiException("商品种类号为空!");
        }
        try {
            ProductCategory productCategory = productCategoryService.getProductCategory(product.getProductCategoryId());
            if (null == productCategory) {
                throw new ApiException("指定类别不存在");
            }
        } catch (ApiException e) {
            LOG.warn(e.toString());
            throw new ApiException(e.getMessage());
        }

        try {
            product.setProductId(CodeGenerator.uuid());
            product.setDeleteFlag(false);
            product.setCreateTime(DateUtil.getDate());
            product.setModifyTime(DateUtil.getDate());
            product.setCreatorId(LoginUtil.getSessionInfo().getLoginId());

            if (CollectionUtils.isEmpty(product.getProductMeta().getPicUrlList())) {
                throw new ApiException("产品图至少需要一张");
            }

            product.setLogoUrl(product.getProductMeta().getPicUrlList().get(0));

            productMapper.insert(product);
            // 将 productMeta 保存到 mongo
            productMetaService.saveProductMeta(product.getProductId(), product.getProductMeta());
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public void updateProduct(Product product) throws ApiException {
        LOG.info("update product");

        Example example = new Example(Product.class);
        product.setModifyTime(DateUtil.getDate());
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId", product.getProductId())
                .andEqualTo("deleteFlag", false);

        List<Product> products = productMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(products)) {
            throw new ApiException("产品不存在");
        }
        Product preProduct = products.get(0);
        if (!StringUtils.equals(product.getName(), preProduct.getName())) {
            //名称更改 检查名称是否冲突
            Example conflictExp = new Example(Product.class);
            Example.Criteria conflictCri = conflictExp.createCriteria();
            conflictCri.andEqualTo("name", product.getName()).andEqualTo("deleteFlag", false);
            int conflictNum = productMapper.selectCountByExample(conflictExp);
            if (conflictNum != 0) {
                throw new ApiException("名称冲突");
            }
        }
        int updateNumber = productMapper.updateByExampleSelective(product, example);
        if (updateNumber == 0) {
            throw new ApiException("产品不存在");
        }
        productMetaService.saveProductMeta(product.getProductId(), product.getProductMeta());

    }

    @Override
    public void deleteProduct(String productId) throws ApiException {
        LOG.info("delete product");

        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleteFlag", false);
        criteria.andEqualTo("productId", productId);

        List<Product> productList = productMapper.selectByExample(example);
        boolean empty = CollectionUtils.isEmpty(productList);
        if (empty) {
            throw new ApiException("找不到该产品");
        }

        // 下属 goods 的判断
        Product resultProduct = productList.get(0);
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("productId", resultProduct.getProductId());
        productMap.put("deleteFlag", false);
        Integer goodsCount = goodsService.getGoodsCount(productMap);
        if (null != goodsCount && goodsCount != 0) {
            throw new ApiException("已有下属商品,不可以删除!");
        }

        Product product = new Product();
        product.setModifyTime(DateUtil.getDate());
        product.setDeleteFlag(true);
        product.setModifyId(LoginUtil.getLoginId());
        productMapper.updateByExampleSelective(product, example);
    }

    @Override
    public Product getProduct(String productId) throws ApiException {
        LOG.info("get product by id");

        Product product = new Product();
        product.setDeleteFlag(false);
        product.setProductId(productId);
        Product productRes = productMapper.selectOne(product);
        if (null == productRes) {
            return null;
        }
        // 设置 productMeta
        ProductMeta productMeta = productMetaService.getProductMeta(productRes);
        productRes.setProductMeta(productMeta);
        // 设置 productCategory
        ProductCategory productCategory = productCategoryService.getProductCategory(productRes.getProductCategoryId());
        productRes.setProductCategory(productCategory);
        return productRes;
    }

    @Override
    public List<Product> getProductList(String productCategoryId) throws ApiException {
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(productCategoryId)) {
            criteria.andEqualTo("productCategoryId", productCategoryId);
        }
        criteria.andEqualTo("deleteFlag", false);
        return productMapper.selectByExample(example);
    }

    @Override
    public Integer getProductCount(String productCategoryId) throws ApiException {
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(productCategoryId)) {
            criteria.andEqualTo("productCategoryId", productCategoryId);
        }
        criteria.andEqualTo("deleteFlag", false);
        return productMapper.selectCountByExample(example);
    }

    /**
     * 获取 product 的 goods 中最低的价格(线上/线下的最低价)
     *
     * @param productId 产品 id
     * @return 最低价
     * @throws ApiException
     */
    @Override
    public Double getMinProductPrice(String productId) throws ApiException {
        LOG.info("get minimum price of product.");
        Product product = new Product();
        product.setDeleteFlag(false);
        product.setProductId(productId);
        Product productRes = productMapper.selectOne(product);
        if (null == productRes) {
            throw new ApiException("product 不存在!");
        }

        Double minOnlineProductGoodsPrice = goodsMapper.getMinOnlineProductGoodsPrice(productId);

        return minOnlineProductGoodsPrice;
    }

    /**
     * 获取 product 下在售的 goods 总数
     *
     * @param productId 产品 id
     * @return 在售的 goods 总数
     * @throws ApiException
     */
    @Override
    public Integer getProductOnSellGoodsCount(String productId) throws ApiException {
        LOG.info("get product goods count.");

        try {
            Product product = new Product();
            product.setDeleteFlag(false);
            product.setProductId(productId);
            Product productRes = productMapper.selectOne(product);
            if (null == productRes) {
                throw new ApiException("product 不存在!");
            }

            return goodsMapper.getOnSellGoodsCountByProductId(productId);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public List<Product> getRandom3ProductList(String productId) throws ApiException {
        Example example = new Example(Product.class);
        example.createCriteria().andNotEqualTo("productId", productId).andEqualTo("deleteFlag",false);
        List<Product> products = productMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(products) || products.size() < 3) {
            return null;
        }
        Random random1 = new Random();
        int num1 = random1.nextInt(products.size() - 1);
        int num2 = num1;
        Random random2 = new Random();
        while (num2 == num1) {
            num2 = random2.nextInt(products.size() - 1);
        }
        int num3 = num1;
        Random random3 = new Random();
        while (num3 == num1 || num3 == num2) {
            num3 = random3.nextInt(products.size() - 1);
        }
        ArrayList<Product> resultList = new ArrayList<>(3);
        resultList.add(products.get(num1));
        resultList.add(products.get(num2));
        resultList.add(products.get(num3));

        Product product1 = resultList.get(0);
        Product product2 = resultList.get(1);
        Product product3 = resultList.get(2);

        List<Product> productPriceList = productMapper.getProductListPriceStr(Arrays.asList(product1.getProductId(), product2.getProductId(), product3.getProductId()));
        HashMap<String, String> id2PriceMap = new HashMap<>(productPriceList.size());
        for (Product product : productPriceList) {
            id2PriceMap.put(product.getProductId(), product.getPriceStr());
        }
        product1.setPriceStr(id2PriceMap.get(product1.getProductId()));
        product2.setPriceStr(id2PriceMap.get(product2.getProductId()));
        product3.setPriceStr(id2PriceMap.get(product3.getProductId()));

        return resultList;
    }

    private BigDecimal getLowPrice(Product product) {
        Example goodsExp = new Example(Goods.class);
        goodsExp.createCriteria().andEqualTo("productId", product.getProductId());

        List<Goods> goodsList = goodsMapper.selectByExample(goodsExp);
        int val = 100000;
        BigDecimal indeVal = new BigDecimal(val);
        for (Goods goods : goodsList) {
            BigDecimal itemPrice = goods.getOfflinePrice().compareTo(goods.getOnlinePrice()) < 0 ? goods.getOfflinePrice() :
                    goods.getOnlinePrice();
            indeVal = itemPrice.compareTo(indeVal) < 0 ? itemPrice : indeVal;
        }

        if (indeVal.compareTo(new BigDecimal(val)) == 0) {
            return new BigDecimal(0);
        }
        return indeVal;
    }

    @Override
    public List<Product> searchProductByName(String content) throws ApiException {
        if (StringUtils.isNotEmpty(content)) {
            return productMapper.searchProductList(content);

        } else {
            return null;
        }
    }

    @Override
    public void setIndexProductPriceStr(List<IndexProduct> indexProductList) throws ApiException {

    }
}
