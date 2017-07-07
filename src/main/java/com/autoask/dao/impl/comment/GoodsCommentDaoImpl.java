package com.autoask.dao.impl.comment;

import com.autoask.dao.comment.GoodsCommentDao;
import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.entity.mongo.comment.GoodsComment;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hyy
 * @create 2016-11-03 18:37
 */
@Repository
public class GoodsCommentDaoImpl extends BaseDaoImpl<GoodsComment, String> implements GoodsCommentDao {

    @Override
    public List<GoodsComment> getGoodsCommentListByOrderId(String orderId) {
       return this.find(Query.query(Criteria.where("orderId").is(orderId)));
    }
}
