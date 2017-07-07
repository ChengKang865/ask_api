package com.autoask.dao.config;

import com.autoask.common.exception.ApiException;
import com.autoask.dao.BaseDao;
import com.autoask.entity.mongo.config.Banner;

import java.util.List;

/**
 * @author hyy
 * @create 2016-10-25 13:43
 */
public interface BannerDao extends BaseDao<Banner, String> {

    /**
     * 获取渠道的bannerList
     *
     * @param channel
     * @return
     */
    List<Banner> getBannerList(String channel);

    /**
     * 插入 或者 更新 bannerList
     * 更新的时候直接删除之前的banner
     *
     * @param bannerList
     * @return
     */
    List<Banner> insertOrUpdateBannerList(List<Banner> bannerList, String channel) throws ApiException;
}
