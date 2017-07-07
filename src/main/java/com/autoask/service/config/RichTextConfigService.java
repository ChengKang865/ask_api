package com.autoask.service.config;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.config.RichTextConfig;

import java.util.List;

/**
 * Created by hyy on 2016/12/4.
 */
public interface RichTextConfigService {

    /**
     * 增加富文本配置信息
     *
     * @param config
     * @return
     */
    void addConfig(RichTextConfig config);

    /**
     * 修改富文本配置信息
     *
     * @param config
     * @return
     */
    int modifyConfig(RichTextConfig config);

    /**
     * 删除富文本配置信息
     *
     * @param id
     * @return
     */
    void deleteConfig(String id) throws ApiException;

    /**
     * 查询富文本配置信息
     *
     * @return
     */
    List<RichTextConfig> queryConfigs(String type, String channel, String title);


    /**
     * 根据id获取配置项
     *
     * @param id
     * @return
     */
    RichTextConfig getConfigById(String id);
}
