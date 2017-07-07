package com.autoask.service.impl.config;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.config.RichTextConfigDao;
import com.autoask.entity.common.SessionInfo;
import com.autoask.entity.mongo.config.RichTextConfig;
import com.autoask.service.config.RichTextConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by hyy on 2016/12/4.
 */
@Service
public class RichTextConfigServiceImpl implements RichTextConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RichTextConfigServiceImpl.class);

    @Autowired
    private RichTextConfigDao richTextConfigDao;

    @Override
    public void addConfig(RichTextConfig config) {
        try {
            SessionInfo userInfo = LoginUtil.getSessionInfo();
            config.setCreatorId(userInfo.getLoginId());
            config.setCreatorType(userInfo.getLoginType());
        } catch (ApiException e) {
            LOGGER.error("Get user info failure", e);
        }

        config.setCreateTime(new Date());
        richTextConfigDao.save(config);
    }

    @Override
    public int modifyConfig(RichTextConfig config) {
        Update update = new Update();
        update.set("channel", config.getChannel());
        update.set("title", config.getTitle());
        update.set("content", config.getContent());
        try {
            SessionInfo userInfo = LoginUtil.getSessionInfo();
            config.setModifyId(userInfo.getLoginId());
            config.setModifyType(userInfo.getLoginType());
        } catch (ApiException e) {
            LOGGER.error("Get user info failure", e);
        }
        config.setModifyTime(new Date());

        return richTextConfigDao.update(config.getId(), update);
    }

    @Override
    public void deleteConfig(String id) throws ApiException {
        if (richTextConfigDao.findById(id) == null) {
            throw new ApiException("被删除的对象不存在");
        }
        richTextConfigDao.deleteById(id);
    }

    @Override
    public List<RichTextConfig> queryConfigs(String type, String channel, String title) {
        Criteria criteria = Criteria.where("type").is(type);
        if (StringUtils.isNoneBlank(title)) {
            criteria = criteria.and("title").is(title);
        }
        if (StringUtils.isNoneBlank(channel)) {
            criteria = criteria.and("channel").is(channel);
        }
        return richTextConfigDao.find(Query.query(criteria));
    }

    @Override
    public RichTextConfig getConfigById(String id) {
        return richTextConfigDao.findById(id);
    }
}
