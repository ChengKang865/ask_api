package com.autoask.dao.impl.config;

import com.autoask.dao.config.RichTextConfigDao;
import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.entity.mongo.config.RichTextConfig;
import org.springframework.stereotype.Repository;

/**
 * Created by hyy on 2016/12/4.
 */
@Repository
public class RichTextConfigDaoImpl extends BaseDaoImpl<RichTextConfig, String> implements RichTextConfigDao {
}
