package com.autoask.dao.impl.merchant;

import com.autoask.dao.merchant.FactoryDao;
import com.autoask.entity.mongo.merchant.Factory;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 * @create 2016-08-07 15:47
 */
@Repository("factoryDao")
public class FactoryDaoImpl extends BaseMerchantDaoImpl<Factory> implements FactoryDao {
}
