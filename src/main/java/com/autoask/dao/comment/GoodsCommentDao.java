package com.autoask.dao.comment;

import com.autoask.dao.BaseDao;
import com.autoask.entity.mongo.comment.GoodsComment;

import java.util.List;

/**
 * @author hyy
 * @create 2016-11-03 18:37
 */
public interface GoodsCommentDao extends BaseDao<GoodsComment,String> {

    List<GoodsComment> getGoodsCommentListByOrderId(String orderId);
}
