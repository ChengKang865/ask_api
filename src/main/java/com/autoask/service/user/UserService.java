package com.autoask.service.user;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mongo.user.UserInfo;
import com.autoask.entity.mysql.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by weid on 16-9-13.
 */
public interface UserService {

    /**
     * 根据userId获取用户
     *
     * @param userId
     * @return
     * @throws ApiException
     */
    User getUser(String userId) throws ApiException;


    /**
     * 根据用户的手机号码获取用户
     *
     * @param phone
     * @return
     */
    User getUserByPhone(String phone);

    /**
     * 注册用户的同时 需要生成用户的 资产表
     * UserAsserts
     *
     * @param phone
     * @param recommendPhone
     * @return
     * @throws ApiException
     */
    User registerUser(String phone, String recommendPhone, JSONObject qrJson) throws ApiException;


    ListSlice getUserList(String phone, String startTime, String endTime, int start, int limit) throws ApiException;

    Map<String, User> getUserMap(List<String> userIdList);

    Map<String, UserInfo> getUserInfoMap(List<String> userIdList);

}
