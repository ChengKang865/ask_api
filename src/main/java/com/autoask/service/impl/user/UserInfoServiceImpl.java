package com.autoask.service.impl.user;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.user.UserInfoDao;
import com.autoask.entity.common.Address;
import com.autoask.entity.mongo.user.CarModel;
import com.autoask.entity.mongo.user.UserInfo;
import com.autoask.entity.mysql.User;
import com.autoask.mapper.UserMapper;
import com.autoask.service.user.UserInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/22.
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserInfo getByUserId(String userId) throws ApiException {
        UserInfo userInfo = userInfoDao.findOne(Query.query(Criteria.where("userId").is(userId)));
        if (null == userInfo) {
            //如果用户的资料为空 需要同步用户的资料
            Example example = new Example(User.class);
            example.createCriteria().andEqualTo("userId", userId);

            List<User> userList = userMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(userList)) {
                throw new ApiException("用户数据非法");
            }

            User user = userList.get(0);

            userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setPhone(user.getPhone());

            userInfoDao.save(userInfo);
        }
        return userInfo;
    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo) throws ApiException {
        String userId = LoginUtil.getSessionInfo().getLoginId();
        UserInfo preUserInfo = userInfoDao.findOne(Query.query(Criteria.where("userId").is(userId)));
        if (null == preUserInfo) {
            userInfo.setUserId(userId);
            userInfoDao.save(userInfo);
        } else {
            userInfoDao.updateSelective(preUserInfo.getId(), userInfo);
        }
        String nickName = userInfo.getNickname();
        if (null == nickName) {
            nickName = "";
        }
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("userId", userId);
        User updateEntity = new User();
        updateEntity.setName(nickName);
        userMapper.updateByExampleSelective(updateEntity, example);
        return userInfo;
    }

    @Override
    public void addUserAddress(Address address, Integer defaultFlag) throws ApiException {
        //check param
        Address.checkFullInfo(address);
        if (null == defaultFlag) {
            defaultFlag = 0;
        }
        String userId = LoginUtil.getSessionInfo().getLoginId();
        UserInfo userInfo = userInfoDao.findOne(Query.query(Criteria.where("userId").is(userId)));

        if (null == userInfo) {
            userInfo.setId(null);
            userInfo.setUserId(userId);

            ArrayList<Address> addressList = new ArrayList<>();
            addressList.add(address);

            userInfo.setAddressList(addressList);

            userInfoDao.save(userInfo);
        } else {
            UserInfo updateInfo = new UserInfo();
            List<Address> preAddressList = userInfo.getAddressList();
            if (null == preAddressList) {
                preAddressList = new ArrayList<>(1);
            }
            if (defaultFlag.equals(0)) {
                //是否默认 放在第一个
                preAddressList.add(0, address);
            } else {
                preAddressList.add(address);
            }
            updateInfo.setAddressList(preAddressList);

            userInfoDao.updateSelective(userInfo.getId(), updateInfo);
        }
    }

    @Override
    public void deleteUserAddress(Integer index) throws ApiException {

        String userId = LoginUtil.getSessionInfo().getLoginId();
        UserInfo userInfo = userInfoDao.findOne(Query.query(Criteria.where("userId").is(userId)));

        if (null == userInfo || CollectionUtils.isEmpty(userInfo.getAddressList())) {
            throw new ApiException("地址不存在");
        }
        if (index < 0 || index > userInfo.getAddressList().size() - 1) {
            throw new ApiException("地址不存在");
        }
        List<Address> addressList = userInfo.getAddressList();
        addressList.remove(index.intValue());
        userInfo.setAddressList(addressList);

        userInfoDao.updateSelective(userInfo.getId(), userInfo);
    }

    @Override
    public void updateUserAddress(List<Address> addressList) throws ApiException {
        if (CollectionUtils.isEmpty(addressList)) {
            throw new ApiException("地址列表不能为空");
        }
        String userId = LoginUtil.getSessionInfo().getLoginId();
        UserInfo userInfo = userInfoDao.findOne(Query.query(Criteria.where("userId").is(userId)));

        if (null == userInfo) {
            userInfo.setId(null);
            userInfo.setUserId(userId);
            userInfo.setAddressList(addressList);

            userInfoDao.save(userInfo);
        } else {
            UserInfo updateInfo = new UserInfo();

            updateInfo.setAddressList(addressList);

            userInfoDao.updateSelective(userInfo.getId(), updateInfo);
        }
    }

    @Override
    public void addUserCarModel(CarModel carModel) throws ApiException {
        if (CarModel.isIllegal(carModel)) {
            throw new ApiException("参数非法");
        }

        String userId = LoginUtil.getSessionInfo().getLoginId();
        UserInfo userInfo = userInfoDao.findOne(Query.query(Criteria.where("userId").is(userId)));

        if (null == userInfo) {
            userInfo = new UserInfo();
            userInfo.setUserId(userId);
            ArrayList<CarModel> carModels = new ArrayList<>(1);
            carModels.add(carModel);
            userInfo.setCarModelList(carModels);

            userInfoDao.save(userInfo);
        } else {
            List<CarModel> carModelList = userInfo.getCarModelList();
            if (CollectionUtils.isEmpty(carModelList)) {
                carModelList = new ArrayList<>(1);
            }
            carModelList.add(carModel);
            userInfo.setCarModelList(carModelList);
            userInfoDao.updateSelective(userInfo.getId(), userInfo);
        }
    }

    @Override
    public void updateUserCarModel(List<CarModel> carModels) throws ApiException {
        if (CollectionUtils.isEmpty(carModels)) {
            throw new ApiException("参数非法");
        }
        for (CarModel carModel : carModels) {
            if (CarModel.isIllegal(carModel)) {
                throw new ApiException("参数非法");
            }
        }

        String userId = LoginUtil.getSessionInfo().getLoginId();

        UserInfo userInfo = userInfoDao.findOne(Query.query(Criteria.where("userId").is(userId)));

        if (null == userInfo) {
            userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setCarModelList(carModels);

            userInfoDao.save(userInfo);
        } else {
            userInfo.setCarModelList(carModels);
            userInfoDao.updateSelective(userInfo.getId(), userInfo);
        }
    }

    @Override
    public void deleteUserCarModel(Integer index) throws ApiException {
        String userId = LoginUtil.getSessionInfo().getLoginId();
        UserInfo userInfo = userInfoDao.findOne(Query.query(Criteria.where("userId").is(userId)));

        if (null == userInfo || CollectionUtils.isEmpty(userInfo.getCarModelList())) {
            throw new ApiException("车型不存在");
        }
        if (index < 0 || index > userInfo.getCarModelList().size() - 1) {
            throw new ApiException("车型不存在");
        }
        userInfo.getCarModelList().remove(index.intValue());

        userInfoDao.updateSelective(userInfo.getId(), userInfo);
    }

    @Override
    public void updatePassword(String password) throws ApiException {
        String userId = LoginUtil.getSessionInfo().getLoginId();
        if (StringUtils.isEmpty(password)) {
            throw new ApiException("密码不能为空");
        }
        if (password.length() < Constants.PASSWORD_MIN_LENGTH) {
            throw new ApiException("密码长度不够");
        }

        Example queryExp = new Example(User.class);
        queryExp.createCriteria().andEqualTo("userId", userId);
        User updateEntity = new User();
        updateEntity.setPassword(password);
        int updateNum = userMapper.updateByExampleSelective(updateEntity, queryExp);
        if (updateNum == 0) {
            throw new ApiException("更新失败");
        }
    }
}
