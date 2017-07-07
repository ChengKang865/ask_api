package com.autoask.dao.impl.merchant;

import com.autoask.dao.merchant.MechanicDao;
import com.autoask.entity.mongo.merchant.Mechanic;
import org.springframework.stereotype.Repository;

/**
 * Created by weid on 16-8-14.
 */
@Repository("mechanicDao")
public class MechanicDaoImpl extends BaseMerchantDaoImpl<Mechanic> implements MechanicDao {
}
