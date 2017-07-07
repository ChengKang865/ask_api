package com.autoask.service.merchant;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.exception.ApiException;
import com.autoask.entity.mysql.MerchantAssets;

/**
 * Created by weid on 16-9-27.
 */
public interface MerchantAssetsService {

    MerchantAssets getMerchantAssets(String merchantType, String merchantId);

    /**
     * 创建MerchantAssets
     */
    void addMerchantAssets(String merchantType, String merchantId) throws ApiException;

    /**
     * 更新商户的红包信息
     *
     * @param json
     * @return
     */
    void updateMerchantBonusRecord(JSONObject json) throws ApiException;
}
