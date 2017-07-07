package com.autoask.dao.impl.merchant;

import com.autoask.dao.merchant.ServiceProviderDao;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by weid on 16-8-8.
 */
@Repository("serviceProviderDao")
public class ServiceProviderDaoImpl extends BaseMerchantDaoImpl<ServiceProvider> implements ServiceProviderDao {

    @Override
    public ServiceProvider getServiceProviderByPhone(String phone) {
        return this.findOne(Query.query(Criteria.where("phone").is(phone).and("deleteFlag").is(false)));
    }
}
