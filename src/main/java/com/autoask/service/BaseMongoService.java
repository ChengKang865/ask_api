package com.autoask.service;

import com.autoask.common.exception.ApiException;
import com.autoask.dao.BaseDao;
import com.autoask.entity.mongo.BaseEntity;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.List;

/**
 * Created by weid on 16-8-14.
 */
public interface BaseMongoService<T extends BaseEntity> {

    BaseDao<T, String> getMainDao();

    void create(T emptyEntity) throws ApiException;

    List<T> listAll();

    T findById(String id);

    T findOne(Query query);

    List<T> findByIds(Collection<String> ids);

    List<T> find(Query query);

    long count();

    long count(Query query);

    void update(Query query, Update update) throws ApiException;

    void update(String id, Update update) throws ApiException;

    void updateFirst(Query query, Update update) throws ApiException;

    void updateAll(Query query, Update update) throws ApiException;

    void updateSelective(String id, T entity) throws ApiException, IllegalAccessException;

    void deleteById(String id) throws ApiException;

    void delete(Query query) throws ApiException;

    Criteria getBaseCriteria();

    boolean isActive(String id);

    Criteria timePeriod(String startTime, String endTime);

    void addBaseCriteria(Query query);

    void checkBaseValidation(T entity) throws ApiException;

    void checkPreUpdateValidation(T entity) throws ApiException;

    void checkPreCreateValidation(T entity) throws ApiException;
}
