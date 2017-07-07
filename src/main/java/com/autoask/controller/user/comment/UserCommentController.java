package com.autoask.controller.user.comment;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mongo.comment.GoodsComment;
import com.autoask.entity.mongo.comment.OrderComment;
import com.autoask.entity.mongo.product.ProductUserComment;
import com.autoask.service.comment.CommentService;
import com.autoask.service.order.UOrderService;
import com.autoask.service.product.ProductUserCommentService;
import com.autoask.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hyy
 * @create 16/10/9 02:55
 */
@Controller
@RequestMapping("/user/comment/")
public class UserCommentController {

    private static final Logger LOG = LoggerFactory.getLogger(UserCommentController.class);


    @Autowired
    private UOrderService uOrderService;

    @Autowired
    private CommentService commentService;


    /**
     * 获取产品的评论列表
     *
     * @param productId 产品 id
     * @param start     起始位置
     * @param end       end
     * @return 评论列表
     */
    @RequestMapping("product/list/")
    @ResponseBody
    public ResponseDo listProductComment(@RequestParam("productId") String productId,
                                         @RequestParam("start") Integer start,
                                         @RequestParam("end") Integer end) {
        try {
            ListSlice<GoodsComment> commentListSlice = commentService.getProductComment(productId, start, end - start);
            return ResponseDo.buildSuccess(commentListSlice);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 评价订单
     *
     * @param orderComment
     * @return
     */
    @RequestMapping(value = "order/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo commentOrder(@RequestBody OrderComment orderComment) {
        try {
            if (StringUtils.isEmpty(orderComment.getOrderId())) {
                throw new ApiException("订单号不能为空");
            }
            uOrderService.insertOrderComment(orderComment);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 评论 订单/订单快递 商品
     */
    @RequestMapping(value = "goods/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo commentOrderGoods(@RequestBody GoodsComment goodsComment) {
        try {
            uOrderService.insertGoodsComment(goodsComment);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
