package com.autoask.service.product;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mysql.Goods;
import com.autoask.entity.mysql.GoodsSnapshot;
import com.autoask.entity.mysql.GoodsSnapshotInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by weid on 16-8-5.
 */
public interface GoodsService {

    void addGoodsSnapshot(GoodsSnapshot goodsSnapshot) throws ApiException;

    void updateGoodsSnapshot(GoodsSnapshot goodsSnapshot) throws ApiException;

    void deleteGoodsById(String goodsId) throws ApiException;

    List<GoodsSnapshot> getGoodsSnapshotList(Integer start, Integer length, Map<String, Object> queryParams) throws ApiException;

    List<Goods> getGoodsList(Integer start, Integer length, Map<String, Object> queryParams) throws ApiException;

    ListSlice<Goods> getGoodsList(Integer start, Integer length, String productCategoryId, String productId, String name) throws ApiException;

    void setGoodsSaleById(String goodsId) throws ApiException;

    void setGoodsNotSaleById(String goodsId) throws ApiException;

    Goods getGoodsById(String GoodsId) throws ApiException;

    Integer getGoodsCount(Map<String, Object> queryParams) throws ApiException;

    Integer getGoodsSnapshotCount(Map<String, Object> queryParams) throws ApiException;

    GoodsSnapshot getGoodsSnapShotById(String goodsSnapshotId) throws ApiException;

    void passGoodsSnapshot(String goodsSnapshotId) throws ApiException;

    void refuseGoodsSnapshot(String goodsSnapshotId) throws ApiException;

    GoodsSnapshot getGoodsSnapshotInfoByGoodsId(String goodsId, String status) throws ApiException;

    List<GoodsSnapshotInfo> getGoodsSnapshotInfoList(String goodsSnapshotId);
}
