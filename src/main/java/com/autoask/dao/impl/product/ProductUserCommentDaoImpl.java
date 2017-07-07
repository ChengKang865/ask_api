package com.autoask.dao.impl.product;

import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.product.ProductUserCommentDao;
import com.autoask.entity.mongo.product.ProductUserComment;
import org.springframework.stereotype.Repository;

/**
 * Created by hp on 16-9-24.
 */
@Repository("productUserCommentDao")
public class ProductUserCommentDaoImpl extends BaseDaoImpl<ProductUserComment, String> implements ProductUserCommentDao {
}
