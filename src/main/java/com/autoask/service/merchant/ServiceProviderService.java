package com.autoask.service.merchant;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.common.Address;
import com.autoask.entity.mongo.merchant.ServiceProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by weid on 16-8-8.
 */
public interface ServiceProviderService {

    ServiceProvider findById(String serviceProviderId) throws ApiException;

    List<ServiceProvider> findByIdCollection(Collection<String> idCollection);

    List<ServiceProvider> findByPartnerId(String partnerId);

    ListSlice getServiceProviderList(String phone, String name, Integer start, Integer length) throws ApiException;

    ServiceProvider getServiceProviderByPhone(String phone);

    void create(ServiceProvider serviceProvider) throws ApiException;

    void updateSelective(String serviceProviderId, ServiceProvider serviceProvider) throws ApiException;

    void deleteById(String serviceProviderId) throws ApiException;

    List<ServiceProvider> getAreaServiceProviderList(String province, String city, String street, Double latitude, Double longitude) throws ApiException;

    ServiceProvider getUserLastServiceProvider(String userId) throws ApiException;
}
