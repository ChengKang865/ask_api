package com.autoask.service.impl.common;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.entity.mysql.MerchantAssets;
import com.autoask.entity.mysql.MerchantShareApply;
import com.autoask.mapper.MerchantAssetsMapper;
import com.autoask.mapper.MerchantShareApplyMapper;
import com.autoask.pay.ali.config.AlipayConfig;
import com.autoask.pay.ali.util.AlipayNotify;
import com.autoask.pay.ali.util.AlipaySubmit;
import com.autoask.service.common.AlipayService;
import com.autoask.service.log.PayLogService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by hp on 16-10-30.
 */
@Service("AlipayService")
public class AlipayServiceImpl implements AlipayService {

    private Logger LOG = LoggerFactory.getLogger(AlipayServiceImpl.class);

    @Autowired
    private PayLogService payLogService;

    @Autowired
    private MerchantShareApplyMapper merchantShareApplyMapper;

    @Autowired
    private MerchantAssetsMapper merchantAssetsMapper;

    /**
     * 将前端传递过来的参数格式化成满足支付宝需要的格式.
     *
     * @param
     * @return String
     * @throws ApiException
     */
    @Override
    public String updateMerchantAppliesAndAlipayApply(List<String> applyIds) throws ApiException {

        List<MerchantShareApply> merchantShareApplies = merchantShareApplyMapper.selectByParams(applyIds, Constants.MerchantShareApplyStatus.APPLYING, null);
        if (CollectionUtils.isEmpty(merchantShareApplies)) {
            throw new ApiException("获取到待处理的提款申请信息为空");
        }

        String batchNo = DateUtil.getDataToSecond(); //批号
        StringBuffer detailDataBuffer = new StringBuffer();
        BigDecimal batchFee = BigDecimal.ZERO;
        for (int index = 0; index < merchantShareApplies.size(); index++) {
            MerchantShareApply apply = merchantShareApplies.get(index);
            String serialNo = batchNo.concat(String.valueOf(index));
            detailDataBuffer.append(serialNo).append("^")
                    .append(apply.getAccount()).append("^")
                    .append(apply.getAccountName()).append("^")
                    .append(apply.getAmount()).append("^")
                    .append("AutoASK转账").append("|");
            batchFee = batchFee.add(apply.getAmount());

            MerchantShareApply updateParam = new MerchantShareApply();
            updateParam.setId(apply.getId());
            updateParam.setSerialNo(serialNo);
            updateParam.setBatchNo(batchNo);
            updateParam.setStatus(Constants.MerchantShareApplyStatus.DOING);
            updateParam.setUpdateTime(DateUtil.getDate());
            merchantShareApplyMapper.updateByPrimaryKeySelective(updateParam);  //更新批次号和版本号
        }

        return buildAlipayFormatStr(batchNo, merchantShareApplies.size(), detailDataBuffer, batchFee);
    }

    /**
     * 构造阿里支付的参数字符串
     *
     * @param batchNo
     * @param detailDataBuffer
     * @param batchFee
     * @return
     */
    private String buildAlipayFormatStr(String batchNo, Integer batchNum, StringBuffer detailDataBuffer, BigDecimal batchFee) {
        String detailDataStr = detailDataBuffer.toString();
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("service", AlipayConfig.batch_trans_notify_service);
        resultMap.put("partner", AlipayConfig.partner);
        resultMap.put("_input_charset", AlipayConfig.input_charset);
        resultMap.put("email", AlipayConfig.email);
        resultMap.put("account_name", AlipayConfig.account_name);

        // TODO: notify_url 是支付宝异步回调地址. 如果修改, 此处需要更新.
        resultMap.put("notify_url", AlipayConfig.notify_url);
        resultMap.put("pay_date", DateUtil.getDateShort());
        resultMap.put("batch_no", batchNo.toString());
        resultMap.put("batch_fee", batchFee.toString());
        resultMap.put("batch_num", batchNum.toString());
        resultMap.put("detail_data", detailDataStr);

        return AlipaySubmit.buildRequest(resultMap, "post", "确认");
    }

    /**
     * 处理支付宝异步通知结果.
     * <p>
     * 这里需要注意:
     * 支付宝可能会对一次批量付款的申请会有多次的异步通知.
     * 商户必须判断商户网站中是否已经对该次的通知结果数据做过同样处理。
     *
     * @param servletRequest 请求体
     * @throws ApiException
     */
    @Override
    public void updateSubmitPayment(HttpServletRequest servletRequest) throws ApiException {
        Map<String, String> callbackParam = parseAlipayCallbackParam(servletRequest);
        //记录日志
        payLogService.savePayLog(Constants.PayLogType.CALLBACK, callbackParam);

        boolean verify = AlipayNotify.verify(callbackParam);
        if (!verify) {
            LOG.error("Verify notify params failure");
            throw new ApiException("校验通知参数失败");
        }

        //校验通过  进行分成结果处理
        String batchNo = callbackParam.get("batch_no");
        //1.解析成功的账户信息，更新申请状态
        String[] successDetails = splitDetail(callbackParam.get("success_details"));
        if (successDetails != null) {
            for (String detail : successDetails) {
                MerchantShareApply notifyDetail = buildMerchantShareApply(batchNo, detail, true);
                MerchantShareApply merchantShareApply = queryRecordByNotifyDetail(notifyDetail);
                updateMerchantApplyInfo(merchantShareApply.getId(), detail, Constants.MerchantShareApplyStatus.SUCCESS);
            }
        }
        //2.解析失败的账户的信息，更新申请状态
        String[] failDetails = splitDetail(callbackParam.get("fail_details"));
        if (failDetails != null) {
            for (String detail : failDetails) {
                MerchantShareApply notifyDetail = buildMerchantShareApply(batchNo, detail, false);
                MerchantShareApply merchantShareApply = queryRecordByNotifyDetail(notifyDetail);
                updateMerchantApplyInfo(merchantShareApply.getId(), detail, Constants.MerchantShareApplyStatus.FAILURE);
                //回退用户资产信息
                rollbackMerchantAsset(merchantShareApply.getMerchantId(), merchantShareApply.getMerchantType(), merchantShareApply.getAmount().add(merchantShareApply.getFee()));
            }
        }
    }

    /**
     * 分割detail信息
     *
     * @param detail
     * @return
     */
    private String[] splitDetail(String detail) {
        if (detail == null) {
            return null;
        }
        return detail.split("\\|");
    }

    /**
     * 回退用户资产
     */
    private void rollbackMerchantAsset(String merchantId, String merchantType, BigDecimal amount) throws ApiException {
//        MerchantAssets merchantAssets = merchantAssetsMapper.selectForLock(merchantType, merchantId);
//        if (merchantAssets == null) {
//            LOG.info("Query merchant asset failure, merchantId:{}, merchantType:{}, amount:{}",
//                    new Object[]{merchantId, merchantType, amount});
//            throw new ApiException("商户资产信息不存在");
//        }
        merchantAssetsMapper.updateBalance(merchantType, merchantId, amount);
    }

    /**
     * 获取商户提款申请信息
     *
     * @param notifyDetail
     * @return
     * @throws ApiException
     */
    private MerchantShareApply queryRecordByNotifyDetail(MerchantShareApply notifyDetail) throws ApiException {
        MerchantShareApply param = new MerchantShareApply();
        param.setAccount(notifyDetail.getAccount());
        param.setSerialNo(notifyDetail.getSerialNo());
        param.setBatchNo(notifyDetail.getBatchNo());
        param.setStatus(Constants.MerchantShareApplyStatus.DOING);
        MerchantShareApply merchantShareApply = merchantShareApplyMapper.selectOne(param);
        if (merchantShareApply == null) {
            LOG.info("Query merchant apply record failure, notifyDetail:{}", notifyDetail);
            throw new ApiException("获取商户待处理申请提款信息失败");
        }
        return merchantShareApply;
    }

    /**
     * 构造商户的申请更新参数
     *
     * @return
     * @throws ApiException
     */
    private void updateMerchantApplyInfo(Long id, String remark, String status) throws ApiException {
        MerchantShareApply updateParam = new MerchantShareApply();
        updateParam.setId(id);
        updateParam.setStatus(status);
        updateParam.setRemark(remark);
        merchantShareApplyMapper.updateByPrimaryKeySelective(updateParam);
        LOG.info("update merchant share apply info, merchantApplyInfo:{}", updateParam);
    }

    /**
     * 构造商户提款申请信息参数
     *
     * @param batchNo
     * @param detail
     * @param payFlag
     * @return
     * @throws ApiException
     */
    private MerchantShareApply buildMerchantShareApply(String batchNo, String detail, boolean payFlag) throws ApiException {
        MerchantShareApply shareApply = new MerchantShareApply();
        shareApply.setBatchNo(batchNo);
        //201611052313070^247522363@qq.com^何永余^5.00^S^^201611050140323524^20161105231347
        String[] details = detail.split("\\^");
        if (details == null || details.length != AlipayConfig.notify_detail_count) {
            throw new ApiException("回调参数有误");
        }
        shareApply.setSerialNo(details[0]);
        shareApply.setAccount(details[1]);
        shareApply.setAccountName(details[2]);
        shareApply.setAmount(new BigDecimal(details[3]));
        shareApply.setRemark(detail);
        shareApply.setPaySuccess(payFlag);
        return shareApply;
    }

    /**
     * 获取alipay的会点参数
     *
     * @param servletRequest
     * @return
     */
    private Map<String, String> parseAlipayCallbackParam(HttpServletRequest servletRequest) {
        Map<String, String> callbackParam = new HashedMap();
        Enumeration<String> parameterNames = servletRequest.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            callbackParam.put(name, servletRequest.getParameter(name));
        }
        LOG.info("aplipay callback param:{}", callbackParam);
        return callbackParam;
    }
}