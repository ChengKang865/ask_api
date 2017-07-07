package com.autoask.dao.notice;

import com.autoask.common.exception.ApiException;
import com.autoask.dao.BaseDao;
import com.autoask.entity.mongo.notice.NoticeItem;

/**
 * Created by hyy on 2016/12/20.
 */
public interface NoticeItemDao extends BaseDao<NoticeItem, String> {

    void updateNoticeItemRead(String id) throws ApiException;
}
