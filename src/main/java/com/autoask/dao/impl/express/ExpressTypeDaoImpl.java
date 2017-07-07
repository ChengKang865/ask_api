package com.autoask.dao.impl.express;

import org.springframework.stereotype.Repository;

import com.autoask.dao.express.ExpressTypeDao;
import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.entity.mongo.express.ExpressType;

/**
 * 快递类型
 * @author ck
 *
 * @Create 2017年4月1日下午3:03:31
 */
@Repository("expressTypeDao")
public class ExpressTypeDaoImpl extends BaseDaoImpl<ExpressType, String> implements ExpressTypeDao {
	
}
