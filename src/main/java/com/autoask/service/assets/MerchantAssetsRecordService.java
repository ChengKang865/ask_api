package com.autoask.service.assets;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hyy
 * @create 2016-10-27 17:12
 */
public interface MerchantAssetsRecordService {

    ListSlice selectMerchantAssetsRecordList(String merchantType, String factoryId, String serviceProviderId, String outletsId, String mechanicId,
                                             String orderId, String startTime, String endTime,
                                             int start, int limit) throws ApiException;

    void updateMerchantAssetsByOrder(String orderId) throws ApiException;

    BigDecimal countIncome(String merchantType, String merchantId, Date startTime, Date endTime) throws ApiException;
}