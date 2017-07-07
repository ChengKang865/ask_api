package com.autoask.service.impl.notice;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.merchant.FactoryDao;
import com.autoask.dao.merchant.ServiceProviderDao;
import com.autoask.dao.notice.NoticeDao;
import com.autoask.dao.notice.NoticeItemDao;
import com.autoask.entity.common.Address;
import com.autoask.entity.mongo.merchant.Factory;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.entity.mongo.notice.Notice;
import com.autoask.entity.mongo.notice.NoticeItem;
import com.autoask.service.notice.NoticeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by hyy on 2016/12/20.
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    private static final Logger LOG = LoggerFactory.getLogger(NoticeServiceImpl.class);

    @Autowired
    private FactoryDao factoryDao;

    @Autowired
    private ServiceProviderDao serviceProviderDao;

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private NoticeItemDao noticeItemDao;


    @Override
    public void insertNotice(Notice notice) throws ApiException {
        Date now = DateUtil.getDate();

        notice.setId(null);
        notice.setAddress(Address.cleanWithNull(notice.getAddress()));
        notice.setCreatorId(LoginUtil.getLoginId());
        notice.setCreateTime(now);
        noticeDao.save(notice);

        List<String> merchantIds = getMerchantIds(notice.getMerchantType(), notice.getAddress());
        if (CollectionUtils.isNotEmpty(merchantIds)) {
            List<NoticeItem> noticeItemList = new ArrayList<>(merchantIds.size());
            for (String merchantId : merchantIds) {
                NoticeItem noticeItem = new NoticeItem();
                noticeItem.setMerchantType(notice.getMerchantType());
                noticeItem.setMerchantId(merchantId);
                noticeItem.setCreateTime(now);
                noticeItem.setNoticeId(notice.getId());
                noticeItem.setReadFlag(false);

                noticeItemList.add(noticeItem);
            }

            noticeItemDao.saveList(noticeItemList);
        }
    }

    private List<String> getMerchantIds(String merchantType, Address address) {
        List<String> ids = null;
        Criteria criteria = new Criteria();
        criteria.and("deleteFlag").is(false);
        if (null != address) {
            if (StringUtils.isNotEmpty(address.getRegion())) {
                criteria.and("address.region").is(address.getRegion());
            }
            if (StringUtils.isNotEmpty(address.getCity())) {
                criteria.and("address.city").is(address.getCity());
            }
            if (StringUtils.isNotEmpty(address.getProvince())) {
                criteria.and("address.province").is(address.getProvince());
            }
        }
        Query query = Query.query(criteria);
        switch (merchantType) {
            case Constants.MerchantType.FACTORY:
                List<Factory> factoryList = factoryDao.find(query);
                if (CollectionUtils.isNotEmpty(factoryList)) {
                    ids = new ArrayList<>(factoryList.size());
                    for (Factory factory : factoryList) {
                        ids.add(factory.getId());
                    }
                }
                break;
            case Constants.MerchantType.SERVICE_PROVIDER:
                List<ServiceProvider> serviceProviderList = serviceProviderDao.find(query);
                if (CollectionUtils.isNotEmpty(serviceProviderList)) {
                    ids = new ArrayList<>(serviceProviderList.size());
                    for (ServiceProvider serviceProvider : serviceProviderList) {
                        ids.add(serviceProvider.getId());
                    }
                }
                break;
        }
        return ids;
    }


    @Override
    public ListSlice<Notice> getNoticeList(String name, String merchantType, Date startTime, Date endTime, Integer start, Integer limit) throws ApiException {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotEmpty(merchantType)) {
            criteria.and("merchantType").is(merchantType);
        }
        if (StringUtils.isNotEmpty(name)) {
            String nameReg = ".*" + name + ".*";
            criteria.and("title").regex(nameReg);
        }
        if (null != startTime && null != endTime) {
            criteria.and("createTime").gte(startTime).lte(endTime);
        }
        if (null == startTime && null != endTime) {
            criteria.and("createTime").lte(endTime);
        }
        if (null != startTime && null == endTime) {
            criteria.and("createTime").gte(startTime);
        }
        Query query = Query.query(criteria);
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        if (null != start) {
            query.skip(start);
        }
        if (null != limit) {
            query.limit(limit);
        }
        List<Notice> noticeList = noticeDao.find(query);
        long totalNum = noticeDao.count(Query.query(criteria));
        return new ListSlice<>(noticeList, totalNum);
    }


    @Override
    public Notice getNoticeById(String id) {
        return noticeDao.findById(id);
    }

    @Override
    public NoticeItem getNoticeItemById(String id) throws ApiException {
        NoticeItem noticeItem = noticeItemDao.findById(id);

        if (null != noticeItem) {
            //查看即更新为已读
            noticeItemDao.updateNoticeItemRead(id);
            Notice notice = noticeDao.findById(noticeItem.getNoticeId());
            noticeItem.setNotice(notice);
        }
        return noticeItem;
    }

    @Override
    public ListSlice<NoticeItem> getNoticeItemList(Date startTime, Date endTime, String content, Boolean readFlag, Integer start, Integer limit) throws ApiException {
        Criteria criteria = new Criteria();
        String merchantType = LoginUtil.getLoginType();
        String merchantId = LoginUtil.getLoginId();
        criteria.and("merchantType").is(merchantType);
        criteria.and("merchantId").is(merchantId);

        ArrayList<Criteria> criteriaArr = new ArrayList<>();
        if (null != startTime) {
            criteriaArr.add(Criteria.where("createTime").gte(startTime));
        }
        if (null != endTime) {
            criteriaArr.add(Criteria.where("createTime").lte(endTime));
        }
        if (null != readFlag) {
            criteriaArr.add(Criteria.where("readFlag").is(readFlag));
        }
        if (StringUtils.isNotEmpty(content)) {
            String contentReg = ".*" + content + ".*";
            Criteria regexCriteria = Criteria.where("title").regex(contentReg).orOperator(Criteria.where("content").regex(contentReg));
            List<Notice> noticeList = noticeDao.find(Query.query(regexCriteria));
            if (CollectionUtils.isNotEmpty(noticeList)) {
                List<String> noticeIdList = new ArrayList<>();
                for (Notice notice : noticeList) {
                    noticeIdList.add(notice.getId());
                }
                criteriaArr.add(Criteria.where("noticeId").in(noticeIdList));
            } else {
                return new ListSlice<>(new ArrayList<NoticeItem>(), 0L);
            }
        }
        if (CollectionUtils.isNotEmpty(criteriaArr)) {
            criteria.andOperator(criteriaArr.toArray(new Criteria[criteriaArr.size()]));
        }

        long totalNum = noticeItemDao.count(Query.query(criteria));

        Query query = Query.query(criteria);
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        if (null != start) {
            query.skip(start);
        }
        if (null != limit) {
            query.limit(limit);
        }
        List<NoticeItem> noticeItemList = noticeItemDao.find(query);

        //初始化关联的notice
        if (CollectionUtils.isNotEmpty(noticeItemList)) {
            ArrayList<String> noticeIds = new ArrayList<>(noticeItemList.size());
            for (NoticeItem noticeItem : noticeItemList) {
                noticeIds.add(noticeItem.getNoticeId());
            }
            List<Notice> noticeList = noticeDao.find(Query.query(Criteria.where("id").in(noticeIds)));
            HashMap<String, Notice> id2NoticeMap = new HashMap<>(noticeList.size());
            for (Notice notice : noticeList) {
                id2NoticeMap.put(notice.getId(), notice);
            }
            for (NoticeItem noticeItem : noticeItemList) {
                noticeItem.setNotice(id2NoticeMap.get(noticeItem.getNoticeId()));
            }
        }

        return new ListSlice<>(noticeItemList, totalNum);
    }
}
