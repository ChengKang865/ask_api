package com.autoask.service.impl.user;

import com.alibaba.fastjson.JSONObject;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CodeGenerator;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.dao.merchant.MechanicDao;
import com.autoask.dao.merchant.OutletsDao;
import com.autoask.dao.merchant.ServiceProviderDao;
import com.autoask.dao.user.UserInfoDao;
import com.autoask.entity.mongo.merchant.*;
import com.autoask.entity.mongo.user.UserInfo;
import com.autoask.entity.mysql.User;
import com.autoask.entity.mysql.UserAssets;
import com.autoask.mapper.UserAssetsMapper;
import com.autoask.mapper.UserMapper;
import com.autoask.service.merchant.*;
import com.autoask.service.user.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.javassist.compiler.CodeGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weid on 16-9-13.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAssetsMapper userAssetsMapper;

    @Autowired
    private OutletsService outletsService;

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private FactoryService factoryService;

    @Autowired
    private UserInfoDao userInfoDao;


    @Override
    public User getUser(String userId) throws ApiException {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("userId", userId);

        List<User> userList = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(userList)) {
            throw new ApiException("找不到对应用户");
        }
        User user = userList.get(0);
        return user;
    }

    @Override
    public User getUserByPhone(String phone) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("phone", phone);
        List<User> userList = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    @Override
    public User registerUser(String phone, String recommendPhone, JSONObject qrJson) throws ApiException {
        if (null != getUserByPhone(phone)) {
            throw new ApiException("该用户已经注册,请登录");
        }

        String userId = CodeGenerator.uuid();
        User user = new User();
        user.setUserId(userId);
        user.setPhone(phone);
        if (StringUtils.isNotEmpty(recommendPhone)) {
            user.setRecommendPhone(recommendPhone);
            User recommendUser = getUserByPhone(recommendPhone);
            if (null != recommendUser) {
                user.setRecommendUserId(recommendUser.getUserId());
            }
        }

        if (null != qrJson && qrJson.containsKey(Constants.QRInfoId.OUTLETS_ID)) {
            //分销点引流
            String outletsId = qrJson.getString(Constants.QRInfoId.OUTLETS_ID);
            Outlets outlets = outletsService.findById(outletsId);
            if (null != outlets) {
                user.setPromoteType(Constants.MerchantType.OUTLETS);
                user.setPromoteId(outlets.getId());
            }
        } else if (null != qrJson && qrJson.containsKey(Constants.QRInfoId.MECHANIC_ID)) {
            //修理厂引流 扫码修理工的二维码
            String mechanicId = qrJson.getString(Constants.QRInfoId.MECHANIC_ID);
            Mechanic mechanic = mechanicService.findById(mechanicId);
            if (null != mechanic) {
                user.setPromoteType(Constants.MerchantType.SERVICE_PROVIDER);
                user.setPromoteId(mechanic.getServiceProviderId());
            }
        } else if (null != qrJson && qrJson.containsKey(Constants.QRInfoId.SERVICE_PROVIDER_ID)) {
            //修理厂引流
            String serviceProviderId = qrJson.getString(Constants.QRInfoId.SERVICE_PROVIDER_ID);
            ServiceProvider serviceProvider = serviceProviderService.findById(serviceProviderId);
            if (null != serviceProvider) {
                user.setPromoteType(Constants.MerchantType.SERVICE_PROVIDER);
                user.setPromoteId(serviceProviderId);
            }
        }  else if (null != qrJson && qrJson.containsKey(Constants.QRInfoId.FACTORY_ID)) {
            String factoryId = qrJson.getString(Constants.QRInfoId.FACTORY_ID);
            Factory factory = factoryService.findById(factoryId);
            if (null != factory) {
                user.setPromoteType(Constants.MerchantType.FACTORY);
                user.setPromoteId(factoryId);
            }
        }

        user.setDeleteFlag(false);
        user.setCreateTime(DateUtil.getDate());
        user.setModifyTime(DateUtil.getDate());

        userMapper.insert(user);

        UserAssets userAssets = new UserAssets();
        userAssets.setUserId(user.getUserId());
        userAssets.setBalance(new BigDecimal(0));
        userAssets.setIntegration(0L);
        userAssets.setCreateTime(DateUtil.getDate());
        userAssets.setModifyTime(DateUtil.getDate());
        userAssets.setVersion(0L);

        userAssetsMapper.insert(userAssets);

        return user;
    }

    @Override
    public ListSlice getUserList(String phone, String startTime, String endTime, int start, int limit) throws ApiException {

        Long totalNum = userMapper.countUserNum(phone, startTime, endTime);
        List<User> userList = userMapper.selectUserList(phone, startTime, endTime, start, limit);

        //初始化对应的mongodb中得userInfo信息
        if (CollectionUtils.isNotEmpty(userList)) {
            List<String> userIds = new ArrayList<>(userList.size());
            for (User user : userList) {
                userIds.add(user.getUserId());
            }
            List<UserInfo> userInfoList = userInfoDao.find(Query.query(Criteria.where("userId").in(userIds)));
            if (CollectionUtils.isNotEmpty(userInfoList)) {
                HashMap<String, UserInfo> id2InfoMap = new HashMap<>(userInfoList.size());
                for (UserInfo userInfo : userInfoList) {
                    id2InfoMap.put(userInfo.getUserId(), userInfo);
                }

                for (User user : userList) {
                    user.setUserInfo(id2InfoMap.get(user.getUserId()));
                }
            }

        }

        //查询mongodb中得user
        return new ListSlice(userList, totalNum);
    }

    @Override
    public Map<String, User> getUserMap(List<String> userIdList) {
        Example example = new Example(User.class);
        example.createCriteria().andIn("userId", userIdList);
        List<User> userList = userMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(userList)) {
            HashMap<String, User> userMap = new HashMap<>(userList.size());
            for (User user : userList) {
                userMap.put(user.getUserId(), user);
            }
            return userMap;
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, UserInfo> getUserInfoMap(List<String> userIdList) {
        List<UserInfo> userInfoList = userInfoDao.find(Query.query(Criteria.where("userId").in(userIdList)));
        if (CollectionUtils.isNotEmpty(userInfoList)) {
            HashMap<String, UserInfo> userInfoMap = new HashMap<>(userInfoList.size());
            for (UserInfo userInfo : userInfoList) {
                userInfoMap.put(userInfo.getUserId(), userInfo);
            }
            return userInfoMap;
        }
        return new HashMap<>();
    }
}
