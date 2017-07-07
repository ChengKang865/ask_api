package com.autoask.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.autoask.entity.mysql.ShoppingCartGoods;
import com.autoask.mapper.base.MyMapper;

public interface ShoppingCartGoodsMapper extends MyMapper<ShoppingCartGoods> {
	List<ShoppingCartGoods> selectShoppingCardGoodsList(@Param("userId") String userId,@Param("deleteFlag") Boolean deleteFlag);
	
	ShoppingCartGoods selectShoppingCartGoodsById(@Param("shoppingCartGoodsId") String shoppingCartGoodsId);
	
	int updateShopingCardGoods(@Param("shoppingCartGoodsId")String shoppingCartGoodsId,@Param("shoppingCartNum")Long shoppingCartNum,@Param("lastUpdateTime")Date lastUpdateTime);
	
	ShoppingCartGoods selectGoodsSnapshotById(@Param("goodsSnapshotId") String goodsSnapshotId, @Param("userId") String userId);
	
	Long selectShoppingCardGoodsCount(@Param("userId") String userId,@Param("deleteFlag") Boolean deleteFlag);
}