package com.autoask.service.impl.merchant;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.BaseDao;
import com.autoask.dao.merchant.MechanicDao;
import com.autoask.dao.merchant.ServiceProviderDao;
import com.autoask.entity.mongo.merchant.Mechanic;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.entity.mysql.MerchantAssets;
import com.autoask.service.merchant.MechanicService;
import com.autoask.service.merchant.MerchantAssetsService;
import com.autoask.service.merchant.MerchantService;
import com.autoask.service.merchant.ServiceProviderService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by weid on 16-8-14.
 */
@Service("mechanicService")
public class MechanicServiceImpl implements MechanicService {

    private static Logger LOG = LoggerFactory.getLogger(MechanicServiceImpl.class);

    @Autowired
    private MechanicDao mechanicDao;

    @Autowired
    private MerchantAssetsService merchantAssetsService;

    @Autowired
    private ServiceProviderDao serviceProviderDao;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private MerchantService merchantService;

    @Override
    public void create(Mechanic mechanic) throws ApiException {
        String loginType = LoginUtil.getSessionInfo().getLoginType();
        String loginId = LoginUtil.getSessionInfo().getLoginId();
        if (StringUtils.equals(loginType, Constants.LoginType.SERVICE_PROVIDER)) {
            mechanic.setServiceProviderId(loginId);
        } else if (StringUtils.equals(loginType, Constants.LoginType.STAFF)) {
        } else {
            throw new ApiException("没有权限");
        }

        checkBaseValidation(mechanic);

        mechanic.setLoginType(Constants.LoginType.MECHANIC);
        mechanic.setCreateTime(DateUtil.getDate());
        mechanic.setCreatorId(LoginUtil.getSessionInfo().getLoginId());
        mechanic.setCreatorType(LoginUtil.getSessionInfo().getLoginType());
        mechanic.setDeleteFlag(false);
        mechanicDao.save(mechanic);
        merchantAssetsService.addMerchantAssets(Constants.MerchantType.MECHANIC, mechanic.getId());
    }

    @Override
    public Mechanic findById(String id) {
        return mechanicDao.findOne(Query.query(Criteria.where("id").is(id).and("deleteFlag").is(false)));
    }

    @Override
    public void updateSelective(Mechanic mechanic) throws ApiException {
        checkBaseValidation(mechanic);

        mechanic.setModifyTime(DateUtil.getDate());
        mechanic.setModifyType(LoginUtil.getLoginType());
        mechanic.setModifyId(LoginUtil.getLoginId());
        mechanicDao.updateSelective(mechanic.getId(), mechanic);

        MerchantAssets mechanicAssets = merchantAssetsService.getMerchantAssets(Constants.MerchantType.SERVICE_PROVIDER, mechanic.getId());
        if (null == mechanicAssets) {
            merchantAssetsService.addMerchantAssets(Constants.MerchantType.SERVICE_PROVIDER, mechanic.getId());
        }
    }

    @Override
    public void deleteById(String id) throws ApiException {
        Mechanic updateEntity = new Mechanic();
        updateEntity.setDeleteFlag(true);
        updateEntity.setDeleteTime(DateUtil.getDate());
        updateEntity.setDeleteType(LoginUtil.getLoginType());
        updateEntity.setDeleteId(LoginUtil.getLoginId());
        mechanicDao.updateSelective(id, updateEntity);
    }

    @Override
    public List<Mechanic> getMechanicByServiceProviderId(String serviceProviderId) {
        return mechanicDao.find(Query.query(Criteria.where("serviceProviderId").is(serviceProviderId).and("deleteFlag").is(false)));
    }

    @Override
    public ListSlice<Mechanic> getMechanicList(String serviceProviderId, String phone, String name, Integer start, Integer length) throws ApiException {
        LOG.info("get mechanic list.");

        Criteria criteria = new Criteria();

        String loginType = LoginUtil.getSessionInfo().getLoginType();
        String loginId = LoginUtil.getSessionInfo().getLoginId();

        List<String> serviceProviderIdList = null;
        if (StringUtils.isNotEmpty(serviceProviderId)) {
	        if (StringUtils.equals(loginType, Constants.LoginType.STAFF)) {
	            serviceProviderIdList = Arrays.asList(serviceProviderId);
	        } else if (StringUtils.equals(loginType, Constants.LoginType.SERVICE_PROVIDER)) {
	            serviceProviderIdList = Arrays.asList(loginId);
	        } else {
	            throw new ApiException("没有权限");
	        }
        }
        if (StringUtils.isNotEmpty(serviceProviderId)) {
            criteria.and("serviceProviderId").is(serviceProviderId);
        } else if (CollectionUtils.isNotEmpty(serviceProviderIdList)) {
            criteria.and("serviceProviderId").in(serviceProviderIdList);
        }
        if (StringUtils.isNotEmpty(name)) {
            String nameReg = ".*" + name + ".*";
            criteria.and("name").regex(nameReg);
        }
        if (StringUtils.isNotEmpty(phone)) {
            String phoneReg = ".*" + phone + ".*";
            criteria.and("phone").regex(phoneReg);
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
        List<Mechanic> mechanicList = mechanicDao.find(query);
        long count = mechanicDao.count(Query.query(criteria));
        return new ListSlice<>(mechanicList, count);
    }


    private void checkBaseValidation(Mechanic mechanic) throws ApiException {
        if (null == mechanic) {
            throw new ApiException("修理工对象为空.");
        }
        if (StringUtils.isEmpty(mechanic.getPhone())) {
            throw new ApiException("手机号不能为空");
        }
        if (StringUtils.isEmpty(mechanic.getName())) {
            throw new ApiException("名称不能为空");
        }
        if (StringUtils.isEmpty(mechanic.getServiceProviderId())) {
            throw new ApiException("修理工所属修理厂id为空");
        }
        ServiceProvider serviceProvider = serviceProviderService.findById(mechanic.getServiceProviderId());
        if (null == serviceProvider) {
            throw new ApiException("修理工所属的服务商不存在.");
        }
        merchantService.checkMerchantPhone(Constants.MerchantType.MECHANIC, mechanic.getId(), mechanic.getPhone());
    }


    @Override
    public List<Mechanic> getServiceProviderMechanicList(String serviceProviderId) throws ApiException {
        return mechanicDao.find(Query.query(Criteria.where("serviceProviderId").is(serviceProviderId).and("deleteFlag").is(false)));
    }
}
