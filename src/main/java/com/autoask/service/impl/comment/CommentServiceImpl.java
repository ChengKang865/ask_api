package com.autoask.service.impl.comment;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.comment.GoodsCommentDao;
import com.autoask.dao.comment.OrderCommentDao;
import com.autoask.dao.user.UserInfoDao;
import com.autoask.entity.mongo.comment.GoodsComment;
import com.autoask.entity.mongo.comment.OrderComment;
import com.autoask.entity.mongo.user.UserInfo;
import com.autoask.entity.mysql.User;
import com.autoask.mapper.UserMapper;
import com.autoask.service.comment.CommentService;
import com.autoask.service.user.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @author hyy
 * @create 16/11/20 15:47
 */
@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private GoodsCommentDao goodsCommentDao;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderCommentDao orderCommentDao;

    @Override
    public ListSlice<GoodsComment> getProductComment(String productId, Integer start, Integer limit) throws ApiException {
        if (null == start || start.compareTo(0) < 0) {
            throw new ApiException("参数非法");
        }
        if (null == limit || limit.compareTo(0) < 0) {
            throw new ApiException("参数非法");
        }

        long totalNum = goodsCommentDao.count(Query.query(Criteria.where("productId").is(productId)));

        Criteria criteria = new Criteria("productId").is(productId);
        Query query = Query.query(criteria);
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        query.skip(start).limit(limit);
        List<GoodsComment> goodsCommentList = goodsCommentDao.find(query);
        if (CollectionUtils.isNotEmpty(goodsCommentList)) {
            //讲所有的评论的手机号码隐藏
            for (GoodsComment goodsComment : goodsCommentList) {
                goodsComment.setUserPhone(goodsComment.getUserPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            }
        }


        return new ListSlice<>(goodsCommentList, totalNum);
    }

    @Override
    public ListSlice<GoodsComment> getGoodsCommentList(String orderId, String phone, Date startTime, Date endTime, Integer start, Integer limit) throws ApiException {
        ListSlice<GoodsComment> slice = getPureGoodsCommentList(orderId, phone, startTime, endTime, start, limit);
        List<GoodsComment> goodsCommentList = slice.getResult();
        if (CollectionUtils.isNotEmpty(goodsCommentList)) {
            initUserInfo(goodsCommentList);
            //
            initOrderComment(goodsCommentList);
        }
        return slice;
    }

    private ListSlice<GoodsComment> getPureGoodsCommentList(String orderId, String phone, Date startTime, Date endTime, Integer start, Integer limit) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotEmpty(orderId)) {
            criteria.and("orderId").is(orderId);
        }
        if (StringUtils.isNotEmpty(phone)) {
            User user = userService.getUserByPhone(phone);
            if (null != user) {
                criteria.and("userId").is(user.getUserId());
            } else {
                return new ListSlice<>(new ArrayList<GoodsComment>(), 0L);
            }
        }
        if (null != startTime && null != endTime) {
            criteria.and("createTime").gte(startTime).lte(endTime);
        }
        if (null == startTime && null != endTime) {
            criteria.and("createTime").lte(endTime);
        }
        if (null != startTime && null == endTime) {
            criteria.and("createTime").gte(startTime);
        }
        long totalNum = goodsCommentDao.count(Query.query(criteria));
        Query query = Query.query(criteria);
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        query.skip(start).limit(limit);
        List<GoodsComment> goodsCommentList = goodsCommentDao.find(query);
        return new ListSlice<>(goodsCommentList, totalNum);
    }

    private void initUserInfo(List<GoodsComment> goodsCommentList) {
        HashSet<String> userIdSet = new HashSet<>();
        for (GoodsComment goodsComment : goodsCommentList) {
            userIdSet.add(goodsComment.getUserId());
        }
        List<String> userIdList = new ArrayList<>(userIdSet.size());
        userIdList.addAll(userIdSet);

        Map<String, User> userMap = userService.getUserMap(userIdList);
        Map<String, UserInfo> userInfoMap = userService.getUserInfoMap(userIdList);

        for (GoodsComment goodsComment : goodsCommentList) {
            String userId = goodsComment.getUserId();
            goodsComment.setUser(userMap.get(userId));
            goodsComment.setUserInfo(userInfoMap.get(userId));
        }
    }

    private void initOrderComment(List<GoodsComment> goodsCommentList) {
        HashSet<String> orderIdSet = new HashSet<>();
        for (GoodsComment goodsComment : goodsCommentList) {
            orderIdSet.add(goodsComment.getOrderId());
        }
        List<OrderComment> orderCommentList = orderCommentDao.find(Query.query(Criteria.where("orderId").in(orderIdSet)));
        if (CollectionUtils.isNotEmpty(orderCommentList)) {
            HashMap<String, OrderComment> orderIdOrderCommentMap = new HashMap<>(orderCommentList.size());
            for (OrderComment orderComment : orderCommentList) {
                String orderId = orderComment.getOrderId();
                orderIdOrderCommentMap.put(orderId, orderComment);
            }
            for (GoodsComment goodsComment : goodsCommentList) {
                String orderId = goodsComment.getOrderId();
                goodsComment.setOrderComment(orderIdOrderCommentMap.get(orderId));
            }
        }
    }

    @Override
    public void replyContent(String orderGoodsId, String replayContent) throws ApiException {
        GoodsComment goodsComment = goodsCommentDao.findOne(Query.query(Criteria.where("orderGoodsId").is(orderGoodsId)));
        if (null == goodsComment) {
            throw new ApiException("评价内容不存在");
        }
        if (StringUtils.isEmpty(replayContent)) {
            throw new ApiException("回复内容不能为空");
        }
        GoodsComment updateEntity = new GoodsComment();
        updateEntity.setReplyContent(replayContent);
        updateEntity.setReplyId(LoginUtil.getLoginId());
        updateEntity.setReplyTime(DateUtil.getDate());
        goodsCommentDao.updateSelective(goodsComment.getId(), updateEntity);
    }
}
