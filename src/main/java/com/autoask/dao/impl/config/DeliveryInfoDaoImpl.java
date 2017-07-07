package com.autoask.dao.impl.config;

import com.autoask.dao.config.DeliveryInfoDao;
import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.entity.mongo.config.DeliveryInfo;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 * @create 16/10/23 20:54
 */
@Repository
public class DeliveryInfoDaoImpl extends BaseDaoImpl<DeliveryInfo, String> implements DeliveryInfoDao {
}
