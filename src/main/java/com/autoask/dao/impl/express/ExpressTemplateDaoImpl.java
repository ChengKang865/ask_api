package com.autoask.dao.impl.express;

import org.springframework.stereotype.Repository;

import com.autoask.dao.express.ExpressTemplateDao;
import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.entity.mongo.express.ExpressTemplate;
/**
 * 快递模板
 * @author ck
 *
 * @Create 2017年4月1日上午10:41:54
 */
@Repository("expressTemplateDao")
public class ExpressTemplateDaoImpl extends BaseDaoImpl<ExpressTemplate, String> implements ExpressTemplateDao {



}
