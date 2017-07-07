package com.autoask.controller.order;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.controller.common.BaseController;
import com.autoask.entity.mongo.comment.GoodsComment;
import com.autoask.entity.mongo.comment.OrderComment;
import com.autoask.service.comment.CommentService;
import com.autoask.service.order.OrderCommentService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by hyy on 2016/12/19.
 */
@Controller
@RequestMapping("order/comment/")
public class OrderCommentController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderCommentController.class);

    @Autowired
    private OrderCommentService orderCommentService;

    @Autowired
    private CommentService commentService;


    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo commentList(@RequestParam(value = "page") Integer page,
                                  @RequestParam(value = "count") Integer count,
                                  @RequestParam(value = "startTime", required = false) Date startTime,
                                  @RequestParam(value = "endTime", required = false) Date endTime,
                                  @RequestParam(value = "orderId", required = false) String orderId,
                                  @RequestParam(value = "phone", required = false) String phone) {
        Integer start = CommonUtil.pageToSkipStart(page, count);
        Integer limit = CommonUtil.cleanCount(count);
        try {
            ListSlice<OrderComment> orderCommentList = orderCommentService.getOrderCommentList(orderId, phone, startTime, endTime, start, limit);
            return ResponseDo.buildSuccess(orderCommentList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "goods/list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo listGoodsComment(@RequestParam(value = "page") Integer page,
                                       @RequestParam(value = "count") Integer count,
                                       @RequestParam(value = "startTime", required = false) Date startTime,
                                       @RequestParam(value = "endTime", required = false) Date endTime,
                                       @RequestParam(value = "orderId", required = false) String orderId,
                                       @RequestParam(value = "phone", required = false) String phone) {
        try {
            Integer start = CommonUtil.pageToSkipStart(page, count);
            Integer limit = CommonUtil.cleanCount(count);
            ListSlice<GoodsComment> goodsCommentList = commentService.getGoodsCommentList(orderId, phone, startTime, endTime, start, limit);
            return ResponseDo.buildSuccess(goodsCommentList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "goods/replay/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo replayOrderGoods(@Param("orderGoodsId") String orderGoodsId, @Param("replyContent") String replyContent) {
        try {
            commentService.replyContent(orderGoodsId, replyContent);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}