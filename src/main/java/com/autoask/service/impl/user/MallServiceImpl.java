package com.autoask.service.impl.user;

import com.alibaba.fastjson.JSON;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.dao.product.GoodsMetaDao;
import com.autoask.dao.product.GoodsSnapshotMetaDao;
import com.autoask.entity.mongo.product.GoodsMeta;
import com.autoask.entity.mongo.product.GoodsSnapshotMeta;
import com.autoask.entity.mongo.product.Meta;
import com.autoask.entity.mongo.product.ProductMeta;
import com.autoask.entity.mysql.Goods;
import com.autoask.entity.mysql.GoodsSnapshot;
import com.autoask.entity.mysql.GoodsSnapshotInfo;
import com.autoask.entity.mysql.Product;
import com.autoask.mapper.GoodsMapper;
import com.autoask.mapper.ProductMapper;
import com.autoask.service.user.MallService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp on 16-10-16.
 * 商城的 service
 */
@Service("mallService")
public class MallServiceImpl implements MallService {

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    GoodsSnapshotMetaDao goodsSnapshotMetaDao;

    @Override
    public Map<String, Object> getGoodsInfoMap(List<Goods> goodsList, Product product) throws ApiException {
        if (null == goodsList) {
            return null;
        }
        ArrayList<String> snapshotIdList = new ArrayList<>(goodsList.size());
        for (Goods goods : goodsList) {
            snapshotIdList.add(goods.getGoodsSnapshotId());
        }
        List<GoodsSnapshotMeta> goodsMetaList = goodsSnapshotMetaDao.find(Query.query(Criteria.where("goodsSnapshotId").in(snapshotIdList)));

        HashMap<String, GoodsSnapshotMeta> id2MetaMap = new HashMap<>(goodsMetaList == null ? 0 : goodsMetaList.size());
        if (CollectionUtils.isNotEmpty(goodsMetaList)) {
            for (GoodsSnapshotMeta snapshotMeta : goodsMetaList) {
                id2MetaMap.put(snapshotMeta.getGoodsSnapshotId(), snapshotMeta);
            }
        }

        Map<String, Object> goodsMap = new HashMap<>(goodsList.size());
        for (Goods goods : goodsList) {
            Map<String, Object> goodsMessage = new HashMap<>();
            GoodsSnapshot goodsSnapshot = goods.getGoodsSnapshot();
            goodsMessage.put("onLinePrice", goodsSnapshot.getOnlinePrice());
            goodsMessage.put("offLinePrice", goodsSnapshot.getOfflinePrice());
            goodsMessage.put("goodsId", goodsSnapshot.getGoodsId());
            goodsMessage.put("goodsSnapshotId", goodsSnapshot.getGoodsSnapshotId());
            goodsMessage.put("goodsName", goodsSnapshot.getName());
            goodsMessage.put("goodsNameEn", goodsSnapshot.getNameEn());
            goodsMessage.put("weight", goodsSnapshot.getWeight());
            goodsMessage.put("type", goodsSnapshot.getType());
            String picUrl = product.getLogoUrl();
            GoodsSnapshotMeta goodsSnapshotMeta = id2MetaMap.get(goodsSnapshot.getGoodsSnapshotId());
            if (null != goodsSnapshotMeta) {
                List<String> picUrlList = goodsSnapshotMeta.getPicUrlList();
                if (CollectionUtils.isNotEmpty(picUrlList)) {
                    goodsMessage.put("goodsPic", picUrlList.get(0));
                } else {
                    goodsMessage.put("goodsPic", picUrl);
                }
            } else {
                goodsMessage.put("goodsPic", picUrl);
            }


            List<GoodsSnapshotInfo> snapshotInfoList = goodsSnapshot.getGoodsSnapshotInfoList();
            Map<String, String> goodsLabel = new HashedMap();
            for (GoodsSnapshotInfo goodsSnapshotInfo : snapshotInfoList) {
                goodsLabel.put(goodsSnapshotInfo.getKeyName(), goodsSnapshotInfo.getValue());
            }
            goodsMessage.put("goodsLabel", goodsLabel);
            goodsMap.put(goods.getName(), goodsMessage);
        }

        return goodsMap;
    }

    /**
     * 获取价格界限
     *
     * @param goodsList goods 列表
     * @return Map
     * @throws ApiException
     */
    @Override
    public Map<String, String> getGoodsPriceRange(List<Goods> goodsList) throws ApiException {
        Map<String, String> goodsPriceMap = new HashMap<>();
        BigDecimal onLinePriceLow = new BigDecimal(0.0);
        BigDecimal onLinePriceHigh = new BigDecimal(0.0);
        BigDecimal offLinePriceLow = new BigDecimal(0.0);
        BigDecimal offLinePriceHigh = new BigDecimal(0.0);

        for (Goods goods : goodsList) {
            BigDecimal onlinePrice = goods.getOnlinePrice();
            BigDecimal offlinePrice = goods.getOfflinePrice();

            if (onlinePrice.compareTo(onLinePriceHigh) > 0) {
                onLinePriceHigh = onlinePrice;
            } else if (onlinePrice.compareTo(onLinePriceLow) > 0) {
                onLinePriceLow = onlinePrice;
            }

            if (offlinePrice.compareTo(offLinePriceHigh) > 0) {
                offLinePriceHigh = offlinePrice;
            } else if (offlinePrice.compareTo(offLinePriceLow) > 0) {
                offLinePriceLow = offlinePrice;
            }
        }

        String onLinePriceRange = String.format("%s-%s", onLinePriceLow, offLinePriceHigh);
        String offLinePriceRange = String.format("%s-%s", offLinePriceLow, offLinePriceHigh);
        goodsPriceMap.put("onLinePriceRange", onLinePriceRange);
        goodsPriceMap.put("offLinePriceRange", offLinePriceRange);

        return goodsPriceMap;
    }

    @Override
    public Map<String, Object> getProductMetaMap(Product product) throws ApiException {
        Map<String, Object> metaMap = new HashMap<>();
        ProductMeta productMeta = product.getProductMeta();
        if (null != productMeta) {
            List<Meta> metaList = productMeta.getMetaList();
            if (CollectionUtils.isNotEmpty(metaList)) {
                for (Meta meta : metaList) {
                    metaMap.put(meta.getName(), meta.getValueList());
                }
            }
        }
        return metaMap;
    }

    @Override
    public String getProductPriceStr(String productId) throws ApiException {
        return productMapper.getProductPriceStr(productId);
    }
}