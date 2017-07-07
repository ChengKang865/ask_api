package com.autoask.dao.impl.log;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.log.OrderLogDao;
import com.autoask.entity.mongo.log.OrderLog;
import com.autoask.entity.mysql.OrderInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyy on 2016/12/14.
 */
@Repository
public class OrderLogDaoImpl extends BaseDaoImpl<OrderLog, String> implements OrderLogDao {

    @Override
    public void saveOrderLog(String orderId, String operateType, String content, String loginType, String loginId) {
        OrderLog orderLog = initOrderLog(orderId, operateType, content, loginType, loginId);
        save(orderLog);
    }

    @Override
    public void saveOrderLogList(List<String> orderIdList, String operateType, String content, String loginType, String loginId) {
        ArrayList<OrderLog> orderInfoList = new ArrayList<>(orderIdList.size());
        for (String orderId : orderIdList) {
            orderInfoList.add(initOrderLog(orderId, operateType, content, loginType, loginId));
        }
        saveList(orderInfoList);
    }

    private OrderLog initOrderLog(String orderId, String operateType, String content, String loginType, String loginId) {
        OrderLog orderLog = new OrderLog();
        orderLog.setId(null);
        orderLog.setOrderId(orderId);
        orderLog.setOperateType(operateType);
        orderLog.setContent(content);
        orderLog.setLoginType(loginType);
        orderLog.setLoginId(loginId);
        orderLog.setCreateTime(DateUtil.getDate());

        return orderLog;
    }

    @Override
    public List<OrderLog> getOrderLogList(String orderId) {
        Query query = Query.query(Criteria.where("orderId").is(orderId));
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "createTime")));
        return find(query);
    }
}
