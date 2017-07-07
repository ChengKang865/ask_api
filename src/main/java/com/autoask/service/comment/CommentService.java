package com.autoask.service.comment;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mongo.comment.GoodsComment;

import java.util.Date;

/**
 * @author hyy
 * @create 16/11/20 15:46
 */
public interface CommentService {

    ListSlice<GoodsComment> getProductComment(String productId, Integer start, Integer limit) throws ApiException;

    ListSlice<GoodsComment> getGoodsCommentList(String orderId, String phone, Date startTime, Date endTime, Integer start, Integer limit) throws ApiException;

    void replyContent(String orderGoodsId, String replyContent)throws ApiException;
}
