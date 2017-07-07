package com.autoask.dao.impl.merchant;

import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.merchant.BaseMerchantDao;
import com.autoask.entity.mongo.merchant.BaseMerchant;

/**
 * Created by weid on 16-8-13.
 */
public class BaseMerchantDaoImpl<T extends BaseMerchant> extends BaseDaoImpl<T, String> implements BaseMerchantDao<T> {
}
