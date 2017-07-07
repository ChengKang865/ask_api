package com.autoask.service.log;

/**
 * @author hyy
 * @create 2016-11-05 14:41
 */
public interface PayLogService {

    /**
     * 保存支付日志
     *
     * @param type
     * @param content
     */
    void savePayLog(String type, Object content);
}
