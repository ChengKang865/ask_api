package com.autoask.service.impl.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.autoask.common.exception.ApiException;
import com.autoask.dao.config.ConfigDao;
import com.autoask.entity.mongo.config.Config;
import com.autoask.service.common.ConfigService;

/**
 * Created by hp on 16-10-10.
 */
@Service("configService")
public class ConfigServiceImpl implements ConfigService {
	
    @Autowired
	private ConfigDao configDao;

	@Override
	public void insertConfigData(String type, String name, String val, String mark) throws ApiException {
		Config fig = new Config();
		fig.setName(name);
		fig.setType(type);
		fig.setVal(val);
		fig.setMark(mark);
		configDao.save(fig);
	}

	@Override
	public void updateConfigData(Config config) throws ApiException {
		configDao.updateSelective(config.getId(), config);
	}
}
