package com.autoask.service.impl.product;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.merchant.ServiceProviderDao;
import com.autoask.dao.store.GoodsNumRecordDao;
import com.autoask.entity.mongo.merchant.BaseMerchant;
import com.autoask.entity.mongo.merchant.ServiceProvider;
import com.autoask.entity.mongo.store.GoodsNumRecord;
import com.autoask.entity.mysql.Goods;
import com.autoask.entity.mysql.GoodsNum;
import com.autoask.mapper.GoodsMapper;
import com.autoask.service.merchant.MerchantService;
import com.autoask.service.merchant.ServiceProviderService;
import com.autoask.service.product.GoodsNumRecordService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @author hyy
 * @create 2016-12-07 16:38
 */
@Service
public class GoodsNumRecordServiceImpl implements GoodsNumRecordService {

    @Autowired
    private GoodsNumRecordDao goodsNumRecordDao;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private ServiceProviderDao serviceProviderDao;

    @Override
    public ListSlice listGoodsNumRecord(String productCategoryId, String productId, String goodsId, String goodsName,
                                        String merchantType, String merchantId, Boolean addType, String serial,
                                        Date startTime, Date endTime, int start, int limit) throws ApiException {
        Query query = new Query();
        Criteria criteria = new Criteria();

        if (StringUtils.isNotEmpty(serial)) {
            criteria.and("serial").is(serial);
            List<GoodsNumRecord> goodsNumRecordList = goodsNumRecordDao.find(Query.query(criteria));
            long count = goodsNumRecordDao.count(Query.query(criteria));
            return new ListSlice(goodsNumRecordList, count);
        }

        String loginType = LoginUtil.getSessionInfo().getLoginType();
        if (StringUtils.equals(Constants.LoginType.STAFF, loginType)) {
            if (StringUtils.isNotEmpty(merchantType)) {
                criteria.and("merchantType").is(merchantType);
            } else {
                criteria.and("merchantType").in(Arrays.asList(Constants.MerchantType.FACTORY));
            }
            if (StringUtils.isNotEmpty(merchantId)) {
                criteria.and("merchantId").is(merchantId);
            }
        } else {
            throw new ApiException("没有权限");
        }
        criteria.and("addType").is(addType);

        //查询出所有的符合条件的商品id
        List<String> goodsIdList = goodsMapper.getGoodsIdList(productCategoryId, productId, goodsId, goodsName);
        if (CollectionUtils.isNotEmpty(goodsIdList)) {
            criteria.and("goodsInfoList").elemMatch(Criteria.where("goodsId").in(goodsIdList));
        }

        ArrayList<Criteria> criteriaArr = new ArrayList<>();
        if (null != startTime) {
            criteriaArr.add(Criteria.where("recordTime").gte(startTime));
        }
        if (null != endTime) {
            criteriaArr.add(Criteria.where("recordTime").lte(endTime));
        }
        if (CollectionUtils.isNotEmpty(criteriaArr)) {
            criteria.andOperator(criteriaArr.toArray(new Criteria[criteriaArr.size()]));
        }

        query.addCriteria(criteria);
        long totalNum = goodsNumRecordDao.count(query);

        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        query.skip(start).limit(limit);
        List<GoodsNumRecord> goodsNumRecordList = goodsNumRecordDao.find(query);
        if (CollectionUtils.isNotEmpty(goodsNumRecordList)) {
            List<BaseMerchant> baseMerchantList = new ArrayList<>(goodsNumRecordList.size());
            for (GoodsNumRecord goodsNumRecord : goodsNumRecordList) {
                BaseMerchant baseMerchant = new BaseMerchant();
                baseMerchant.setLoginType(goodsNumRecord.getMerchantType());
                baseMerchant.setId(goodsNumRecord.getMerchantId());
                baseMerchantList.add(baseMerchant);
            }
            Map<String, String> merchantNameMap = merchantService.getMerchantNameMap(baseMerchantList);
            if (MapUtils.isNotEmpty(merchantNameMap)) {
                for (GoodsNumRecord goodsNumRecord : goodsNumRecordList) {
                    goodsNumRecord.setMerchantName(merchantNameMap.get(goodsNumRecord.getMerchantType() + goodsNumRecord.getMerchantId()));
                }
            }
        }

        return new ListSlice(goodsNumRecordList, totalNum);
    }

    @Override
    public GoodsNumRecord getById(String id) throws ApiException {
        GoodsNumRecord goodsNumRecord = goodsNumRecordDao.findById(id);
        if (null == goodsNumRecord) {
            throw new ApiException("发货/退货单不存在");
        }
        BaseMerchant merchantInfo = merchantService.getMerchantInfo(goodsNumRecord.getMerchantType(), goodsNumRecord.getMerchantId());
        String name = merchantInfo.getName();
        goodsNumRecord.setMerchantName(name);
        return goodsNumRecord;
    }
}
