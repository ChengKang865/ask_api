package com.autoask.service.assets;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;

/**
 * @author hyy
 * @create 2016-10-27 17:11
 */
public interface MerchantAssetsService {

    ListSlice selectMerchantAssetsList(String merchantType,String name, int start, int limit) throws ApiException;
}
