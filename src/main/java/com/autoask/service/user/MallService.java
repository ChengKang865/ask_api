package com.autoask.service.user;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mysql.Goods;
import com.autoask.entity.mysql.Product;

import java.util.List;
import java.util.Map;

/**
 * Created by hp on 16-10-16.
 */
public interface MallService {

    Map<String,Object> getGoodsInfoMap(List<Goods> goodsList,Product product) throws ApiException;

    Map<String, String> getGoodsPriceRange(List<Goods> goodsList) throws ApiException;

    Map<String, Object> getProductMetaMap(Product product) throws ApiException;

    String getProductPriceStr(String productId) throws ApiException;
}
