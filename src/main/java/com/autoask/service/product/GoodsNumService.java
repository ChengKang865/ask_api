package com.autoask.service.product;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mongo.store.GoodsNumRecord;
import com.autoask.entity.mysql.GoodsNum;
import com.autoask.entity.mysql.OrderInfo;

import java.util.List;

/**
 * @author hyy
 * @create 2016-11-29 17:04
 */
public interface GoodsNumService {

    /**
     * 获取 商户的某个商品的数量
     *
     * @param merchantType
     * @param merchantId
     * @param goodsId
     * @return
     * @throws ApiException
     */
//    Long getGoodsNum(String merchantType, String merchantId, String goodsId) throws ApiException;


    /**
     * 获取库存
     *
     * @param merchantType
     * @param factoryId
     * @param productCategoryId
     * @param productId
     * @param goodsId
     * @param goodsName
     * @param start
     * @param limit
     * @return
     * @throws ApiException
     */
//   ListSlice getSubMerchantGoodsNumList(String merchantType, String factoryId,
//                                         String productCategoryId, String productId, String goodsId, String goodsName,
//                                         int start, int limit) throws ApiException;


//    void reduceGoodsNumByOrderList(List<OrderInfo> orderInfoList) throws ApiException;

//    void reduceGoodsNumByOrderId(String orderId) throws ApiException;

    //回滚库存
//    void updateGoodsNumBack(OrderInfo orderInfo) throws ApiException;

//    void updateGoodsNum(GoodsNumRecord goodsNumRecord) throws ApiException;

    //获取缺货的库存
//    List<GoodsNum> getLackingGoodsNum(String merchantType, Integer start, Integer limit) throws ApiException;
}
