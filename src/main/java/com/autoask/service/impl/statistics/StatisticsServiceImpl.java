package com.autoask.service.impl.statistics;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.*;
import com.autoask.dao.comment.OrderCommentDao;
import com.autoask.dao.merchant.FactoryDao;
import com.autoask.dao.merchant.ServiceProviderDao;
import com.autoask.entity.mongo.notice.NoticeItem;
import com.autoask.entity.mysql.CardType;
import com.autoask.entity.mysql.GoodsNum;
import com.autoask.entity.mysql.MerchantShareApply;
import com.autoask.mapper.*;
import com.autoask.service.assets.MerchantAssetsRecordService;
import com.autoask.service.notice.NoticeService;
import com.autoask.service.product.GoodsNumService;
import com.autoask.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author hyy
 * @create 16/11/7 03:13
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private CardTypeMapper cardTypeMapper;

    @Autowired
    private MerchantShareApplyMapper merchantShareApplyMapper;

    @Autowired
    private FactoryDao factoryDao;

    @Autowired
    private ServiceProviderDao serviceProviderDao;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderCommentDao orderCommentDao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GoodsNumService goodsNumService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private MerchantAssetsRecordService merchantAssetsRecordService;


    @Override
    public Map<String, Object> getAutoASKStatisticsIndex() throws ApiException {
        HashMap<String, Object> resultMap = new HashMap<>();
        //待处理事务
        resultMap.put("toCheckGoodsNum", getToCheckGoodsNum());
        resultMap.put("toCheckCardTypeNum", getToCheckCardTypeNum());
        resultMap.put("doingShareApplyNum", getDoingShareApplyNum());

        //合作伙伴统计
        resultMap.put("newFactoryNum", getNewFactoryNum());
        resultMap.put("newServiceProviderNum", getNewServiceProviderNum());

        //销售 用户 评论等统计数据
        resultMap.put("todaySellAmount", getTodaySellAmount());
        resultMap.put("totalSellAmount", getTotalSellAmount());
        resultMap.put("todaySellNum", getTodaySellNum());
        resultMap.put("totalSellNum", getTotalSellNum());

        resultMap.put("todayOrderNum", getTodayOrderNum());
        resultMap.put("totalOrderNum", getTotalOrderNum());

        resultMap.put("todayCommentNum", getTodayCommentNum());
        resultMap.put("totalCommentNum", getTotalCommentNum());
        resultMap.put("todayRegisterNum", getTodayRegisterNum());
        resultMap.put("totalRegisterNum", getTotalRegisterNum());

        return resultMap;
    }

    private int getToCheckGoodsNum() {
        return goodsMapper.getToCheckGoodsNum();
    }

    private int getToCheckCardTypeNum() {
        Example example = new Example(CardType.class);
        example.createCriteria().andEqualTo("status", Constants.CardTypeStatus.TO_CHECK);
        return cardTypeMapper.selectCountByExample(example);
    }

    private int getDoingShareApplyNum() {
        Example example = new Example(MerchantShareApply.class);
        example.createCriteria().andEqualTo("status", Constants.MerchantShareApplyStatus.DOING);
        return merchantShareApplyMapper.selectCountByExample(example);
    }

    private long getNewFactoryNum() {
        return factoryDao.count(Query.query(Criteria.where("deleteFlag").is(false).and("createTime").gte(DateUtil.getEarlyMonthTime(DateUtil.getDate()))));
    }

    private long getNewServiceProviderNum() {
        return serviceProviderDao.count(Query.query(Criteria.where("deleteFlag").is(false).and("createTime").gte(DateUtil.getEarlyMonthTime(DateUtil.getDate()))));
    }

    private BigDecimal getTodaySellAmount() {
        BigDecimal amount = orderInfoMapper.getTodaySellAmount(Constants.OrderStatus.ALL_SELL_STATUS_LIST);
        return amount == null ? new BigDecimal(0) : amount;
    }

    private BigDecimal getTotalSellAmount() {
        BigDecimal amount = orderInfoMapper.getTotalSellAmount(Constants.OrderStatus.ALL_SELL_STATUS_LIST);
        return amount == null ? new BigDecimal(0) : amount;
    }

    private Long getTodaySellNum() {
        Long num = orderInfoMapper.getTodaySellGoodsNum(Constants.OrderStatus.ALL_SELL_STATUS_LIST);
        return num == null ? 0L : num;
    }

    private Long getTotalSellNum() {
        Long num = orderInfoMapper.getTotalSellGoodsNum(Constants.OrderStatus.ALL_SELL_STATUS_LIST);
        return num == null ? 0L : num;
    }

    private Long getTodayOrderNum() {
        Long num = orderInfoMapper.getTodayOrderNum(Constants.OrderStatus.ALL_SELL_STATUS_LIST);
        return num == null ? 0L : num;
    }

    private Long getTotalOrderNum() {
        Long num = orderInfoMapper.getTotalOrderNum(Constants.OrderStatus.ALL_SELL_STATUS_LIST);
        return num == null ? 0L : num;
    }

    private Long getTodayCommentNum() {
        return orderCommentDao.count(Query.query(Criteria.where("createTime").gte(DateUtil.getEarlyDayTime(DateUtil.getDate()))));
    }

    private Long getTotalCommentNum() {
        return orderCommentDao.count(null);
    }

    private Long getTodayRegisterNum() {
        return userMapper.getTodayRegisterNum();
    }

    private Long getTotalRegisterNum() {
        return userMapper.getTotalRegisterNum();
    }

    @Override
    public Map<String, Object> getServiceProviderIndex() throws ApiException {
        HashMap<String, Object> resultMap = new HashMap<>();
        ListSlice<NoticeItem> noticeItemList = noticeService.getNoticeItemList(null, null, null, false, 0, 3);
        resultMap.put("noticeItemList", noticeItemList);
//        List<GoodsNum> serviceProviderLackList = goodsNumService.getLackingGoodsNum(Constants.MerchantType.SERVICE_PROVIDER, 0, 3);
        resultMap.put("serviceProviderLackList", null);

        //业绩统计
        String serviceProviderId = LoginUtil.getLoginId();
        Date now = DateUtil.getDate();
        Date yearStartTime = DateUtil.getEarlyMonthTime(now);
        Date monthStartTime = DateUtil.getEarlyMonthTime(now);

        BigDecimal monthSellAmount = orderInfoMapper.getServiceProviderSell(serviceProviderId, monthStartTime, now,
                Constants.OrderStatus.SP_SELL_STATUS_LIST);
        resultMap.put("monthSellAmount", BigDecimalUtil.clean(monthSellAmount));

        BigDecimal yearSellAmount = orderInfoMapper.getServiceProviderSell(serviceProviderId, monthStartTime, now,
                Constants.OrderStatus.SP_SELL_STATUS_LIST);
        resultMap.put("yearSellAmount", BigDecimalUtil.clean(yearSellAmount));

        //收入统计
        BigDecimal monthIncome = merchantAssetsRecordService.countIncome(Constants.MerchantType.SERVICE_PROVIDER, serviceProviderId, monthStartTime, now);
        resultMap.put("monthIncome", BigDecimalUtil.clean(monthIncome));
        BigDecimal yearIncome = merchantAssetsRecordService.countIncome(Constants.MerchantType.SERVICE_PROVIDER, serviceProviderId, yearStartTime, now);
        resultMap.put("yearIncome", BigDecimalUtil.clean(yearIncome));

        return resultMap;
    }

    @Override
    public Map<String, Object> getFactoryIndex() throws ApiException {


        HashMap<String, Object> resultMap = new HashMap<>();
        ListSlice<NoticeItem> noticeItemList = noticeService.getNoticeItemList(null, null, null, false, 0, 3);
        resultMap.put("noticeItemList", noticeItemList);

//        List<GoodsNum> factoryLackList = goodsNumService.getLackingGoodsNum(Constants.MerchantType.FACTORY, 0, 3);
        resultMap.put("factoryLackList", null);

        String factoryId = LoginUtil.getLoginId();
        Date now = DateUtil.getDate();
        Date yearStartTime = DateUtil.getEarlyMonthTime(now);
        Date monthStartTime = DateUtil.getEarlyMonthTime(now);

        BigDecimal monthSellAmount = orderInfoMapper.getFactorySell(Constants.MerchantType.FACTORY, factoryId,
                Constants.OrderStatus.FACTORY_SELL_STATUS_LIST, monthStartTime, now);
        resultMap.put("monthSellAmount", BigDecimalUtil.clean(monthSellAmount));

        BigDecimal yearSellAmount = orderInfoMapper.getFactorySell(Constants.MerchantType.FACTORY, factoryId,
                Constants.OrderStatus.FACTORY_SELL_STATUS_LIST, yearStartTime, now);
        resultMap.put("yearSellAmount", BigDecimalUtil.clean(yearSellAmount));


        //收入统计
        BigDecimal monthIncome = merchantAssetsRecordService.countIncome(Constants.MerchantType.FACTORY, factoryId, monthStartTime, now);
        resultMap.put("monthIncome", BigDecimalUtil.clean(monthIncome));
        BigDecimal yearIncome = merchantAssetsRecordService.countIncome(Constants.MerchantType.FACTORY, factoryId, yearStartTime, now);
        resultMap.put("yearIncome", BigDecimalUtil.clean(yearIncome));

        return resultMap;
    }
}
