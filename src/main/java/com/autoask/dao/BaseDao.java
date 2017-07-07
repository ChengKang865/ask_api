package com.autoask.dao;

import com.autoask.common.exception.ApiException;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.List;

/**
 * @author hyy
 * @craete 2016/7/25 14:49
 */
public interface BaseDao<T, K> {

    public List<T> listAll();

    public T findById(K key);

    public T findOne(Query query);

    public List<T> findByIds(Collection<K> ids);

    public List<T> findByIds(List<K> ids);

    public List<T> findByIds(K[] ids);

    public List<T> find(Query query);

    public void save(T entity);

    void saveList(List<T> entity);

    public void deleteById(K id);

    public void deleteByIds(Collection<K> ids);

    public void delete(Query query);

    public long count();

    public long count(Query query);

    public int update(Query query, Update update);

    public int update(K id, Update update);

    public int updateFirst(Query query, Update update);

    public int updateAll(Query query, Update update);

    public void updateSelective(K key, T entity) throws ApiException;

    public void updateSelective(Query query, T entity) throws ApiException;

    public Update getSelectiveUpdate(T entity) throws IllegalAccessException;

    public T selectiveUpdateBean(T toUpdate, T update) throws IllegalAccessException;
}
