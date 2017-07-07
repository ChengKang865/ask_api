package com.autoask.controller.user.info;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.LoginUtil;
import com.autoask.entity.common.Address;
import com.autoask.entity.mongo.user.CarModel;
import com.autoask.entity.mongo.user.UserInfo;
import com.autoask.service.user.UserInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息查询、更改
 *
 * @author hyy
 * @create 16/10/9 00:04
 */
@Controller
@RequestMapping("user/info/")
public class UserInfoController {

    private static final Logger LOG = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 用户详情页
     *
     * @return
     */
    @RequestMapping(value = "all/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo all() {
        try {
            String userId = LoginUtil.getSessionInfo().getLoginId();
            return ResponseDo.buildSuccess(userInfoService.getByUserId(userId));
        } catch (Exception e) {
            LOG.error("Query userInfo failure", e);
            return ResponseDo.buildError(e.getMessage());
        }
    }


    /**
     * 保存用户的信息
     *
     * @return
     */
    @RequestMapping(value = "update/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo saveUser(@RequestBody JSONObject jsonObject) {
        String phone = jsonObject.getString("phone");
        String avatar = jsonObject.getString("avatar");
        String nickname = jsonObject.getString("nickname");
        String gender = jsonObject.getString("gender");
        String birthday = jsonObject.getString("birthday");
        try {
            UserInfo userInfo = new UserInfo(phone, avatar, nickname, gender, birthday);
            userInfoService.updateUserInfo(userInfo);
            return ResponseDo.buildSuccess("");
        } catch (Exception e) {
            LOG.error("Modify userInfo failure");
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 添加用户的收货地址
     *
     * @return
     */
    @RequestMapping(value = "address/add/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo saveUserAddress(@RequestBody JSONObject paramJson) {
        try {
            Address address = paramJson.getObject("address", Address.class);
            Integer defaultFlag = paramJson.getInteger("default");
            userInfoService.addUserAddress(address, defaultFlag);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 删除用户的收货地址
     *
     * @param paramJson
     * @return
     */
    @RequestMapping(value = "address/delete/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo deleteUserAddress(@RequestBody JSONObject paramJson) {
        Integer index = paramJson.getInteger("index");
        try {
            if (null == index) {
                throw new ApiException("参数非法");
            }
            userInfoService.deleteUserAddress(index);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 更新用户的收货地址
     *
     * @param addressList
     * @return
     */
    @RequestMapping(value = "address/update/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo updateUserAddress(@RequestBody List<Address> addressList) {
        try {
            userInfoService.updateUserAddress(addressList);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 用户的地址列表
     *
     * @return
     */
    @RequestMapping("address/list/")
    @ResponseBody
    public ResponseDo listAddress() {
        try {
            String userId = LoginUtil.getSessionInfo().getLoginId();
            UserInfo userInfo = userInfoService.getByUserId(userId);
            if (null == userInfo) {
                return ResponseDo.buildSuccess(new ArrayList<Address>());
            } else {
                if (CollectionUtils.isEmpty(userInfo.getAddressList())) {
                    return ResponseDo.buildSuccess(new ArrayList<>());
                }
                return ResponseDo.buildSuccess(userInfo.getAddressList());
            }

        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 用户的车型列表
     *
     * @return
     */
    @RequestMapping("car/list/")
    @ResponseBody
    public ResponseDo listCarModel() {
        try {
            String userId = LoginUtil.getSessionInfo().getLoginId();
            UserInfo userInfo = userInfoService.getByUserId(userId);
            if (null == userInfo) {
                return ResponseDo.buildSuccess(new ArrayList<>());
            } else {
                if (CollectionUtils.isEmpty(userInfo.getCarModelList())) {
                    return ResponseDo.buildSuccess(new ArrayList<>());
                }
                return ResponseDo.buildSuccess(userInfo.getCarModelList());
            }

        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }


    /**
     * 添加车型
     *
     * @param carModel
     * @return
     */
    @RequestMapping(value = "car/add/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo addCarModel(@RequestBody CarModel carModel) {
        try {
            userInfoService.addUserCarModel(carModel);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 更新车型
     *
     * @param carModelList
     * @return
     */
    @RequestMapping(value = "car/update/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo updateCarModel(@RequestBody List<CarModel> carModelList) {
        try {
            userInfoService.updateUserCarModel(carModelList);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 删除车型
     *
     * @param paramJson
     * @return
     */
    @RequestMapping(value = "car/delete/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo updateCarModel(@RequestBody JSONObject paramJson) {
        try {
            Integer index = paramJson.getInteger("index");
            if (null == index) {
                throw new ApiException("参数非法");
            }
            userInfoService.deleteUserCarModel(index);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }


    /**
     * 更新用户密码
     *
     * @param paramJson
     * @return
     */
    @RequestMapping(value = "password/reset/", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    @ResponseBody
    public ResponseDo resetPassword(@RequestBody JSONObject paramJson) {
        String password = paramJson.getString("password");
        try {
            userInfoService.updatePassword(password);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

}
