package com.autoask.controller.user;

import com.alibaba.fastjson.JSON;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.controller.user.IndexController;
import com.autoask.entity.mongo.product.Meta;
import com.autoask.entity.mongo.product.ProductMeta;
import com.autoask.entity.mysql.Goods;
import com.autoask.entity.mysql.Product;
import com.autoask.entity.mysql.ProductCategory;
import com.autoask.mapper.GoodsMapper;
import com.autoask.service.product.GoodsService;
import com.autoask.service.product.ProductCategoryService;
import com.autoask.service.product.ProductService;
import com.autoask.service.user.MallService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp on 16-10-16.
 * 商城物品详情页.
 */
@Controller
public class ProductDetailController {
    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private MallService mallService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "/productInfo/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo info(@RequestParam("productId") String productId) {
        try {
            Product product = productService.getProduct(productId);
            List<String> picUrlList = product.getProductMeta().getPicUrlList();
            List<String> infoUrlList = product.getProductMeta().getInfoUrlList();
            String h5DetailPicture = product.getProductMeta().getH5DetailPicture();
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("picUrlList", picUrlList);
            resultMap.put("infoUrlList", infoUrlList);
            resultMap.put("h5DetailPicture", h5DetailPicture);
            return ResponseDo.buildSuccess(resultMap);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }


    @RequestMapping(value = "/productDetail/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo detail(@RequestParam("productId") String productId) {

        try {
            Product product = productService.getProduct(productId);
            if (null == product) {
                throw new ApiException("产品信息缺失");
            }
            ProductCategory productCategory = productCategoryService.getProductCategory(product.getProductCategoryId());
            if (null == productCategory) {
                throw new ApiException("产品类别信息缺失");
            }
            ProductMeta productMeta = product.getProductMeta();
            List<String> picUrlList = new ArrayList<>();
            List<String> infoUrlList = new ArrayList<>();
            List<String> pcBuyUrlList = new ArrayList<>();
            List<String> h5BuyUrlList = new ArrayList<>();
            String h5DetailPicture = null;
            if (null != productMeta) {
                if (null != productMeta.getPicUrlList()) {
                    picUrlList = product.getProductMeta().getPicUrlList();
                }
                if (null != productMeta.getInfoUrlList()) {
                    infoUrlList = product.getProductMeta().getInfoUrlList();
                }
                if (null != productMeta.getPcBuyUrlList()) {
                    pcBuyUrlList = product.getProductMeta().getPcBuyUrlList();
                }
                if (null != productMeta.getH5BuyUrlList()) {
                    h5BuyUrlList = product.getProductMeta().getH5BuyUrlList();
                }
                h5DetailPicture = productMeta.getH5DetailPicture();
            }
            List<Goods> goodsList = goodsMapper.getGoodsInfoByProductId(productId);
            Map<String, Object> goodsMap = mallService.getGoodsInfoMap(goodsList,product);
            Map<String, Object> productMetaMap = mallService.getProductMetaMap(product);
            List<Product> recommendProductList = productService.getRandom3ProductList(productId);


            Map<String, Object> resultMap = new HashedMap();

            resultMap.put("goodsMap", goodsMap);
            String priceStr = mallService.getProductPriceStr(productId);
            resultMap.put("priceStr", priceStr);
            resultMap.put("productName", product.getName());
            resultMap.put("productNameEn", product.getNameEn());
            resultMap.put("metaMap", productMetaMap);
            resultMap.put("productId", product.getProductId());
            resultMap.put("recommendProductList", recommendProductList);
            resultMap.put("picUrlList", picUrlList);
            resultMap.put("infoUrlList", infoUrlList);
            resultMap.put("pcBuyUrlList", pcBuyUrlList);
            resultMap.put("h5BuyUrlList", h5BuyUrlList);
            resultMap.put("productCategoryName", productCategory.getName());
            resultMap.put("h5DetailPicture", h5DetailPicture);
            resultMap.put("productCategoryId", productCategory.getProductCategoryId());
            resultMap.put("categoryServiceFee", productCategory.getServiceFee());

            return ResponseDo.buildSuccess(resultMap);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}