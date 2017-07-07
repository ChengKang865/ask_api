package com.autoask.mapper;

import com.autoask.entity.mysql.MerchantAssets;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface MerchantAssetsMapper extends MyMapper<MerchantAssets> {


    /**
     * 查询 商户资产，同时锁住商户的资产信息(for update)
     *
     * @param merchantType
     * @param merchantId
     * @return
     */
    MerchantAssets selectForLock(@Param("merchantType") String merchantType, @Param("merchantId") String merchantId);

    int updateBalance(@Param("merchantType") String merchantType, @Param("merchantId") String merchantId,
                      @Param("changeAmount") BigDecimal changeAmount);

    /**
     * 用户收入的时候需要累计 商户的资产
     *
     * @param merchantType
     * @param merchantId
     * @param changeAmount
     * @return
     */
    int incomeBalance(@Param("merchantType") String merchantType, @Param("merchantId") String merchantId, @Param("changeAmount") BigDecimal changeAmount);

    /**
     * 获取特定类型的商户的余额大于某个值
     * @param merchantType
     * @param balance
     * @param startId
     * @param limit
     * @return
     */
    List<MerchantAssets> selectByParam(@Param("merchantType") String merchantType, @Param("balance") BigDecimal balance,
                                       @Param("startId") Long startId, @Param("limit") int limit);


    /**
     * 获取符合条件的商户查询列表的数量
     *
     * @param merchantType
     * @param merchantIds
     * @return
     */
    Long countMerchantAssetsList(@Param("merchantType") String merchantType, @Param("merchantIds") List<String> merchantIds);

    List<MerchantAssets> getMerchantAssetsList(@Param("merchantType") String merchantType, @Param("merchantIds") List<String> merchantIds,
                                               @Param("start") int start, @Param("limit") int limit);
}