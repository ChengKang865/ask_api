package com.autoask.dao.merchant;

import com.autoask.entity.mongo.merchant.ServiceProvider;

/**
 * Created by weid on 16-8-8.
 */
public interface ServiceProviderDao extends BaseMerchantDao<ServiceProvider> {

    ServiceProvider getServiceProviderByPhone(String phone);
}
