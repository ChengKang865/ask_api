package com.autoask.mapper;

import com.autoask.entity.mysql.User;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends MyMapper<User> {

    Long countUserNum(@Param("phone") String phone, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<User> selectUserList(@Param("phone") String phone,
                              @Param("startTime") String startTime, @Param("endTime") String endTime,
                              @Param("start") int start, @Param("limit") int limit);


    /**
     * 统计 获取今天新增用户数量
     *
     * @return
     */
    Long getTodayRegisterNum();

    Long getTotalRegisterNum();

}