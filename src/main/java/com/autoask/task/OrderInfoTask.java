package com.autoask.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.BeanUtil;
import com.autoask.service.order.OrderService;
/**
 * 每隔3个小时扫描未支付的的订单并且回滚信息
 * @author ck
 *
 * @Create 2017年4月17日上午11:04:21
 */
@Component("orderInfoTask")
public class OrderInfoTask extends QuartzJobBean{
	
	 private static final Logger LOG = LoggerFactory.getLogger(OrderInfoTask.class);

	    @Override
	    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
	        LOG.info("Begin execute executeInternal");
	        OrderService orderService = BeanUtil.getBean(OrderService.class);
	        try {
				orderService.updateRollBACKOrderInfo();
			} catch (ApiException e) {
				e.printStackTrace();
			}
	    }
}
