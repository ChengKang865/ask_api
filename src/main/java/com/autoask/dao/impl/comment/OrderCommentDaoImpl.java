package com.autoask.dao.impl.comment;

import com.autoask.dao.comment.OrderCommentDao;
import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.entity.mongo.comment.OrderComment;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 * @create 2016-11-03 18:35
 */
@Repository
public class OrderCommentDaoImpl extends BaseDaoImpl<OrderComment, String> implements OrderCommentDao {

    @Override
    public OrderComment getOrderCommentByOrderId(String orderId) {
        return this.findOne(Query.query(Criteria.where("orderId").is(orderId)));
    }
}