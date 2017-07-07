package com.autoask.mapper;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mysql.UserAssets;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface UserAssetsMapper extends MyMapper<UserAssets> {

    UserAssets selectForLock(@Param("userId") String userId);

    int updateBalance(@Param("userId") String userId, @Param("changeAmount") BigDecimal changeAmount);

}
