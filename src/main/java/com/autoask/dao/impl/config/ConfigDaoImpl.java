package com.autoask.dao.impl.config;

import com.autoask.dao.config.ConfigDao;
import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.entity.mongo.config.Config;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 * @create 16/11/5 16:03
 */
@Repository
public class ConfigDaoImpl extends BaseDaoImpl<Config, String> implements ConfigDao {
}
