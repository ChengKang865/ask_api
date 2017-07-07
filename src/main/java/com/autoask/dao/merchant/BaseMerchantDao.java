package com.autoask.dao.merchant;

import com.autoask.dao.BaseDao;
import com.autoask.entity.mongo.merchant.BaseMerchant;

/**
 * Created by weid on 16-8-13.
 */
public interface BaseMerchantDao<T extends BaseMerchant>  extends BaseDao<T, String> {

}
