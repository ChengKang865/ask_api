package com.autoask.service.shopping;

import java.util.List;
import java.util.Map;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mysql.ShoppingCartGoods;
/**
 * 购物车下所属商品信息
 * @author ck
 *
 * @Create 2017年5月23日下午1:36:23
 */
public interface ShoppingCardService {
	
	/**
	 * 购物车下商品增加
	 * @param shoppingCartGoods
	 * @throws ApiException
	 */
	void saveShopingCardGoods(String goodsSnapshotId, Long shoppingCartNum)throws ApiException;
	
	/**
	 * 删除购物车下面商品
	 * @param shoppingCartGoodsId
	 * @throws ApiException
	 */
	void deleteShopingCardGoods(String shoppingCartGoodsId)throws ApiException;
	
	/**
	 * 购物车下商品修改
	 * @param shoppingCartGoods
	 * @throws ApiException
	 */
	void updateShopingCardGoods(String shoppingCartGoodsId, Long shoppingCartNum)throws ApiException;
	
	/**
	 * 购物车列表
	 * @param shoppingCart
	 * @return
	 * @throws ApiException
	 */
	List<ShoppingCartGoods> shoppingCartList()throws ApiException;
	
	/**
	 * 购物车条数
	 * @throws ApiException
	 */
	Long shoppingCartCount()throws ApiException;

}
