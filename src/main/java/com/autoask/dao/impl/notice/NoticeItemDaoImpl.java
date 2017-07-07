package com.autoask.dao.impl.notice;

import com.autoask.common.exception.ApiException;
import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.notice.NoticeItemDao;
import com.autoask.entity.mongo.notice.NoticeItem;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by hyy on 2016/12/20.
 */
@Repository
public class NoticeItemDaoImpl extends BaseDaoImpl<NoticeItem, String> implements NoticeItemDao {

    @Override
    public void updateNoticeItemRead(String id) throws ApiException {
        NoticeItem updateEntity = new NoticeItem();
        updateEntity.setReadFlag(true);
        this.updateSelective(id, updateEntity);
    }
}
