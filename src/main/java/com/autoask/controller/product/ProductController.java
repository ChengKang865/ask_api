package com.autoask.controller.product;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.HttpRequestUtil;
import com.autoask.controller.common.BaseController;
import com.autoask.entity.mysql.Product;
import com.autoask.service.product.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weid on 16-8-3.
 */
@Controller
@RequestMapping(value = "/product/product")
public class ProductController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/get/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getProductById(@RequestParam(value = "id") String productId) {
        LOG.debug("get product by id");

        try {
            Product product = productService.getProduct(productId);
            return ResponseDo.buildSuccess(product);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getProductList(@RequestParam(value = "productCategoryId", required = false) String productCategoryId) {
        LOG.debug("get product list");
        try {

            List<Product> productList = productService.getProductList(productCategoryId);
            Integer productCount = productService.getProductCount(productCategoryId);

            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("total", productCount);
            stringObjectMap.put("result", productList);
            return ResponseDo.buildSuccess(stringObjectMap);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/add/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo addProduct(Product product) {
        LOG.debug("add product");

        try {
            productService.addProduct(product);
            return ResponseDo.buildSuccess("添加成功");
        } catch (ApiException e) {
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/delete/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo deleteProduct(@RequestParam(value = "id") String productId) {
        LOG.debug("add product");
        if (StringUtils.isEmpty(productId)) {
            return ResponseDo.buildError("商品id号为空");
        }
        try {
            productService.deleteProduct(productId);
            return ResponseDo.buildSuccess("删除成功");
        } catch (ApiException e) {
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updateProduct(Product product) {
        LOG.debug("add product");

        try {
            productService.updateProduct(product);
            return ResponseDo.buildSuccess("更新成功");
        } catch (ApiException e) {
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 购物车的链接
     *
     * @return
     * @throws ApiException
     */
    @RequestMapping(value = "/shoppingCart/")
    public ModelAndView shoppingCart() throws ApiException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("m/shopping_cart");
        return modelAndView;
    }
}
