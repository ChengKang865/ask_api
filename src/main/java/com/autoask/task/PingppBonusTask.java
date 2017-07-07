package com.autoask.task;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.BeanUtil;
import com.autoask.common.util.CodeGenerator;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.entity.mongo.merchant.BaseMerchant;
import com.autoask.entity.mysql.MerchantAssets;
import com.autoask.entity.mysql.MerchantBonusRecord;
import com.autoask.mapper.MerchantAssetsMapper;
import com.autoask.mapper.MerchantBonusRecordMapper;
import com.autoask.pay.pingpp.PingppTransferUtil;
import com.autoask.pay.pingpp.config.PingppConfig;
import com.autoask.service.log.PayLogService;
import com.autoask.service.merchant.MerchantService;
import com.pingplusplus.model.Transfer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 通过Ping++向用户转账
 *
 * @author hyy
 * @create 2016-11-06 18:30
 */
@Component("pingppBonusTask")
public class PingppBonusTask extends QuartzJobBean {

    private static final Logger LOG = LoggerFactory.getLogger(PingppBonusTask.class);

    /**
     * 支持的列表：Mechanic和Outlets
     */
    private List<String> supportMerchantTypes = Arrays.asList(Constants.MerchantType.MECHANIC, Constants.MerchantType.OUTLETS);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("Begin execute pingppBonusTask, merchantTypes:{}", supportMerchantTypes);

        MerchantAssetsMapper merchantAssetsMapper = BeanUtil.getBean(MerchantAssetsMapper.class);
        MerchantService merchantService = BeanUtil.getBean(MerchantService.class);
        MerchantBonusRecordMapper merchantBonusRecordMapper = BeanUtil.getBean(MerchantBonusRecordMapper.class);
        PayLogService payLogService = BeanUtil.getBean(PayLogService.class);

        String orderNo = DateUtil.getDataToSecond(); //仅记录
        int count = 0;
        for (String merchantType : supportMerchantTypes) {
            List<MerchantAssets> merchantAssets = Collections.emptyList();
            Long nowId = 0L;
            int stepSize = 100; //每页查询100条
            do {
                merchantAssets = merchantAssetsMapper.selectByParam(merchantType, BigDecimal.ONE, nowId, stepSize);
                for (MerchantAssets item : merchantAssets) {
                    count++;
                    try {
                        BaseMerchant baseMerchant = merchantService.getMerchantInfo(item.getMerchantType(), item.getMerchantId());
                        if (null != baseMerchant && StringUtils.isNotBlank(baseMerchant.getOpenId())) {
                            //针对每一个商户进行转账
                            Transfer transfer = PingppTransferUtil.transfer(orderNo + count, PingppConfig.PayChannel.WX_PUB, item.getBalance(),
                                    baseMerchant.getOpenId(), "AutoAsk向商户转账", Collections.EMPTY_MAP);
                            if (transfer == null) {
                                throw new ApiException("向Ping++提交转账失败,商户信息" + item.getMerchantType() + "商户" + item.getMerchantId());
                            }
                            payLogService.savePayLog(Constants.PayLogType.PINGPP_BONUS_APPLY, transfer);//记录申请日志
                            if (StringUtils.isNotBlank(transfer.getFailureMsg())) {
                                throw new ApiException("提交付款申请失败，错误信息:" + transfer.getFailureMsg());
                            }
                            //更新商户的资产
                            merchantAssetsMapper.updateBalance(item.getMerchantType(), item.getMerchantId(),
                                    item.getBalance().multiply(BigDecimal.valueOf(-1)));

                            //记录日志
                            MerchantBonusRecord record = new MerchantBonusRecord();
                            record.setStatus(Constants.MerchantBonusStatus.DOING);
                            record.setChangeAmount(item.getBalance());
                            record.setCreateTime(new Date());
                            record.setMerchantId(item.getMerchantId());
                            record.setMerchantType(item.getMerchantType());
                            record.setRecordId(CodeGenerator.uuid());
                            record.setNowAmount(BigDecimal.ZERO);
                            record.setPreAmount(item.getBalance());
                            record.setPingppTransferId(transfer.getId());
                            merchantBonusRecordMapper.insert(record);
                        }
                    } catch (Exception e) {
                        LOG.error("Pingpp bonus to merchantId:{}, merchantType:{} failure", item.getMerchantId(), item.getMerchantType());
                        LOG.error("Error info:", e);
                    }
                    nowId = item.getId();
                    try {
                        Thread.sleep(200); //Ping++对接口访问有限制
                    } catch (InterruptedException e) {
                        LOG.warn("Sleep failure", e);
                    }
                }
            } while (CollectionUtils.isNotEmpty(merchantAssets)); //不为空继续查询
        }
        LOG.info("Finished execute pingppBonusTask, merchantTypes:{}", supportMerchantTypes);
    }
}
