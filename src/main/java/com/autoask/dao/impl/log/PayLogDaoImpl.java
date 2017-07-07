package com.autoask.dao.impl.log;

import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.log.PayLogDao;
import com.autoask.entity.mongo.log.PayLog;
import org.springframework.stereotype.Service;

/**
 * @author hyy
 * @create 2016-11-05 14:38
 */
@Service
public class PayLogDaoImpl extends BaseDaoImpl<PayLog, String> implements PayLogDao {
}
