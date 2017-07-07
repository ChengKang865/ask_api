package com.autoask.mapper;

import com.autoask.entity.mysql.Goods;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GoodsMapper extends MyMapper<Goods> {

    List<Goods> getGoodsList(@Param("productName") String productName, @Param("creatorId") String creatorId);

    Double getMinOnlineProductGoodsPrice(@Param("productId") String productId);

    Double getMinOfflineProductGoodsPrice(@Param("productId") String productId);

    Integer getOnSellGoodsCountByProductId(@Param("productId") String productId);

    List<Goods> getGoodsInfoByProductId(@Param("productId") String productId);

    List<Goods> getAllGoodsList(@Param("productId") String productId);

    //搜索商品
    List<String> getGoodsIdList(@Param("productCategoryId") String productCategoryId, @Param("productId") String productId,
                                @Param("goodsId") String goodsId, @Param("goodsName") String goodsName);

    //统计待审核的商品的数量
    int getToCheckGoodsNum();
}
