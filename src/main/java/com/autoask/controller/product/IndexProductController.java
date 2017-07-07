package com.autoask.controller.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.entity.mongo.config.Banner;
import com.autoask.entity.mongo.product.IndexProduct;
import com.autoask.entity.mysql.Product;
import com.autoask.service.product.IndexProductService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/product/indexProduct/")
public class IndexProductController {
    private static final Logger LOG = LoggerFactory.getLogger(IndexProductController.class);

    @Autowired
    private IndexProductService indexProductService;

    @RequestMapping(value = "update/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo updateIndexProduct(@RequestBody JSONObject paramJson) {
        LOG.info("update index product.");
        String channel = paramJson.getString("channel");
        String label = paramJson.getString("label");
        JSONArray jsonProductArr = paramJson.getJSONArray("indexProductList");

        List<IndexProduct> indexProductList = new ArrayList<>(jsonProductArr.size());
        for (int i = 0; i < jsonProductArr.size(); i++) {
            JSONObject jsonObject = jsonProductArr.getJSONObject(i);

            IndexProduct indexProduct = new IndexProduct();
            indexProduct.setChannel(channel);
            indexProduct.setLabel(label);
            indexProduct.setProductId(jsonObject.getString("productId"));
            indexProduct.setWeight(jsonObject.getInteger("weight"));
            if (StringUtils.equals(Constants.IndexProductLabel.HOT, label)) {
                indexProduct.setPicUrl(jsonObject.getString("picUrl"));
            }

            indexProductList.add(indexProduct);
        }
        try {
            indexProductService.updateIndexProductList(channel, label, indexProductList);
            return ResponseDo.buildSuccess("更新成功!");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getIndexProductList(@RequestParam(value = "channel") String channel,
                                          @RequestParam(value = "label") String label) {
        LOG.info("get index product list.");

        try {
            List<IndexProduct> indexProductList = indexProductService.getIndexProductList(channel, label);
            return ResponseDo.buildSuccess(indexProductList);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
