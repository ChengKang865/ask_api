package com.autoask.controller.user;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.entity.mongo.config.Banner;
import com.autoask.entity.mongo.config.RichTextConfig;
import com.autoask.entity.mongo.product.IndexProduct;
import com.autoask.entity.mysql.Product;
import com.autoask.service.config.ConfigService;
import com.autoask.service.config.RichTextConfigService;
import com.autoask.service.product.IndexProductService;
import com.autoask.service.product.ProductCategoryService;
import com.autoask.service.product.ProductService;
import com.autoask.service.qr.QRCodeService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weid on 16-10-15.
 */
@Controller
@RequestMapping("index/")
public class IndexController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IndexProductService indexProductService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RichTextConfigService richTextConfigService;

    @Autowired
    private QRCodeService qrCodeService;

    @RequestMapping(method = RequestMethod.GET)
    public void qrCode(@RequestParam("code") String code, HttpServletResponse response) {
        String redirectUrl = qrCodeService.getQRCodeRedirect(code);
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }


    @RequestMapping(value = "pc/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo pcIndexController() {
        try {
            HashMap<String, Object> resultMap = new HashMap<>();

            List<Banner> bannerList = configService.getBannerList(Constants.BannerChannel.PC);
            resultMap.put("bannerList", bannerList);

            List<IndexProduct> newGenIndexProductList = indexProductService.getIndexProductList(Constants.IndexProductChannel.PC
                    , Constants.IndexProductLabel.NEW_GEN);

            List<IndexProduct> classicIndexProductList = indexProductService.getIndexProductList(Constants.IndexProductChannel.PC
                    , Constants.IndexProductLabel.CLASSIC);

            List<IndexProduct> hotIndexProductList = indexProductService.getIndexProductList(Constants.IndexProductChannel.PC
                    , Constants.IndexProductLabel.HOT);

            List<IndexProduct> classicOtherList = indexProductService.getIndexProductList(Constants.IndexProductChannel.PC, Constants.IndexProductLabel.CLASSIC_OTHERS);

            resultMap.put("newGenList", newGenIndexProductList);

            resultMap.put("classicList", classicIndexProductList);

            resultMap.put("hotList", hotIndexProductList);

            resultMap.put("classicOtherList", classicOtherList);

            return ResponseDo.buildSuccess(resultMap);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "m/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo m() {
        try {
            HashMap<String, Object> resultMap = new HashMap<>();

            List<Banner> bannerList = configService.getBannerList(Constants.BannerChannel.M);
            resultMap.put("bannerList", bannerList);

            List<IndexProduct> newGenProductList = indexProductService.getIndexProductList(Constants.IndexProductChannel.M, Constants.IndexProductLabel.NEW_GEN);
            resultMap.put("newGenList", newGenProductList);

            List<IndexProduct> classicProductList = indexProductService.getIndexProductList(Constants.IndexProductChannel.M, Constants.IndexProductLabel.CLASSIC);
            resultMap.put("classicList", classicProductList);

            List<IndexProduct> classicOtherList = indexProductService.getIndexProductList(Constants.IndexProductChannel.M, Constants.IndexProductLabel.CLASSIC_OTHERS);
            resultMap.put("classicOtherList", classicOtherList);

            return ResponseDo.buildSuccess(resultMap);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "category/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo category() {
        List<Map<String, Object>> resultList = productCategoryService.getIndexProductCategoryList();
        return ResponseDo.buildSuccess(resultList);
    }

    @RequestMapping(value = "search/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo search(@RequestParam("content") String content) {

        try {
            List<Product> productList = productService.searchProductByName(content);
            if (CollectionUtils.isNotEmpty(productList)) {
                ArrayList<Map<String, Object>> resultList = new ArrayList<>(productList.size());

                for (Product product : productList) {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("name", product.getName());
                    itemMap.put("productId", product.getProductId());
                    resultList.add(itemMap);
                }
                return ResponseDo.buildSuccess(resultList);
            } else {
                return ResponseDo.buildSuccess(new ArrayList<>());
            }
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/richText/list/", method = RequestMethod.GET)
    public ResponseDo richTextList(@RequestParam(value = "type") String type,
                                   @RequestParam(value = "channel", required = false) String channel,
                                   @RequestParam(value = "title", required = false) String title) {

        List<RichTextConfig> richTextConfigList = richTextConfigService.queryConfigs(type, channel, title);
        if (CollectionUtils.isEmpty(richTextConfigList)) {
            return ResponseDo.buildSuccess(new ArrayList<>());
        } else {
            return ResponseDo.buildSuccess(richTextConfigList);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/richText/detail/", method = RequestMethod.GET)
    public ResponseDo richTestDetail(@RequestParam(value = "id") String id) {
        RichTextConfig richTextConfig = richTextConfigService.getConfigById(id);
        if (null == richTextConfig) {
            return ResponseDo.buildSuccess("{}");
        } else {
            return ResponseDo.buildSuccess(richTextConfig);
        }
    }
}