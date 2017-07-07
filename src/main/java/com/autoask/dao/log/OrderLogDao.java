package com.autoask.dao.log;

import com.autoask.common.exception.ApiException;
import com.autoask.dao.BaseDao;
import com.autoask.entity.mongo.log.OrderLog;

import java.util.List;

/**
 * Created by hyy on 2016/12/14.
 */
public interface OrderLogDao extends BaseDao<OrderLog, String> {

    /**
     * 保存订单日志
     *
     * @param orderId
     * @param operateType
     * @param content
     * @throws ApiException
     */
    void saveOrderLog(String orderId, String operateType, String content, String loginType, String loginId);

    void saveOrderLogList(List<String> orderIdList, String operateType, String content, String loginType, String loginId);

    /**
     * 获取订单日志
     *
     * @param orderId
     * @return
     */
    List<OrderLog> getOrderLogList(String orderId);
}
