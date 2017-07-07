package com.autoask.mapper;

import com.autoask.entity.mysql.MerchantAssetsRecord;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface MerchantAssetsRecordMapper extends MyMapper<MerchantAssetsRecord> {

    Long countMerchantAssetsRecord(@Param("merchantType") String merchantType, @Param("merchantIds") List<String> merchantIds, @Param("orderId") String orderId,
                                   @Param("startTime") String startTime, @Param("endTime") String endTime);


    //TODO
    List<MerchantAssetsRecord> selectMerchantAssetsRecordList(@Param("merchantType") String merchantType, @Param("merchantIds") List<String> merchantIds, @Param("orderId") String orderId,
                                                              @Param("startTime") String startTime, @Param("endTime") String endTime,
                                                              @Param("start") int start, @Param("limit") int limit);

    BigDecimal countIncome(@Param("merchantType") String merchantType, @Param("merchantId") String merchantId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}