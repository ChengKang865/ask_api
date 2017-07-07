package com.autoask.service.impl.shopping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CodeGenerator;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.LoginUtil;
import com.autoask.entity.mysql.GoodsSnapshot;
import com.autoask.entity.mysql.Product;
import com.autoask.entity.mysql.ShoppingCartGoods;
import com.autoask.entity.mysql.User;
import com.autoask.mapper.GoodsSnapshotMapper;
import com.autoask.mapper.ShoppingCartGoodsMapper;
import com.autoask.service.product.GoodsService;
import com.autoask.service.product.ProductService;
import com.autoask.service.shopping.ShoppingCardService;
import com.autoask.service.user.UserService;
import com.ibm.icu.text.SimpleDateFormat;

import tk.mybatis.mapper.entity.Example;

/**
 * 购物车实现类
 * 
 * @author ck
 *
 * @Create 2017年5月23日下午2:07:21
 */
@Service
public class ShoppingCardServiceImpl implements ShoppingCardService {
	private static final Logger LOG = LoggerFactory.getLogger(ShoppingCardServiceImpl.class);

	@Autowired
	ShoppingCartGoodsMapper shoppingCartGoodsMapper;

	@Autowired
	GoodsSnapshotMapper goodsSnapshotMapper;

	@Autowired
	UserService userService;

	@Autowired
	GoodsService goodsService;
	
	@Autowired
	ProductService productService;

	@Override
	public void saveShopingCardGoods(String goodsSnapshotId,  Long shoppingCartNum) throws ApiException {
		LOG.info("save shopingCardGoods");
		try {
			String userId = LoginUtil.getSessionInfo().getLoginId();
			User user = userService.getUser(userId);
			if (user == null) {
				throw new ApiException("非法用户！");
			}
			if (StringUtils.isEmpty(goodsSnapshotId)) {
				throw new ApiException("商品快照id不能为空");
			}
			if(shoppingCartNum <= 0){
				throw new ApiException("商品数量不能小于0");
			}
			//根据快照id和当前用户id查询购物车是否有此商品
			ShoppingCartGoods scg = shoppingCartGoodsMapper.selectGoodsSnapshotById(goodsSnapshotId, user.getUserId());
			if(scg != null){
				//更新此商品
				shoppingCartGoodsMapper.updateShopingCardGoods(scg.getShoppingCartGoodsId(), scg.getShoppingCartNum()+shoppingCartNum, DateUtil.getDate());
			}else{
				ShoppingCartGoods shoppingCartGoods = new ShoppingCartGoods();
				// 快照id
				shoppingCartGoods.setGoodsSnapshotId(goodsSnapshotId);
				// 录入时间
				shoppingCartGoods.setCreateTime(DateUtil.getDate());
				// 购物车逻辑id
				String shoppingCartGoodsId = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
						+ CodeGenerator.getStringRandom(4);
				shoppingCartGoods.setShoppingCartGoodsId(shoppingCartGoodsId);
				// 删除标示
				shoppingCartGoods.setDeleteFlag(false);
				shoppingCartGoods.setUserId(user.getUserId());
				shoppingCartGoods.setShoppingCartNum(shoppingCartNum);
				shoppingCartGoodsMapper.insert(shoppingCartGoods);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			throw new ApiException(e.getMessage());
		}
	}

	@Override
	public void deleteShopingCardGoods(String shoppingCartGoodsId) throws ApiException {
		LOG.info("get By Id delete shoppingCartGoods");
		try {
			if (StringUtils.isEmpty(shoppingCartGoodsId)) {
				throw new ApiException("id不能为空");
			}
			ShoppingCartGoods scg = shoppingCartGoodsMapper.selectShoppingCartGoodsById(shoppingCartGoodsId);
			if (scg == null) {
				throw new ApiException("未查询到数据！");
			}
			String userId = LoginUtil.getSessionInfo().getLoginId();
			User user = userService.getUser(userId);
			if (user == null) {
				throw new ApiException("非法用户！");
			}
			if(!scg.getUserId().equals(user.getUserId())){
				throw new ApiException("登录用户不匹配");
			}
			Example typeExp = new Example(ShoppingCartGoods.class);
			typeExp.createCriteria().andEqualTo("shoppingCartGoodsId", shoppingCartGoodsId).andEqualTo("deleteFlag", 0);
			ShoppingCartGoods typeEntity = new ShoppingCartGoods();
			typeEntity.setDeleteFlag(true);
			typeEntity.setDeleteTime(DateUtil.getDate());
			shoppingCartGoodsMapper.updateByExampleSelective(typeEntity, typeExp);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			throw new ApiException(e.getMessage());
		}
	}

	@Override
	public void updateShopingCardGoods(String shoppingCartGoodsId, Long shoppingCartNum) throws ApiException {
		LOG.info("get By Id delete shoppingCartGoods");
		try {
			String userId = LoginUtil.getSessionInfo().getLoginId();
			User user = userService.getUser(userId);
			if (user == null) {
				throw new ApiException("非法用户！");
			}
			if (StringUtils.isEmpty(shoppingCartGoodsId)) {
				throw new ApiException("id不能为空");
			}
			if (shoppingCartNum <= 0) {
				throw new ApiException("购物车商品数量必须大于0");
			}
			ShoppingCartGoods scg = shoppingCartGoodsMapper.selectShoppingCartGoodsById(shoppingCartGoodsId);
			if (scg == null) {
				throw new ApiException("未查询到数据！");
			}
			if(!scg.getUserId().equals(user.getUserId())){
				throw new ApiException("登录用户不匹配");
			}
			// 根据快照id查询所属快照信息
			GoodsSnapshot goodsSnapshot = goodsService
								.getGoodsSnapShotById(scg.getGoodsSnapshotId());
			// 获取最新的商品快照信息
			GoodsSnapshot newGoodsSnapshot = goodsService.getGoodsSnapshotInfoByGoodsId(
					goodsSnapshot.getGoodsId(), Constants.GoodsStatus.CHECKED);
			// 判断快照id是否一样
			if (!newGoodsSnapshot.getGoodsSnapshotId().equals(scg.getGoodsSnapshotId())) {
				throw new ApiException("商品已经失效！");
			}
			shoppingCartGoodsMapper.updateShopingCardGoods(shoppingCartGoodsId,shoppingCartNum,DateUtil.getDate());
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			throw new ApiException(e.getMessage());
		}
	}

	@Override
	public List<ShoppingCartGoods> shoppingCartList() throws ApiException {
		List<ShoppingCartGoods> shoppingList = new ArrayList<ShoppingCartGoods>();
		LOG.info("get shoppingCartList");
		try {
			String userId = LoginUtil.getSessionInfo().getLoginId();
			User user = userService.getUser(userId);
			if (user == null) {
				throw new ApiException("非法用户！");
			}
			shoppingList = shoppingCartGoodsMapper.selectShoppingCardGoodsList(user.getUserId(), false);
			if (shoppingList.size() > 0) {
				for (int i = 0; i < shoppingList.size(); i++) {
					// 根据快照id查询所属快照信息
					GoodsSnapshot goodsSnapshot = goodsService
							.getGoodsSnapShotById(shoppingList.get(i).getGoodsSnapshotId());
					// 获取最新的商品快照信息
					GoodsSnapshot newGoodsSnapshot = goodsService.getGoodsSnapshotInfoByGoodsId(
							goodsSnapshot.getGoodsId(), Constants.GoodsStatus.CHECKED);
					// 判断快照id是否一样
					if (newGoodsSnapshot.getGoodsSnapshotId().equals(shoppingList.get(i).getGoodsSnapshotId())) {
						// 购物车商品是否失效 true:失效 false:正常
						shoppingList.get(i).setIsInvalid(false);
					} else {
						shoppingList.get(i).setIsInvalid(true);
					}
					String productId = goodsSnapshot.getProductId();
					if(!StringUtils.isEmpty(productId)){
						 Product product = productService.getProduct(productId);
						 if(product != null){
							 shoppingList.get(i).setProductCategoryId(product.getProductCategoryId());
							 if(product.getProductCategory() != null){
								 shoppingList.get(i).setCategoryServiceFee(product.getProductCategory().getServiceFee());
								 shoppingList.get(i).setProductCategoryName(product.getProductCategory().getName());
							 }
						 }
					}
					shoppingList.get(i).setGoodsSnapshot(goodsSnapshot);
				}
			} else {
				throw new ApiException("购物车为空！");
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			throw new ApiException(e.getMessage());
		}
		return shoppingList;
	}

	@Override
	public Long shoppingCartCount() throws ApiException {
		String userId = LoginUtil.getSessionInfo().getLoginId();
		User user = userService.getUser(userId);
		if (user == null) {
			throw new ApiException("非法用户！");
		}
		return shoppingCartGoodsMapper.selectShoppingCardGoodsCount(userId, false);
	}

}
