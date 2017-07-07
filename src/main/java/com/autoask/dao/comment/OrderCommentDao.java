package com.autoask.dao.comment;

import com.autoask.dao.BaseDao;
import com.autoask.entity.mongo.comment.OrderComment;

/**
 * @author hyy
 * @create 2016-11-03 18:34
 */
public interface OrderCommentDao extends BaseDao<OrderComment, String> {

    OrderComment getOrderCommentByOrderId(String orderId);
}
