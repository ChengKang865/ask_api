package com.autoask.controller.product;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.HttpRequestUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.controller.common.BaseController;
import com.autoask.entity.mysql.ProductCategory;
import com.autoask.service.product.ProductCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp on 16-8-4.
 */

@Controller
@RequestMapping(value = "/product/category")
public class ProductCategoryController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCategoryController.class);

    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "/get/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getProductCategory(@RequestParam(value = "id") String productCategoryId) {
        LOG.debug("get product category by id");

        if (StringUtils.isEmpty(productCategoryId)) {
            return ResponseDo.buildError("商品种类id号为空");
        }
        try {
            ProductCategory productCategory = productCategoryService.getProductCategory(productCategoryId);
            return ResponseDo.buildSuccess(productCategory);
        } catch (ApiException e) {
            LOG.warn(e.getMessage(), e);
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getProductCategoryList(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                             @RequestParam(value = "count", defaultValue = "0") Integer count) {
        LOG.debug("get product category list");
        try {
            Integer limit = CommonUtil.cleanCount(count);
            Integer start = CommonUtil.pageToSkipStart(page, count);

            ListSlice productCategoryList = productCategoryService.getProductCategoryList(start, limit);

            return ResponseDo.buildSuccess(productCategoryList);
        } catch (ApiException e) {
            LOG.warn(e.getMessage(), e);
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updateProductCategory(ProductCategory productCategory) {
        LOG.debug("update product category");

        if (null == productCategory) {
            return ResponseDo.buildError("商品种类为空");
        }

        try {
            productCategoryService.updateProductCategory(productCategory);
            return ResponseDo.buildSuccess("更新商品种类成功!");
        } catch (ApiException e) {
            LOG.warn(e.getMessage(), e);
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/delete/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo deleteProductCategory(@RequestParam(value = "id") String productCategoryId) {
        LOG.debug("delete product category");

        if (StringUtils.isEmpty(productCategoryId)) {
            return ResponseDo.buildError("商品种类id号为空");
        }
        try {
            productCategoryService.deleteProductCategory(productCategoryId);
            return ResponseDo.buildSuccess("删除商品种类成功");
        } catch (ApiException e) {
            LOG.warn(e.getMessage(), e);
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/add/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo addProductCategory(ProductCategory productCategory) {
        LOG.debug("add product category");

        if (null == productCategory) {
            return ResponseDo.buildError("商品种类为空");
        }

        try {
            productCategoryService.addProductCategory(productCategory);
            return ResponseDo.buildSuccess("添加商品种类成功");
        } catch (ApiException e) {
            LOG.warn(e.getMessage(), e);
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
