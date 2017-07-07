package com.autoask.dao.impl.express;

import org.springframework.stereotype.Repository;

import com.autoask.dao.express.MailFreeDao;
import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.entity.mongo.express.MailFree;

/**
 * 免邮设置
 * @author ck
 *
 * @Create 2017年4月8日上午10:05:57
 */
@Repository("mailFreeDao")
public class MailFreeDaoImpl  extends BaseDaoImpl<MailFree, String> implements MailFreeDao {

}
