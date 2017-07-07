package com.autoask.service.impl.merchant;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.*;
import com.autoask.dao.merchant.ServiceProviderDao;
import com.autoask.entity.common.Address;
import com.autoask.entity.mongo.merchant.BaseMerchant;
import com.autoask.entity.mongo.merchant.Mechanic;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.mapper.OrderInfoMapper;
import com.autoask.service.common.LandMarkService;
import com.autoask.service.merchant.*;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by weid on 16-8-8.
 */
@Service()
public class ServiceProviderServiceImpl implements ServiceProviderService {

    private static Logger LOG = LoggerFactory.getLogger(ServiceProviderServiceImpl.class);

    @Autowired
    private ServiceProviderDao serviceProviderDao;

    @Autowired
    private LandMarkService landMarkService;

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private MerchantAssetsService merchantAssetsService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private MerchantService merchantService;

    @Override
    public ServiceProvider findById(String serviceProviderId) throws ApiException {
        ServiceProvider serviceProvider = serviceProviderDao.findOne(Query.query(Criteria.where("id").is(serviceProviderId).and("deleteFlag").is(false)));
        if (null == serviceProvider) {
            throw new ApiException("服务点不存在");
        }
        return serviceProvider;
    }

    @Override
    public List<ServiceProvider> findByIdCollection(Collection<String> idCollection) {
        return serviceProviderDao.find(Query.query(Criteria.where("id").in(idCollection).and("deleteFlag").is(false)));
    }

    @Override
    public List<ServiceProvider> findByPartnerId(String partnerId) {
        return serviceProviderDao.find(Query.query(Criteria.where("partnerId").is(partnerId).and("deleteFlag").is(false)));
    }

    @Override
    public ListSlice getServiceProviderList(String phone, String name, Integer start, Integer length) throws ApiException {
        LOG.info("get serviceProvider list");

        Criteria criteria = new Criteria();
        criteria.and("deleteFlag").is(false);

        String loginType = LoginUtil.getSessionInfo().getLoginType();
        String loginId = LoginUtil.getSessionInfo().getLoginId();
        if (StringUtils.equals(loginType, Constants.MerchantType.SERVICE_PROVIDER)) {
            criteria.and("id").is(loginId);
        }

        if (StringUtils.isNotEmpty(phone)) {
            String phoneReg = ".*" + phone + ".*";
            criteria.and("phone").regex(phoneReg);
        }
        if (StringUtils.isNotEmpty(name)) {
            String nameReg = ".*" + name + ".*";
            criteria.and("name").regex(nameReg);
        }

        Query query = Query.query(criteria);
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        if (start != null) {
            query.skip(start);
        }
        if (length != null) {
            query.limit(length);
        }
        List<ServiceProvider> serviceProviders = serviceProviderDao.find(query);
        long count = serviceProviderDao.count(Query.query(criteria));

        return new ListSlice<>(serviceProviders, count);
    }

    @Override
    public ServiceProvider getServiceProviderByPhone(String phone) {
        return serviceProviderDao.getServiceProviderByPhone(phone);
    }

    @Override
    public void create(ServiceProvider serviceProvider) throws ApiException {
        LOG.info("add serviceProvider");

        if (null == serviceProvider) {
            throw new ApiException("待添加服务点为空");
        }
        String loginId = LoginUtil.getSessionInfo().getLoginId();
        String loginType = LoginUtil.getSessionInfo().getLoginType();
        if (StringUtils.equals(loginType, Constants.LoginType.STAFF)) {

        } else {
            throw new ApiException("没有权限");
        }
        serviceProvider.setAddress(Address.cleanAddress(serviceProvider.getAddress()));
        serviceProvider.setLandmark(landMarkService.getLandMark(serviceProvider.getAddress()));
        serviceProvider.setLoginType(Constants.LoginType.SERVICE_PROVIDER);
        ServiceProvider preServiceProvider = serviceProviderDao.getServiceProviderByPhone(serviceProvider.getPhone());
        if (null != preServiceProvider) {
            throw new ApiException("手机号码冲突");
        }

        checkBaseValidation(serviceProvider);

        serviceProvider.setDeleteFlag(false);
        serviceProvider.setCreateTime(DateUtil.getDate());
        serviceProvider.setCreatorType(LoginUtil.getLoginType());
        serviceProvider.setCreatorId(LoginUtil.getLoginId());

        serviceProviderDao.save(serviceProvider);
        merchantAssetsService.addMerchantAssets(Constants.MerchantType.SERVICE_PROVIDER, serviceProvider.getId());
    }

    @Override
    public void updateSelective(String serviceProviderId, ServiceProvider serviceProvider) throws ApiException {
        String loginType = LoginUtil.getLoginType();
        if (StringUtils.equals(loginType, Constants.LoginType.SERVICE_PROVIDER)) {
            ServiceProvider preServiceProvider = serviceProviderDao.findById(LoginUtil.getLoginId());
            if (null == preServiceProvider) {
                throw new ApiException("没有权限");
            }
            serviceProvider.setId(LoginUtil.getLoginId());
        } else if (!StringUtils.equals(Constants.LoginType.STAFF, loginType)) {
            throw new ApiException("没有权限");
        }
        serviceProvider.setAddress(Address.cleanAddress(serviceProvider.getAddress()));

        serviceProvider.setModifyTime(DateUtil.getDate());
        serviceProvider.setModifyType(LoginUtil.getLoginType());
        serviceProvider.setModifyId(LoginUtil.getLoginId());

        serviceProviderDao.updateSelective(serviceProviderId, serviceProvider);
    }


    private void checkBaseValidation(ServiceProvider serviceProvider) throws ApiException {
        // 验证地址
        checkAddressValidation(serviceProvider);
    }

    private void checkAddressValidation(ServiceProvider serviceProvider) throws ApiException {
        Address address = serviceProvider.getAddress();
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
    }

    @Override
    public void deleteById(String serviceProviderId) throws ApiException {
        List<Mechanic> mechanicList = mechanicService.getMechanicByServiceProviderId(serviceProviderId);
        if (CollectionUtils.isNotEmpty(mechanicList)) {
            throw new ApiException("此服务点下存在服务点, 清先删除修理工");
        }

        ServiceProvider updateEntity = new ServiceProvider();
        updateEntity.setDeleteFlag(true);
        updateEntity.setDeleteTime(DateUtil.getDate());
        updateEntity.setDeleteType(LoginUtil.getLoginType());
        updateEntity.setDeleteId(LoginUtil.getLoginId());

        serviceProviderDao.updateSelective(serviceProviderId, updateEntity);
    }


    @Override
    public List<ServiceProvider> getAreaServiceProviderList(String province, String city, String region,
                                                            Double latitude, Double longitude) throws ApiException {

        List<ServiceProvider> serviceProviderList = serviceProviderDao.find(Query.query(Criteria.where("address.province").is(province)
                .and("address.city").is(city).and("address.region").is(region).and("deleteFlag").is(false)));


        if (CollectionUtils.isEmpty(serviceProviderList)) {
            serviceProviderList = serviceProviderDao.find(Query.query(Criteria.where("address.province").is(province)
                    .and("address.city").is(city).and("deleteFlag").is(false)));
        }
        return serviceProviderList;
    }

    @Override
    public ServiceProvider getUserLastServiceProvider(String userId) throws ApiException {

        String serviceProviderId = orderInfoMapper.getUserLastServiceProvider(userId);
        if (null != serviceProviderId) {
            return this.findById(serviceProviderId);
        } else {
            return null;
        }
    }
}
