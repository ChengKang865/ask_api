package com.autoask.dao.impl.user;

import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.user.UserInfoDao;
import com.autoask.entity.mongo.user.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/10/22.
 */
@Repository("userInfoDao")
public class UserInfoDaoImpl extends BaseDaoImpl<UserInfo, String> implements UserInfoDao {
}
