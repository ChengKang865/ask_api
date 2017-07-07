package com.autoask.dao.impl.merchant;

import com.autoask.dao.merchant.OutletsDao;
import com.autoask.entity.mongo.merchant.Outlets;
import org.springframework.stereotype.Repository;

/**
 * Created by hp on 16-8-15.
 */
@Repository("outletsDao")
public class OutletsDaoImpl extends BaseMerchantDaoImpl<Outlets> implements OutletsDao {

}
