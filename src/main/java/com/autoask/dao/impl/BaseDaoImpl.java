package com.autoask.dao.impl;

import com.autoask.common.exception.ApiException;
import com.autoask.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

/**
 * @author hyy
 * @craete 2016/7/25 14:49
 */
public class BaseDaoImpl<T, K> implements BaseDao<T, K> {

    private Class cls;

    @Autowired
    protected MongoTemplate mongoTemplate;

    protected Class<T> getCls() {
        if (this.cls == null) {
            ParameterizedType type = (ParameterizedType) getClass()
                    .getGenericSuperclass();
            this.cls = ((Class) type.getActualTypeArguments()[0]);
        }
        return this.cls;
    }

    @Override
    public List<T> listAll() {
        return mongoTemplate.findAll(getCls());
    }

    @Override
    public T findById(K id) {
        T result = mongoTemplate.findById(id, getCls());
        return result;
    }

    @Override
    public T findOne(Query query) {
        return mongoTemplate.findOne(query, getCls());
    }

    @Override
    public List<T> findByIds(Collection<K> ids) {
        return mongoTemplate.find(Query.query(Criteria.where("_id").in(ids)), getCls());
    }

    @Override
    public List<T> findByIds(List<K> ids) {
        return mongoTemplate.find(Query.query(Criteria.where("_id").in(ids)), getCls());
    }

    @Override
    public List<T> findByIds(K[] ids) {
        return mongoTemplate.find(Query.query(Criteria.where("_id").in(ids)), getCls());
    }

    @Override
    public List<T> find(Query query) {
        return mongoTemplate.find(query, getCls());
    }

    @Override
    public void save(T entity) {
        mongoTemplate.save(entity);
    }

    @Override
    public void saveList(List<T> entity) {
        mongoTemplate.insert(entity, getCls());
    }

    @Override
    public void deleteById(K id) {
        mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), getCls());
    }

    @Override
    public void deleteByIds(Collection<K> ids) {
        mongoTemplate.remove(Query.query(Criteria.where("_id").in(ids)), getCls());
    }

    @Override
    public void delete(Query query) {
        mongoTemplate.remove(query, getCls());
    }

    @Override
    public long count() {
        return mongoTemplate.count(null, getCls());
    }

    @Override
    public long count(Query query) {
        return mongoTemplate.count(query, getCls());
    }

    @Override
    public int update(Query query, Update update) {
        return mongoTemplate.updateMulti(query, update, getCls()).getN();
    }

    @Override
    public int update(K id, Update update) {
        Query idQuery = Query.query(Criteria.where("_id").is(id));
        return mongoTemplate.updateFirst(idQuery, update, getCls()).getN();
    }

    @Override
    public int updateFirst(Query query, Update update) {
        return mongoTemplate.updateFirst(query, update, getCls()).getN();
    }

    @Override
    public int updateAll(Query query, Update update) {
        return mongoTemplate.updateMulti(query, update, getCls()).getN();
    }

    @Override
    public void updateSelective(K key, T entity) throws ApiException {
        try {
            Update update = getSelectiveUpdate(entity);
            mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(key)), update, getCls());
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }


    @Override
    public void updateSelective(Query query, T entity) throws ApiException {
        try {
            Update update = getSelectiveUpdate(entity);
            mongoTemplate.updateFirst(query, update, getCls());
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public Update getSelectiveUpdate(T entity) throws IllegalAccessException {
        Update update = new Update();
        for (Class<?> nowCls = entity.getClass(); nowCls != Object.class; nowCls = nowCls.getSuperclass()) {
            Field[] declaredFields = nowCls.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                if (null != declaredField.get(entity) && isSaveField(declaredField)) {
                    update.set(declaredField.getName(), declaredField.get(entity));
                }
            }
        }
        return update;
    }

    @Override
    public T selectiveUpdateBean(T toUpdate, T update) throws IllegalAccessException {
        for (Class<?> nowCls = update.getClass(); nowCls != Object.class; nowCls = nowCls.getSuperclass()) {
            Field[] declaredFields = nowCls.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                if (null != declaredField.get(update) && isSaveField(declaredField)) {
                    declaredField.set(toUpdate, declaredField.get(update));
                }
            }
        }
        return toUpdate;
    }

    private String getCollectionName(Object object) {
        Document annotation = object.getClass().getAnnotation(Document.class);
        if (null == annotation) {
            throw new RuntimeException("the object has no Document Annotation");
        }
        if (StringUtils.isNotEmpty(annotation.collection())) {
            return annotation.collection();
        } else {
            String simpleName = object.getClass().getSimpleName();
            char[] chars = simpleName.toCharArray();
            if (chars[0] >= 'A' && chars[0] <= 'Z') {
                chars[0] += 32;
            } else {
                throw new RuntimeException("the class first letter must be up case");
            }
            return new String(chars);
        }
    }


    /**
     * 判断类的注解是否需要更新,过滤点@Id @Transient 的注解  以及 public static 等属性
     *
     * @param field
     * @return
     */
    private boolean isSaveField(Field field) {
        int modifiers = field.getModifiers();
        if (Modifier.isAbstract(modifiers) || Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers) ||
                Modifier.isPublic(modifiers)) {
            return false;
        }
        if (Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers)) {
            if (field.getAnnotation(javax.persistence.Transient.class) == null && field.getAnnotation(Id.class) == null
                    && field.getAnnotation(org.springframework.data.annotation.Transient.class) == null) {
                return true;
            }
            return false;
        }
        return false;
    }
}
