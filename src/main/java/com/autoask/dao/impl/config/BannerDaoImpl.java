package com.autoask.dao.impl.config;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.config.BannerDao;
import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.entity.mongo.config.Banner;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hyy
 * @create 2016-10-25 13:43
 */
@Repository
public class BannerDaoImpl extends BaseDaoImpl<Banner, String> implements BannerDao {

    @Override
    public List<Banner> getBannerList(String channel) {
        Query query = Query.query(Criteria.where("channel").is(channel));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "sort")));
        List<Banner> channelList = this.find(query);
        return channelList;
    }

    @Override
    public List<Banner> insertOrUpdateBannerList(List<Banner> bannerList, String channel) throws ApiException {
        if (CollectionUtils.isEmpty(bannerList)) {
            throw new ApiException("插入bannerList为空");
        }
        this.delete(Query.query(Criteria.where("channel").is(channel)));
        for (Banner banner : bannerList) {
            banner.setId(null);
            banner.setCreateTime(DateUtil.getDate());
            banner.setCreatorId(LoginUtil.getSessionInfo().getLoginId());
            banner.setModifyTime(DateUtil.getDate());
            banner.setModifyId(LoginUtil.getSessionInfo().getLoginId());
        }
        for (Banner banner : bannerList) {
            this.save(banner);
        }
        return bannerList;
    }
}
