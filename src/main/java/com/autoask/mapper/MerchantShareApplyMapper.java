package com.autoask.mapper;

import com.autoask.entity.mysql.MerchantShareApply;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by weid on 16-11-5.
 */
public interface MerchantShareApplyMapper extends MyMapper<MerchantShareApply> {

    List<MerchantShareApply> selectByParams(@Param("applyIds") List<String> applyIds, @Param("status") String status, @Param("batchNo") String batchNo);

    Long countApplyHistoryNum(@Param("merchantType") String merchantType, @Param("merchantId") String merchantId,
                              @Param("status") String status, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<MerchantShareApply> selectApplyHistory(@Param("merchantType") String merchantType, @Param("merchantId") String merchantId,
                                                @Param("status") String status, @Param("startTime") String startTime, @Param("endTime") String endTime,
                                                @Param("start") int start, @Param("limit") int limit);

    Long countApplyNum(@Param("merchantType") String merchantType, @Param("merchantId") String merchantId,
                       @Param("status") String status);

    int updateShareDoingBack(@Param("doingStatus") String doingStatus, @Param("applyingStatus") String applyStatus, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
