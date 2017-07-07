package com.autoask.controller.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.service.common.AlipayService;
import com.autoask.service.log.PayLogService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 16-10-29.
 */
@RequestMapping(value = "/alipay/")
@Controller
public class AlipayController {

    private static final Logger LOG = LoggerFactory.getLogger(AlipayController.class);

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private PayLogService payLogService;

    /**
     * 批量支付调用接口
     *
     * @return
     * @throws ApiException
     */
    @RequestMapping(value = "/transfer/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo alipayCall(@RequestBody JSONObject paramJson) {
        JSONArray ids = paramJson.getJSONArray("applyIds");
        ArrayList<String> applyIds = new ArrayList<>(ids.size());
        for (int index = 0; index < ids.size(); index++) {
            applyIds.add(ids.getString(index));
        }
        LOG.info("POST method. Input param alipayEntity");

        try {
            if (CollectionUtils.isEmpty(applyIds)) {
                throw new ApiException("入参不能为空");
            }

            payLogService.savePayLog(Constants.PayLogType.APPLY_REQUEST, applyIds);

            // 格式化付款信息
            String sHtmlText = alipayService.updateMerchantAppliesAndAlipayApply(applyIds);
            LOG.info("sHtmlText: {}", sHtmlText);

            payLogService.savePayLog(Constants.PayLogType.APPLY, sHtmlText); //记录日志
            return ResponseDo.buildSuccess(sHtmlText);

        } catch (Exception e) {
            LOG.error("Post apply info failure", e);

            return ResponseDo.buildError("申请失败");
        }

    }

    @RequestMapping(value = "/notify/", method = RequestMethod.POST)
    public void alipayNotify(HttpServletRequest servletRequest, HttpServletResponse response) {
        try {
            alipayService.updateSubmitPayment(servletRequest);
            String data = "success";
            OutputStream outputStream = response.getOutputStream();//获取OutputStream输出流
            response.setHeader("content-type", "text/html;charset=UTF-8");//通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码

            byte[] dataByteArr = data.getBytes("UTF-8");//将字符转换成字节数组，指定以UTF-8编码进行转换
            outputStream.write(dataByteArr);//使用OutputStream流向客户端输出字节数组
        } catch (Exception e) {
            LOG.error("notify error: ", e.getMessage());
        }
    }
}
