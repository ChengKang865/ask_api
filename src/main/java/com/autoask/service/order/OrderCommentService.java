package com.autoask.service.order;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mongo.comment.OrderComment;

import java.util.Date;

/**
 * Created by hyy on 2016/12/20.
 */
public interface OrderCommentService {

    ListSlice<OrderComment> getOrderCommentList(String orderId, String phone, Date startTime, Date endTime, Integer start, Integer limit) throws ApiException;
}
