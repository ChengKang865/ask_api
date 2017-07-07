package com.autoask.dao.impl.notice;

import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.notice.NoticeDao;
import com.autoask.entity.mongo.notice.Notice;
import org.springframework.stereotype.Repository;

/**
 * Created by hyy on 2016/12/20.
 */
@Repository
public class NoticeDaoImpl extends BaseDaoImpl<Notice,String> implements NoticeDao {
}
