package com.autoask.controller.shopping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.controller.common.BaseController;
import com.autoask.service.shopping.ShoppingCardService;

/**
 * 购物车控制器
 * 
 * @author ck
 *
 * @Create 2017年5月23日下午4:06:40
 */
@Controller
@RequestMapping("user/shoppingCard/")
public class ShoppingCardController extends BaseController {
	private static Logger LOG = LoggerFactory.getLogger(ShoppingCardController.class);

	@Autowired
	private ShoppingCardService shoppingCardService;

	/**
	 * 用户所属购物车列表
	 * @return
	 */
	@RequestMapping(value = "shoppingCartGoodsList/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseDo shoppingCartGoodsList() {
		try {
			return ResponseDo.buildSuccess(shoppingCardService.shoppingCartList());
		} catch (ApiException e) {
			LOG.error(e.getMessage());
			return ResponseDo.buildError(e.getMessage());
		}
	}
	
	/**
	 * 购物车条数
	 * @return
	 */
	@RequestMapping(value = "shoppingCartGoodsCount/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseDo shoppingCartGoodsCount() {
		try {
			return ResponseDo.buildSuccess(shoppingCardService.shoppingCartCount());
		} catch (ApiException e) {
			LOG.error(e.getMessage());
			return ResponseDo.buildError(e.getMessage());
		}
	}
	
	/**
	 * 增加购物车
	 * @param paramJson
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "saveShoppingCartGoods/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
	@ResponseBody
	public ResponseDo saveShoppingCartGoods(@RequestBody JSONObject paramJson,
            HttpServletRequest request, HttpServletResponse response){
		try {
			String goodsSnapshotId = paramJson.getString("goodsSnapshotId");
			Long shoppingCartNum = paramJson.getLong("shoppingCartNum");
			shoppingCardService.saveShopingCardGoods(goodsSnapshotId, shoppingCartNum);
			return ResponseDo.buildSuccess("购物车商品增加成功！");
		} catch (ApiException e) {
			LOG.error(e.getMessage());
			return ResponseDo.buildError(e.getMessage());
		}
	}
	
	/**
	 * 删除
	 * @param paramJson
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "deleteShoppingCartGoods/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
	@ResponseBody
	public ResponseDo deleteShoppingCartGoods(@RequestBody JSONObject paramJson,
            HttpServletRequest request, HttpServletResponse response){
		try {
			String shoppingCartGoodsId = paramJson.getString("shoppingCartGoodsId");
			shoppingCardService.deleteShopingCardGoods(shoppingCartGoodsId);
			return ResponseDo.buildSuccess("购物车商品删除成功！");
		} catch (ApiException e) {
			LOG.error(e.getMessage());
			return ResponseDo.buildError(e.getMessage());
		}
	}
	
	@RequestMapping(value = "updateShoppingCartGoods/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
	@ResponseBody
	public ResponseDo updateShoppingCartGoods(@RequestBody JSONObject paramJson,
            HttpServletRequest request, HttpServletResponse response){
		try {
			String shoppingCartGoodsId = paramJson.getString("shoppingCartGoodsId");
			Long shoppingCartNum = paramJson.getLong("shoppingCartNum");
			shoppingCardService.updateShopingCardGoods(shoppingCartGoodsId, shoppingCartNum);
			return ResponseDo.buildSuccess("购物车商品更新成功！");
		} catch (ApiException e) {
			LOG.error(e.getMessage());
			return ResponseDo.buildError(e.getMessage());
		}
	}
}
