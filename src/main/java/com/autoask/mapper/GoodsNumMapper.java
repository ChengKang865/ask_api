package com.autoask.mapper;

import com.autoask.entity.mysql.GoodsNum;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by hyy on 2016/12/3.
 */
public interface GoodsNumMapper extends MyMapper<GoodsNum> {

    Long getGoodsNum(@Param("merchantType") String merchantType, @Param("merchantId") String merchantId, @Param("goodsId") String goodsId);

    int updateGoodsNum(@Param("merchantType") String merchantType, @Param("merchantId") String merchantId,
                       @Param("goodsId") String goodsId, @Param("addNum") Long addNum);

    Long countGoodsNumListSize(@Param("merchantType") String merchantType, @Param("merchantIds") List<String> merchantIds,
                               @Param("productCategoryId") String productCategoryId, @Param("productId") String productId,
                               @Param("goodsId") String goodsId, @Param("goodsName") String goodsName);

    List<GoodsNum> getGoodsNumList(@Param("merchantType") String merchantType, @Param("merchantIds") List<String> merchantIds,
                                   @Param("productCategoryId") String productCategoryId, @Param("productId") String productId,
                                   @Param("goodsId") String goodsId, @Param("goodsName") String goodsName, @Param("start") int start, @Param("limit") int limit);

    List<GoodsNum> getGoodsNumLackList(@Param("merchantType")String merchantType, @Param("merchantIds") List<String> merchantIds, @Param("count") Integer count,@Param("start") int start, @Param("limit") int limit);
}
