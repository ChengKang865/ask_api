package com.autoask.service.impl.merchant;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.*;
import com.autoask.controller.merchant.MechanicController;
import com.autoask.dao.BaseDao;
import com.autoask.dao.merchant.FactoryDao;
import com.autoask.entity.common.Address;
import com.autoask.entity.mongo.merchant.Factory;
import com.autoask.service.common.LandMarkService;
import com.autoask.service.merchant.FactoryService;
import com.autoask.service.merchant.MerchantAssetsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by weid on 16-8-14.
 */
@Service("factoryService")
public class FactoryServiceImpl implements FactoryService {

    public static Logger LOG = LoggerFactory.getLogger(MechanicController.class);

    @Autowired
    private FactoryDao factoryDao;

    @Autowired
    private LandMarkService landMarkService;

    @Autowired
    private MerchantAssetsService merchantAssetsService;

    @Override
    public Factory findById(String id) throws ApiException {
        String loginType = LoginUtil.getLoginType();
        if (StringUtils.equals(loginType, Constants.LoginType.FACTORY)) {
            id = LoginUtil.getLoginId();
        }
        if (StringUtils.isEmpty(id)) {
            throw new ApiException("id为空");
        }
        return factoryDao.findOne(Query.query(Criteria.where("id").is(id).and("deleteFlag").is(false)));
    }

    @Override
    public Factory getFactoryByPhone(String phone) {
        return factoryDao.findOne(Query.query(Criteria.where("phone").is(phone).and("deleteFlag").is(false)));
    }

    @Override
    public ListSlice<Factory> getFactoryList(String phone, String name, Integer start, Integer length) {
        LOG.info("get factory list.");

        Criteria criteria = new Criteria();

        criteria.and("deleteFlag").is(false);

        if (StringUtils.isNotEmpty(name)) {
            String nameReg = ".*" + name + ".*";
            criteria.and("name").regex(nameReg);
        }
        if (StringUtils.isNotEmpty(phone)) {
            String phoneReg = ".*" + phone + ".*";
            criteria.and("phone").regex(phoneReg);
        }

        Query query = Query.query(criteria);
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        if (start != null) {
            query.skip(start);
        }
        if (length != null) {
            query.limit(length);
        }
        List<Factory> mechanicList = factoryDao.find(query);
        long count = factoryDao.count(Query.query(criteria));
        return new ListSlice<>(mechanicList, count);
    }

    @Override
    public void create(Factory factory) throws ApiException {
        LOG.info("add factory data.");
        if (null == factory) {
            throw new ApiException("合作工厂为空");
        }
        String loginType = LoginUtil.getSessionInfo().getLoginType();
        if (!StringUtils.equals(loginType, Constants.LoginType.STAFF)) {
            throw new ApiException("没有权限");
        }
        factory.setAddress(Address.cleanAddress(factory.getAddress()));
        Date now = DateUtil.getDate();
        factory.setCreateTime(now);
        factory.setCreatorId(LoginUtil.getSessionInfo().getLoginId());
        factory.setCreatorType(LoginUtil.getSessionInfo().getLoginType());
        factory.setDeleteFlag(false);
        factory.setLoginType(Constants.LoginType.FACTORY);
        factoryDao.save(factory);
        merchantAssetsService.addMerchantAssets(Constants.MerchantType.FACTORY, factory.getId());
    }

    @Override
    public void deleteById(String id) {
        Update update = new Update();
        update.set("deleteFlag", true);
        factoryDao.update(id, update);
    }

    @Override
    public void updateSelective(String factoryId, Factory factory) throws ApiException {
        factory.setAddress(Address.cleanAddress(factory.getAddress()));

        factoryDao.updateSelective(factoryId, factory);
    }
}
