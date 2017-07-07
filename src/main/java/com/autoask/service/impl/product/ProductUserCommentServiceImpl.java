package com.autoask.service.impl.product;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.LoginUtil;
import com.autoask.common.util.MapUtil;
import com.autoask.dao.BaseDao;
import com.autoask.dao.product.ProductUserCommentDao;
import com.autoask.entity.mongo.product.ProductUserComment;
import com.autoask.entity.mysql.GoodsSnapshot;
import com.autoask.entity.mysql.GoodsSnapshotInfo;
import com.autoask.entity.mysql.User;
import com.autoask.service.impl.BaseMongoServiceImpl;
import com.autoask.service.product.GoodsService;
import com.autoask.service.product.ProductUserCommentService;
import com.autoask.service.user.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by hp on 16-9-24.
 */
@Service("productUserCommentService")
public class ProductUserCommentServiceImpl implements ProductUserCommentService {
    private static Logger LOG = LoggerFactory.getLogger(ProductUserCommentServiceImpl.class);

    @Autowired
    private ProductUserCommentDao productUserCommentDao;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserService userService;

    /**
     * 获取用户评论信息列表
     *
     * @param start     起始处
     * @param length    长度
     * @param productId 产品id
     * @return List<ProductUserComment>
     * @throws ApiException
     */
    @Override
    public List<ProductUserComment> getUserCommentList(Integer start, Integer length, String productId) throws ApiException {
        LOG.info("get user comment list.");

        if (StringUtils.isEmpty(productId)) {
            throw new ApiException("产品id不可为空!");
        }
        try {
            Criteria criteria = new Criteria("productId").is(productId).and("deleteFlag").is(false);
            Query query = Query.query(criteria);

            query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
            if (start != null) {
                query.skip(start);
            }
            if (length != null) {
                query.limit(length);
            } else {
                // 默认返回3条信息
                query.limit(3);
            }

            List<ProductUserComment> productUserCommentList = productUserCommentDao.find(query);
            for (ProductUserComment comment : productUserCommentList) {
                StringBuffer sb = new StringBuffer();
                String userPhone = comment.getUserPhone();
                sb.append(userPhone.substring(0, 2));
                sb.append("xxxx");
                sb.append(userPhone.substring(7, 10));
                comment.setUserPhone(sb.toString());
            }
            return productUserCommentList;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

}
