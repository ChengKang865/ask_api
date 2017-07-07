package com.autoask.service.impl;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.BaseDao;
import com.autoask.entity.mongo.BaseEntity;
import com.autoask.service.BaseMongoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mapping.context.InvalidPersistentPropertyPath;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by weid on 16-8-14.
 */
public abstract class BaseMongoServiceImpl<T extends BaseEntity> implements BaseMongoService<T> {

    private Class cls;

    protected Class getCls() {
        if (this.cls == null) {
            ParameterizedType type = (ParameterizedType) getClass()
                    .getGenericSuperclass();
            this.cls = ((Class) type.getActualTypeArguments()[0]);
        }
        return this.cls;
    }

    /**
     * 创建对象的方法
     *
     * @param emptyEntity 待创建对象
     */
    @Override
    public void create(T emptyEntity) throws ApiException {
        checkBaseValidation(emptyEntity);
        checkPreCreateValidation(emptyEntity);
        emptyEntity.setModifyTime(DateUtil.getDate());
        emptyEntity.setCreateTime(DateUtil.getDate());
        emptyEntity.setCreatorId(LoginUtil.getSessionInfo().getLoginId());
        emptyEntity.setCreatorType(LoginUtil.getSessionInfo().getLoginType());
        emptyEntity.setDeleteFlag(false);
        getMainDao().save(emptyEntity);
    }

    @Override
    public List<T> listAll() {
        return getMainDao().find(Query.query(getBaseCriteria()));
    }


    @Override
    public T findById(String id) {
        Query query = Query.query(getBaseCriteria().and("_id").is(id));
        return getMainDao().findOne(query);
    }

    @Override
    public T findOne(Query query) {
        addBaseCriteria(query);
        return getMainDao().findOne(query);
    }

    @Override
    public List<T> findByIds(Collection<String> ids) {
        Query query = Query.query(getBaseCriteria().and("_id").in(ids));
        return getMainDao().find(query);
    }

    @Override
    public List<T> find(Query query) {
        addBaseCriteria(query);
        return getMainDao().find(query);
    }

    @Override
    public long count() {
        return getMainDao().count(Query.query(getBaseCriteria()));
    }

    @Override
    public long count(Query query) {
        addBaseCriteria(query);
        return getMainDao().count(query);
    }


    @Override
    public void update(Query query, Update update) throws ApiException {
        addBaseCriteria(query);
        update.set("modifyTime", DateUtil.getDate());
        update.set("modifyId", LoginUtil.getSessionInfo().getLoginId());
        update.set("modifyType", LoginUtil.getSessionInfo().getLoginType());
        getMainDao().update(query, update);
    }

    @Override
    public void update(String id, Update update) throws ApiException {
        Query idQuery = Query.query(getBaseCriteria().and("_id").is(id));
        update.set("modifyTime", DateUtil.getDate());
        update.set("modifyId", LoginUtil.getSessionInfo().getLoginId());
        update.set("modifyType", LoginUtil.getSessionInfo().getLoginType());
        getMainDao().updateFirst(idQuery, update);
    }

    @Override
    public void updateFirst(Query query, Update update) throws ApiException {
        addBaseCriteria(query);
        update.set("modifyTime", DateUtil.getDate());
        update.set("modifyId", LoginUtil.getSessionInfo().getLoginId());
        update.set("modifyType", LoginUtil.getSessionInfo().getLoginType());
        getMainDao().updateFirst(query, update);
    }

    @Override
    public void updateAll(Query query, Update update) throws ApiException {
        addBaseCriteria(query);
        update.set("modifyTime", DateUtil.getDate());
        update.set("modifyId", LoginUtil.getSessionInfo().getLoginId());
        update.set("modifyType", LoginUtil.getSessionInfo().getLoginType());
        getMainDao().updateAll(query, update);
    }

    /**
     * 直接更新对象的方法, null字段不会更新, 自带校验方法
     *
     * @param id     对象id
     * @param entity 对象
     * @throws ApiException           操作异常
     * @throws IllegalAccessException 类定义错误
     */
    @Override
    public void updateSelective(String id, T entity) throws ApiException, IllegalAccessException {
        if (StringUtils.isEmpty(id)) {
            throw new ApiException("缺少id");
        }
        if (entity == null) {
            throw new ApiException("待更新对象不能为空");
        }
        T toUpdate = findById(id);
        if (null == toUpdate) {
            throw new ApiException("id错误");
        }

        BaseDao<T, String> mainDao = getMainDao();
        mainDao.selectiveUpdateBean(toUpdate, entity);
        entity.setModifyTime(DateUtil.getDate());
        entity.setModifyId(LoginUtil.getSessionInfo().getLoginId());
        entity.setModifyType(LoginUtil.getSessionInfo().getLoginType());
        checkBaseValidation(entity);
        checkPreUpdateValidation(entity);
        mainDao.updateSelective(id, entity);
    }

    @Override
    public void deleteById(String id) throws ApiException {
        Update deleteUpdate = Update.update("deleteFlag", true)
                .set("deleteTime", DateUtil.getDate()).set("deleteId", LoginUtil.getSessionInfo().getLoginId())
                .set("deleteType", LoginUtil.getSessionInfo().getLoginType());
        Query idQuery = Query.query(getBaseCriteria().and("_id").is(id));
        int affectNum = getMainDao().updateFirst(idQuery, deleteUpdate);
        if (affectNum == 0) {
            throw new ApiException("未找到指定的数据");
        }
    }

    @Override
    public void delete(Query query) throws ApiException {
        Update deleteUpdate = Update.update("deleteFlag", true).set("deleteTime", DateUtil.getDate())
                .set("deleteId", LoginUtil.getSessionInfo().getLoginId())
                .set("deleteType", LoginUtil.getSessionInfo().getLoginType());
        int affectNum = 0;
        try {
            affectNum = getMainDao().updateFirst(query, deleteUpdate);
        } catch (InvalidPersistentPropertyPath e) {
            e.printStackTrace();
        }
        if (affectNum == 0) {
            throw new ApiException("未找到指定的数据");
        }
    }

    @Override
    public Criteria getBaseCriteria() {
        return Criteria.where("deleteFlag").ne(true);
    }

    @Override
    public boolean isActive(String id) {
        long num = this.count(Query.query(Criteria.where("id").is(id)));
        return 0 != num;
    }

    @Override
    public Criteria timePeriod(String startTime, String endTime) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            Long fromTime = Long.parseLong(startTime);
            Long toTime = Long.parseLong(startTime);
            criteria.and("createTime").gte(new Date(fromTime)).and("createTime").lte(new Date(toTime));
        } else if (StringUtils.isNotEmpty(startTime) && StringUtils.isEmpty(endTime)) {
            Long fromTime = Long.parseLong(startTime);
            criteria.and("createTime").gte(new Date(fromTime));
        } else if (StringUtils.isEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            Long toTime = Long.parseLong(endTime);
            criteria.and("createTime").lte(new Date(toTime));
        }
        return criteria;
    }

    @Override
    public void addBaseCriteria(Query query) {
        Object deleteFlag = query.getQueryObject().get("deleteFlag");
        if (null == deleteFlag) {
            query.addCriteria(getBaseCriteria());
        }
    }

    @Override
    public void checkBaseValidation(T entity) throws ApiException {

    }

    @Override
    public void checkPreUpdateValidation(T entity) throws ApiException {

    }

    @Override
    public void checkPreCreateValidation(T entity) throws ApiException {

    }
}
