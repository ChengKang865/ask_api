package com.autoask.service.impl.order;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.dao.comment.GoodsCommentDao;
import com.autoask.dao.comment.OrderCommentDao;
import com.autoask.dao.user.UserInfoDao;
import com.autoask.entity.mongo.comment.GoodsComment;
import com.autoask.entity.mongo.comment.OrderComment;
import com.autoask.entity.mongo.user.UserInfo;
import com.autoask.entity.mysql.User;
import com.autoask.mapper.UserMapper;
import com.autoask.service.order.OrderCommentService;
import com.autoask.service.user.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by hyy on 2016/12/20.
 */
@Service
public class OrderCommentServiceImpl implements OrderCommentService {

    @Autowired
    private OrderCommentDao orderCommentDao;

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsCommentDao goodsCommentDao;

    @Override
    public ListSlice<OrderComment> getOrderCommentList(String orderId, String phone, Date startTime, Date endTime, Integer start, Integer limit) throws ApiException {
        ListSlice<OrderComment> slice = getPureOrderCommentList(orderId, phone, startTime, endTime, start, limit);
        List<OrderComment> orderCommentList = slice.getResult();
        if (CollectionUtils.isNotEmpty(orderCommentList)) {
            //初始化用户信息
            initUserInfo(orderCommentList);
            //初始化各个订单中的各个商品的评论列表
            initGoodsCommentList(orderCommentList);
        }
        return slice;
    }

    private ListSlice<OrderComment> getPureOrderCommentList(String orderId, String phone, Date startTime, Date endTime, Integer start, Integer limit) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotEmpty(orderId)) {
            criteria.and("orderId").is(orderId);
        }
        if (StringUtils.isNotEmpty(phone)) {
            User user = userService.getUserByPhone(phone);
            if (null != user) {
                criteria.and("userId").is(user.getUserId());
            }
        }
        if (null != startTime) {
            criteria.and("createTime").gte(startTime);
        }
        if (null != endTime) {
            criteria.and("createTime").lte(endTime);
        }
        long totalNum = orderCommentDao.count(Query.query(criteria));
        Query query = Query.query(criteria);
        new Sort(new Sort.Order(Sort.Direction.DESC, "createTime"));
        query.skip(start).limit(limit);
        List<OrderComment> orderCommentList = orderCommentDao.find(query);
        return new ListSlice<>(orderCommentList, totalNum);
    }

    private void initUserInfo(List<OrderComment> orderCommentList) {
        HashSet<String> userIdSet = new HashSet<>();
        for (OrderComment orderComment : orderCommentList) {
            userIdSet.add(orderComment.getUserId());
        }
        List<String> userIdList = new ArrayList<>(userIdSet.size());
        userIdList.addAll(userIdSet);

        Map<String, User> userMap = userService.getUserMap(userIdList);
        Map<String, UserInfo> userInfoMap = userService.getUserInfoMap(userIdList);

        for (OrderComment orderComment : orderCommentList) {
            String userId = orderComment.getUserId();
            orderComment.setUser(userMap.get(userId));
            orderComment.setUserInfo(userInfoMap.get(userId));
        }
    }

    private void initGoodsCommentList(List<OrderComment> orderCommentList) {
        ArrayList<String> orderIdList = new ArrayList<>(orderCommentList.size());
        for (OrderComment orderComment : orderCommentList) {
            orderIdList.add(orderComment.getOrderId());
        }
        List<GoodsComment> goodsCommentList = goodsCommentDao.find(Query.query(Criteria.where("orderId").in(orderIdList)));
        if (CollectionUtils.isNotEmpty(goodsCommentList)) {
            //初始化 orderId goodsComment 的map
            HashMap<String, List<GoodsComment>> orderIdGoodsCommentMap = new HashMap<>(orderCommentList.size());
            for (GoodsComment goodsComment : goodsCommentList) {
                String orderId = goodsComment.getOrderId();
                List<GoodsComment> itemList = orderIdGoodsCommentMap.get(orderId);
                if (null == itemList) {
                    itemList = new ArrayList<>();
                }
                itemList.add(goodsComment);
                orderIdGoodsCommentMap.put(orderId, itemList);
            }
            //重新遍历，设置goodsCommentList
            for (OrderComment orderComment : orderCommentList) {
                orderComment.setGoodsCommentList(orderIdGoodsCommentMap.get(orderComment.getOrderId()));
            }
        }
    }
}
