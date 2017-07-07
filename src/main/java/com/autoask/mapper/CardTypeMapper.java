package com.autoask.mapper;

import com.autoask.entity.info.CardTypeStatistic;
import com.autoask.entity.mysql.CardType;
import com.autoask.entity.mysql.OrderGoodsCard;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CardTypeMapper extends MyMapper<CardType> {

    Long countCardTypeNum(@Param("name") String name, @Param("productCategoryId") String productCategoryId,
                          @Param("productId") String productId, @Param("goodsId") String goodsId);

    List<String> getGoodsTypeIds(@Param("name") String name, @Param("productCategoryId") String productCategoryId,
                                 @Param("productId") String productId, @Param("goodsId") String goodsId,
                                 @Param("start") Integer start, @Param("limit") Integer limit);

    List<CardType> getGoodsTypeList(@Param("cardTypeIds") List<String> cardTypeIds);

    List<Map<String, Object>> getUseNumList(@Param("cardTypeIds") List<String> cardTypeIds);

    Long countCardTypeStatistic(@Param("cardTypeId") String cardTypeId);

    List<CardTypeStatistic> getCardTypeStatisticList(@Param("cardTypeId") String cardTypeId, @Param("start") Integer start, @Param("limit") Integer limit);

    CardType getCardTypeDetail(@Param("cardTypeId") String cardTypeId);
}