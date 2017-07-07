package com.autoask.service.impl.log;

import com.autoask.common.util.Constants;
import com.autoask.dao.log.PayLogDao;
import com.autoask.entity.mongo.log.PayLog;
import com.autoask.service.log.PayLogService;
import com.ibm.icu.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author hyy
 * @create 2016-11-05 14:42
 */
@Service
public class PayLogServiceImpl implements PayLogService {

    @Autowired
    private PayLogDao payLogDao;

    @Override
    public void savePayLog(String type, Object content) {
        PayLog payLog = new PayLog();
        payLog.setType(type);
        payLog.setContent(content);
        payLog.setCreateTime(new SimpleDateFormat(Constants.DateFormat.YYYY_MM_DD_HH_MM_SS).format(new Date()));
        payLogDao.save(payLog);
    }
}
