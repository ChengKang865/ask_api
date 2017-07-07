package com.autoask.service.product;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.product.ProductUserComment;
import com.autoask.service.BaseMongoService;

import java.util.List;

/**
 * Created by hp on 16-9-24.
 */
public interface ProductUserCommentService {

    List<ProductUserComment> getUserCommentList(Integer start, Integer length, String productId) throws ApiException;
}
