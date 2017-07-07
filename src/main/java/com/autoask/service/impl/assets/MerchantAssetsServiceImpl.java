package com.autoask.service.impl.assets;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.LoginUtil;
import com.autoask.entity.mongo.merchant.BaseMerchant;
import com.autoask.entity.mysql.MerchantAssets;
import com.autoask.mapper.MerchantAssetsMapper;
import com.autoask.service.assets.MerchantAssetsService;
import com.autoask.service.merchant.MerchantService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hyy
 * @create 16/11/6 17:21
 */
@Service
public class MerchantAssetsServiceImpl implements MerchantAssetsService {

    @Autowired
    private MerchantAssetsMapper merchantAssetsMapper;

    @Autowired
    private MerchantService merchantService;

    @Override
    public ListSlice selectMerchantAssetsList(String merchantType, String name, int start, int limit) throws ApiException {
        if (!StringUtils.equals(LoginUtil.getLoginType(), Constants.LoginType.STAFF)) {
            throw new ApiException("没有权限");
        }
        List<String> merchantIds = null;
        if (StringUtils.isNotEmpty(name)) {
            merchantIds = merchantService.searchMerchantIdByName(merchantType, name);
            if (CollectionUtils.isEmpty(merchantIds)) {
                return null;
            }
        }

        Long totalNum = merchantAssetsMapper.countMerchantAssetsList(merchantType, merchantIds);
        List<MerchantAssets> merchantAssetsList = merchantAssetsMapper.getMerchantAssetsList(merchantType, merchantIds, start, limit);
        if (CollectionUtils.isNotEmpty(merchantAssetsList)) {
            ArrayList<BaseMerchant> baseMerchantList = new ArrayList<>(merchantAssetsList.size());
            for (MerchantAssets merchantAssets : merchantAssetsList) {
                BaseMerchant baseMerchant = new BaseMerchant();
                baseMerchant.setLoginType(merchantAssets.getMerchantType());
                baseMerchant.setId(merchantAssets.getMerchantId());
                baseMerchantList.add(baseMerchant);
            }
            Map<String, String> merchantNameMap = merchantService.getMerchantNameMap(baseMerchantList);
            for (MerchantAssets merchantAssets : merchantAssetsList) {
                merchantAssets.setMerchantName(merchantNameMap.get(merchantAssets.getMerchantType() + merchantAssets.getMerchantId()));
            }
        }
        ListSlice<MerchantAssets> resultList = new ListSlice<>(merchantAssetsList, totalNum);
        return resultList;
    }
}
