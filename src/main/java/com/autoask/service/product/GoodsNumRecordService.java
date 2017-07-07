package com.autoask.service.product;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mongo.store.GoodsNumRecord;

import java.util.Date;

/**
 * @author hyy
 * @create 2016-12-07 16:36
 */
public interface GoodsNumRecordService {

    ListSlice listGoodsNumRecord(String productCategoryId, String productId, String goodsId, String goodsName,
                                 String merchantType, String merchantId,
                                 Boolean addType, String serial, Date startTime, Date endTime, int start, int limit) throws ApiException;

    GoodsNumRecord getById(String id) throws ApiException;
}