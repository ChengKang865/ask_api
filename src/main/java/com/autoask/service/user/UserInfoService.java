package com.autoask.service.user;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.common.Address;
import com.autoask.entity.mongo.user.CarModel;
import com.autoask.entity.mongo.user.UserInfo;
import com.autoask.service.BaseMongoService;

import java.util.List;

/**
 * Created by Administrator on 2016/10/22.
 */
public interface UserInfoService {

    /**
     * 根据userId获取用户信息
     *
     * @param userId
     * @return
     */
    UserInfo getByUserId(String userId) throws ApiException;

    UserInfo updateUserInfo(UserInfo userInfo) throws ApiException;


    void addUserAddress(Address address, Integer defaultFlag) throws ApiException;

    void deleteUserAddress(Integer index) throws ApiException;

    void updateUserAddress(List<Address> addressList) throws ApiException;

    void addUserCarModel(CarModel carModel) throws ApiException;

    void updateUserCarModel(List<CarModel> carModels) throws ApiException;

    void deleteUserCarModel(Integer index) throws ApiException;

    void updatePassword(String password) throws ApiException;

}
