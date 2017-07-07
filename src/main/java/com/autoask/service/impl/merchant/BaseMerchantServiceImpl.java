package com.autoask.service.impl.merchant;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.entity.mongo.merchant.BaseMerchant;
import com.autoask.service.impl.BaseMongoServiceImpl;
import com.autoask.service.merchant.BaseMerchantService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by weid on 16-8-14.
 */
public abstract class BaseMerchantServiceImpl<T extends BaseMerchant> extends BaseMongoServiceImpl<T> implements BaseMerchantService<T> {

    @Override
    public void checkBaseValidation(T entity) throws ApiException {
        if (entity == null) {
            throw new ApiException("对象不能为空");
        }
        if (StringUtils.isEmpty(entity.getPhone())) {
            throw new ApiException("手机号不能为空");
        }
        if (StringUtils.isEmpty(entity.getName())) {
            throw new ApiException("名称不能为空");
        }
        if (StringUtils.isEmpty(entity.getPayType())) {
            throw new ApiException("收款方式不能为空");
        }
        if (isNameDuplicated(entity)) {
            throw new ApiException("名称重复,请重新挑选名称");
        }
        if (isPhoneDuplicated(entity)) {
            throw new ApiException("手机号重复");
        }
    }

    @Override
    public void checkPreUpdateValidation(T entity) throws ApiException {
        if (StringUtils.isEmpty(entity.getId())) {
            throw new ApiException("缺少id");
        }
    }

    @Override
    public void checkPreCreateValidation(T entity) throws ApiException {

    }

    public boolean isNameDuplicated(T entity) throws ApiException {
        Criteria criteria = Criteria.where("name").is(entity.getName());
        if (StringUtils.isNotEmpty(entity.getId())) {
            criteria.and("id").ne(entity.getId());
        }
        List<T> merchants = find(Query.query(criteria));
        if (CollectionUtils.isEmpty(merchants)) {
            return false;
        }
        return true;
    }

    public boolean isPhoneDuplicated(T entity) throws ApiException {
        Criteria criteria = Criteria.where("phone").is(entity.getPhone());
        if (StringUtils.isNotEmpty(entity.getId())) {
            criteria.and("id").ne(entity.getId());
        }
        List<T> merchants = find(Query.query(criteria));
        if (CollectionUtils.isEmpty(merchants)) {
            return false;
        }
        return true;
    }
}