package com.autoask.task;

import com.autoask.common.util.BeanUtil;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.mapper.MerchantShareApplyMapper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hyy on 2016/12/25.
 */
@Component("extraApplyTask")
public class ExtraApplyTask extends QuartzJobBean {

    private static final Logger LOG = LoggerFactory.getLogger(ExtraApplyTask.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("Begin execute extraApplyTask");

        MerchantShareApplyMapper merchantShareApplyMapper = BeanUtil.getBean(MerchantShareApplyMapper.class);
        //2.5小时之前 - 1小时之前
        Date now = DateUtil.getDate();
        Long startTime = DateUtil.addTime(now, Calendar.MINUTE, -150);
        Long endTime = DateUtil.addTime(now, Calendar.MINUTE, -60);
        merchantShareApplyMapper.updateShareDoingBack(Constants.MerchantShareApplyStatus.DOING, Constants.MerchantShareApplyStatus.APPLYING, new Date(startTime), new Date(endTime));
    }
}
