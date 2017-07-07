package com.autoask.entity.mongo.user;

import com.autoask.entity.common.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Jack on 2016/10/22.
 * 顾客基本信息
 */
@Document
public class UserInfo {
    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    private String phone;

    private String avatar;

    private String gender;

    private String name;

    private String nickname;

    private String birthday;

    private List<CarModel> carModelList;

    private List<Address> addressList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public List<CarModel> getCarModelList() {
        return carModelList;
    }

    public void setCarModelList(List<CarModel> carModelList) {
        this.carModelList = carModelList;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UserInfo() {

    }

    public UserInfo(String phone, String avatar, String nickName, String gender, String birthday) {
        this.phone = phone;
        this.avatar = avatar;
        this.nickname = nickName;
        this.gender = gender;
        this.birthday = birthday;
    }
}
