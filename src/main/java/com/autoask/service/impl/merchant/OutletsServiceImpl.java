package com.autoask.service.impl.merchant;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.*;
import com.autoask.dao.BaseDao;
import com.autoask.dao.merchant.OutletsDao;
import com.autoask.entity.common.Address;
import com.autoask.entity.mongo.merchant.Mechanic;
import com.autoask.entity.mongo.merchant.Outlets;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.entity.mysql.MerchantAssets;
import com.autoask.service.merchant.MerchantAssetsService;
import com.autoask.service.merchant.MerchantService;
import com.autoask.service.merchant.OutletsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by weid on 16-8-14.
 */
@Service("outletsService")
public class OutletsServiceImpl implements OutletsService {
    private static Logger LOG = LoggerFactory.getLogger(OutletsServiceImpl.class);

    @Autowired
    private OutletsDao outletsDao;

    @Autowired
    private MerchantAssetsService merchantAssetsService;

    @Autowired
    private MerchantService merchantService;

    @Override
    public Outlets findById(String id) {
        return outletsDao.findOne(Query.query(Criteria.where("id").is(id).and("deleteFlag").is(false)));
    }

    @Override
    public ListSlice<Outlets> getOutletsList(String phone, String name, Integer start, Integer length) throws ApiException {
        LOG.info("get outlets list.");

        Criteria criteria = new Criteria();

        if (StringUtils.isNotEmpty(phone)) {
            String phoneReg = ".*" + phone + ".*";
            criteria.and("phone").regex(phoneReg);
        }
        if (StringUtils.isNotEmpty(name)) {
            String nameReg = ".*" + name + ".*";
            criteria.and("name").regex(nameReg);
        }

        criteria.and("deleteFlag").is(false);

        Query query = Query.query(criteria);
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        if (start != null) {
            query.skip(start);
        }
        if (length != null) {
            query.limit(length);
        }
        List<Outlets> outletsList = outletsDao.find(query);
        long totalNum = outletsDao.count(Query.query(criteria));
        return new ListSlice<>(outletsList, totalNum);
    }


    private void checkBaseValidation(Outlets outlets) throws ApiException {
        Address address = outlets.getAddress();
        if (null == address) {
            throw new ApiException("地址为空");
        }
        String province = address.getProvince();
        String city = address.getCity();
        String region = address.getRegion();
        String street = address.getStreet();
        String detail = address.getDetail();
        if (StringUtils.isEmpty(province) || StringUtils.isEmpty(city)
                || StringUtils.isEmpty(region) || StringUtils.isEmpty(street)
                || StringUtils.isEmpty(detail)) {
            throw new ApiException("Address参数不完整");
        }
        merchantService.checkMerchantPhone(Constants.MerchantType.OUTLETS, outlets.getId(), outlets.getPhone());
    }


    @Override
    public void create(Outlets outlets) throws ApiException {
        LOG.info("add outlets data.");

        if (null == outlets) {
            throw new ApiException("分销点为空");
        }

        String loginType = LoginUtil.getSessionInfo().getLoginType();
        String loginId = LoginUtil.getSessionInfo().getLoginId();
        if (StringUtils.equals(loginType, Constants.LoginType.STAFF)) {

        } else {
            throw new ApiException("没有权限");
        }
        checkBaseValidation(outlets);
        outlets.setAddress(Address.cleanAddress(outlets.getAddress()));
        outlets.setLoginType(Constants.LoginType.OUTLETS);
        outlets.setCreateTime(DateUtil.getDate());
        outlets.setCreatorId(LoginUtil.getSessionInfo().getLoginId());
        outlets.setCreatorType(LoginUtil.getSessionInfo().getLoginType());
        outlets.setDeleteFlag(false);
        outletsDao.save(outlets);
        merchantAssetsService.addMerchantAssets(Constants.MerchantType.OUTLETS, outlets.getId());
    }

    @Override
    public void updateSelective(String outletId, Outlets outlets) throws ApiException {
        outlets.setAddress(Address.cleanAddress(outlets.getAddress()));
        outlets.setModifyTime(DateUtil.getDate());
        outlets.setModifyId(LoginUtil.getSessionInfo().getLoginId());
        outlets.setModifyType(LoginUtil.getSessionInfo().getLoginType());
        outletsDao.updateSelective(outletId, outlets);
        MerchantAssets partnerAssets = merchantAssetsService.getMerchantAssets(Constants.MerchantType.OUTLETS, outlets.getId());
        if (null == partnerAssets) {
            merchantAssetsService.addMerchantAssets(Constants.MerchantType.OUTLETS, outlets.getId());
        }
    }

    @Override
    public void deleteById(String id) throws ApiException {
        Outlets updateEntity = new Outlets();
        updateEntity.setDeleteFlag(true);
        updateEntity.setDeleteTime(DateUtil.getDate());
        updateEntity.setDeleteType(LoginUtil.getLoginType());
        updateEntity.setDeleteId(LoginUtil.getLoginId());
        outletsDao.updateSelective(id, updateEntity);
    }
}
