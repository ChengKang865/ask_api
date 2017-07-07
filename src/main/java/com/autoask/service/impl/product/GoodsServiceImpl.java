package com.autoask.service.impl.product;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.*;
import com.autoask.dao.merchant.FactoryDao;
import com.autoask.dao.product.GoodsSnapshotMetaDao;
import com.autoask.entity.mongo.merchant.Factory;
import com.autoask.entity.mongo.product.GoodsSnapshotMeta;
import com.autoask.entity.mysql.Goods;
import com.autoask.entity.mysql.GoodsSnapshot;
import com.autoask.entity.mysql.GoodsSnapshotInfo;
import com.autoask.entity.mysql.Product;
import com.autoask.mapper.GoodsMapper;
import com.autoask.mapper.GoodsSnapshotInfoMapper;
import com.autoask.mapper.GoodsSnapshotMapper;
import com.autoask.mapper.ProductMapper;
import com.autoask.service.product.GoodsService;
import com.autoask.service.product.ProductService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {

    private static final Logger LOG = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsSnapshotMapper goodsSnapshotMapper;

    @Autowired
    private GoodsSnapshotInfoMapper goodsSnapshotInfoMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private GoodsSnapshotMetaDao goodsSnapshotMetaDao;

    @Autowired
    private FactoryDao factoryDao;

    @Autowired
    private ProductService productService;

    @Override
    public void addGoodsSnapshot(GoodsSnapshot goodsSnapshot) throws ApiException {
        LOG.info("add goods and goods snapshot.");
        // 基础校验
        basicVerify(goodsSnapshot);

        // 校验商品名称冲突
        Example example = new Example(Goods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", goodsSnapshot.getName());
        criteria.andEqualTo("deleteFlag", false);
        int goodsCount = goodsMapper.selectCountByExample(example);
        if (0 != goodsCount) {
            throw new ApiException("商品名字冲突!");
        }

        try {
            String goodsId = CodeGenerator.uuid();
            String goodsSnapshotId = CodeGenerator.uuid();

            goodsSnapshot.setGoodsId(goodsId);
            goodsSnapshot.setGoodsSnapshotId(goodsSnapshotId);
            goodsSnapshot.setCreateTime(DateUtil.getDate());
            goodsSnapshot.setCreatorId(LoginUtil.getSessionInfo().getLoginId());
            goodsSnapshot.setGoodsSaleFlag(false);
            goodsSnapshot.setPopularRank(setBasicIntegerProperty(goodsSnapshot.getPopularRank()));
            goodsSnapshot.setFactoryId(setBasicStringProperty(goodsSnapshot.getFactoryId()));
            goodsSnapshot.setFactoryFee(setBasicBigDecimalProperty(goodsSnapshot.getFactoryFee()));
            goodsSnapshot.setAdFee(setBasicBigDecimalProperty(goodsSnapshot.getAdFee()));
            goodsSnapshot.setHandleFee(setBasicBigDecimalProperty(goodsSnapshot.getHandleFee()));
            goodsSnapshot.setPromoteFee(setBasicBigDecimalProperty(goodsSnapshot.getPromoteFee()));
            goodsSnapshot.setCheckId(setBasicStringProperty(goodsSnapshot.getCheckId()));
            goodsSnapshot.setDeleteFlag(false);
            goodsSnapshot.setStatus(Constants.GoodsStatus.TO_CHECK);
            goodsSnapshot.setSnapshotCheckStatus(Constants.GoodsStatus.TO_CHECK);
            goodsSnapshotMapper.insert(goodsSnapshot);

            // 创建 Goods
            saveGoods(goodsSnapshot);
            // 创建 goodsSnapshotInfoList
            saveSnapshotInfoList(goodsSnapshot);
            // 创建 goodsMeta
            saveSnapshotMeta(goodsSnapshot);

        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public void updateGoodsSnapshot(GoodsSnapshot goodsSnapshot) throws ApiException {
        LOG.info("Update goods.");

        // basic verify
        basicVerify(goodsSnapshot);

        // 校验goods
        String goodsId = goodsSnapshot.getGoodsId();
        if (null == goodsId) {
            throw new ApiException("goodsId缺失");
        }
        Example example = new Example(Goods.class);
        example.or().andEqualTo("goodsId", goodsId);
        List<Goods> goodsList = goodsMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(goodsList)) {
            throw new ApiException("商品不存在，页面可能已经失效，请从列表页重新进入");
        }
        Goods preGoods = goodsList.get(0);
        if (!StringUtils.equals(preGoods.getName(), goodsSnapshot.getName())) {
            //名字更改了
            Example conflictExp = new Example(Goods.class);
            conflictExp.createCriteria().andEqualTo("name", goodsSnapshot.getName()).andEqualTo("deleteFlag", false);
            int conflictNum = goodsMapper.selectCountByExample(conflictExp);
            if (conflictNum != 0) {
                throw new ApiException("商品名称重复");
            }
        }

        if (!StringUtils.equals(preGoods.getNameEn(), goodsSnapshot.getNameEn())) {
            //名字更改了
            Example conflictExp = new Example(Goods.class);
            conflictExp.createCriteria().andEqualTo("nameEn", goodsSnapshot.getNameEn()).andEqualTo("deleteFlag", false);
            int conflictNum = goodsMapper.selectCountByExample(conflictExp);
            if (conflictNum != 0) {
                throw new ApiException("商品名称重复");
            }
        }

        // 校验是否已有未审核的商品.
        List<GoodsSnapshot> goodsSnapUnCheckedList = getGoodsSnapUnCheckedList(goodsSnapshot);
        if (CollectionUtils.isNotEmpty(goodsSnapUnCheckedList)) {
            throw new ApiException("该商品正在审核中！不能提交修改");
        }

        try {
            // create goodsSnapshot
            goodsSnapshot.setGoodsId(goodsId);
            // 设置 goodsSnapshotId
            goodsSnapshot.setGoodsSnapshotId(CodeGenerator.uuid());

            goodsSnapshot.setCreateTime(DateUtil.getDate());
            goodsSnapshot.setCreatorId(LoginUtil.getSessionInfo().getLoginId());
            goodsSnapshot.setGoodsSaleFlag(false);
            goodsSnapshot.setPopularRank(setBasicIntegerProperty(goodsSnapshot.getPopularRank()));
            goodsSnapshot.setFactoryId(setBasicStringProperty(goodsSnapshot.getFactoryId()));
            goodsSnapshot.setFactoryFee(setBasicBigDecimalProperty(goodsSnapshot.getFactoryFee()));
            goodsSnapshot.setAdFee(setBasicBigDecimalProperty(goodsSnapshot.getAdFee()));
            goodsSnapshot.setHandleFee(setBasicBigDecimalProperty(goodsSnapshot.getHandleFee()));
            goodsSnapshot.setPromoteFee(setBasicBigDecimalProperty(goodsSnapshot.getPromoteFee()));
            goodsSnapshot.setCheckId(setBasicStringProperty(goodsSnapshot.getCheckId()));
            goodsSnapshot.setDeleteFlag(false);
            goodsSnapshot.setStatus(Constants.GoodsStatus.TO_CHECK);
            goodsSnapshot.setSnapshotCheckStatus(Constants.GoodsStatus.TO_CHECK);

            // 删除 id
            if (goodsSnapshot.getId() != null) {
                goodsSnapshot.setId(null);
            }
            goodsSnapshotMapper.insert(goodsSnapshot);

            // 创建snapshotInfo
            saveSnapshotInfoList(goodsSnapshot);
            // 创建
            saveSnapshotMeta(goodsSnapshot);

            //更新商品的checkSnapshotId
            Example goodsExp = new Example(Goods.class);
            goodsExp.createCriteria().andEqualTo("goodsId", goodsSnapshot.getGoodsId());
            Goods updateEntity = new Goods();
            updateEntity.setCheckSnapshotId(goodsSnapshot.getGoodsSnapshotId());
            goodsMapper.updateByExampleSelective(updateEntity, goodsExp);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public void deleteGoodsById(String goodsId) throws ApiException {
        LOG.info("delete goods");
        Example example = new Example(Goods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("goodsId", goodsId)
                .andEqualTo("deleteFlag", false);
        Goods update = new Goods();
        update.setDeleteFlag(true);
        try {
            int updateNum = goodsMapper.updateByExampleSelective(update, example);
            if (updateNum == 0) {
                throw new ApiException("删除失败,该商品不存在");
            }
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public void setGoodsNotSaleById(String goodsId) throws ApiException {
        LOG.info("offline goods");

        Example example = new Example(Goods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("goodsId", goodsId)
                .andEqualTo("deleteFlag", false)
                .andEqualTo("status", Constants.GoodsStatus.CHECKED);
        Goods update = new Goods();
        update.setSaleFlag(false);
        update.setModifyId(LoginUtil.getSessionInfo().getLoginId());
        update.setModifyTime(DateUtil.getDate());
        try {
            int updateNum = goodsMapper.updateByExampleSelective(update, example);
            if (updateNum == 0) {
                throw new ApiException("下线失败,该商品不存在.");
            }
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public void setGoodsSaleById(String goodsId) throws ApiException {
        LOG.info("online goods");
        Example example = new Example(Goods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("goodsId", goodsId)
                .andEqualTo("deleteFlag", false)
                .andEqualTo("status", Constants.GoodsStatus.CHECKED);
        Goods update = new Goods();
        update.setSaleFlag(true);
        update.setModifyId(LoginUtil.getSessionInfo().getLoginId());
        update.setModifyTime(DateUtil.getDate());
        try {
            int updateNum = goodsMapper.updateByExampleSelective(update, example);
            if (updateNum == 0) {
                throw new ApiException("上线失败,该商品不存在.");
            }
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public Goods getGoodsById(String goodsId) throws ApiException {
        LOG.info("get goods by id ");

        try {
            Goods goods = new Goods();
            goods.setDeleteFlag(false);
            goods.setGoodsId(goodsId);
            return goodsMapper.selectOne(goods);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public List<Goods> getGoodsList(Integer start, Integer length, Map<String, Object> queryParams) throws ApiException {
        LOG.info("get goods list");
        try {
            Example example = new Example(GoodsSnapshot.class);
            Example.Criteria criteria = example.createCriteria();
            for (Map.Entry<String, Object> param : queryParams.entrySet()) {
                if (!"".equals(param.getValue())) {
                    criteria.andEqualTo(param.getKey(), param.getValue());
                }
            }
            List<Goods> goodsList;
            if (start == 0 && length == 0) {
                goodsList = goodsMapper.selectByExample(example);
            } else {
                goodsList = goodsMapper.selectByExampleAndRowBounds(example, new RowBounds(start, length));
            }

            return goodsList;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public ListSlice<Goods> getGoodsList(Integer start, Integer length, String productCategoryId, String productId, String name) throws ApiException {
        Example query = new Example(Goods.class);
        Example.Criteria criteria = query.createCriteria();
        if (StringUtils.isNotEmpty(productCategoryId)) {
            criteria.andEqualTo("productId", productId);
        }
        if (StringUtils.isNotEmpty(productId)) {
            criteria.andEqualTo("productId", productId);
        }
        if (StringUtils.isNotEmpty(name)) {
            criteria.andLike("name", name);
        }
        criteria.andEqualTo("deleteFlag", false);
        int count = goodsMapper.selectCountByExample(query);
        List<Goods> goodsList = goodsMapper.selectByExampleAndRowBounds(query, new RowBounds(start, length));

        return new ListSlice<>(goodsList, new Long(count));
    }

    @Override
    public List<GoodsSnapshot> getGoodsSnapshotList(Integer start, Integer length, Map<String, Object> queryParams) throws ApiException {
        LOG.info("get goodsSnapshot list.");

        try {
            Example example = new Example(GoodsSnapshot.class);
            Example.Criteria criteria = example.createCriteria();
            for (Map.Entry<String, Object> param : queryParams.entrySet()) {
                if (!"".equals(param.getValue())) {
                    criteria.andEqualTo(param.getKey(), param.getValue());
                }
            }
            List<GoodsSnapshot> goodsSnapshotList;
            if (start == 0 && length == 0) {
                goodsSnapshotList = goodsSnapshotMapper.selectByExample(example);
            } else {
                goodsSnapshotList = goodsSnapshotMapper.selectByExampleAndRowBounds(example, new RowBounds(start, length));
            }

            return goodsSnapshotList;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public Integer getGoodsCount(Map<String, Object> queryParams) throws ApiException {
        Example example = new Example(Goods.class);
        Example.Criteria criteria = example.createCriteria();
        for (Map.Entry<String, Object> param : queryParams.entrySet()) {
            if (!"".equals(param.getValue())) {
                criteria.andEqualTo(param.getKey(), param.getValue());
            }
        }
        return goodsMapper.selectCountByExample(example);
    }

    @Override
    public Integer getGoodsSnapshotCount(Map<String, Object> queryParams) throws ApiException {
        Example example = new Example(GoodsSnapshot.class);
        Example.Criteria criteria = example.createCriteria();
        for (Map.Entry<String, Object> param : queryParams.entrySet()) {
            if (!"".equals(param.getValue())) {
                criteria.andEqualTo(param.getKey(), param.getValue());
            }
        }
        return goodsSnapshotMapper.selectCountByExample(example);
    }

    /**
     * 基础的参数校验集合
     *
     * @param goodsSnapshot 商品快照
     * @throws ApiException
     */
    public void basicVerify(GoodsSnapshot goodsSnapshot) throws ApiException {
        // 参数校验
        verifyParams(goodsSnapshot);
        // 产品校验
        verifyProduct(goodsSnapshot);
        // 服务类型校验
        verifyType(goodsSnapshot);
        // 商品价格校验
        verifyGoodsPrice(goodsSnapshot);
        // 代理工厂校验
        verifyFactory(goodsSnapshot);
        // 分成比例校验
        verifyRate(goodsSnapshot);
        //校验商品info key val 是否冲突
        verifyInfoConflict(goodsSnapshot);
    }

    /**
     * 校验基本参数
     *
     * @param goodsSnapshot 商品快照
     * @throws ApiException
     */
    public void verifyParams(GoodsSnapshot goodsSnapshot) throws ApiException {
        if (null == goodsSnapshot.getName()) {
            throw new ApiException("商品名称未指定");
        }
        if (null == goodsSnapshot.getNameEn()) {
            throw new ApiException("商品名称英文名未指定");
        }
    }

    /**
     * 校验工厂的合法性.
     *
     * @param goodsSnapshot 商品快照
     * @throws ApiException
     */
    public void verifyFactory(GoodsSnapshot goodsSnapshot) throws ApiException {
        if (StringUtils.equals(goodsSnapshot.getType(), Constants.GoodsType.ONLINE)) {
            String factoryId = goodsSnapshot.getFactoryId();
            if (StringUtils.isEmpty(factoryId)) {
                throw new ApiException("线上产品必须指定工厂");
            }
            Factory factory = factoryDao.findById(factoryId);
            if (null == factory) {
                throw new ApiException("工厂不存在");
            }
            BigDecimal factoryRate = goodsSnapshot.getFactoryFee();
            verifyRateNumber("制造费", factoryRate);
        }
    }

    /**
     * 校验商品价格的合法性.
     *
     * @param goodsSnapshot 商品快照
     * @throws ApiException
     */
    public void verifyGoodsPrice(GoodsSnapshot goodsSnapshot) throws ApiException {
        if (null == goodsSnapshot.getOnlinePrice()) {
            throw new ApiException("价格不能为空");
        }
        if (goodsSnapshot.getOnlinePrice().compareTo(new BigDecimal(0)) <= 0) {
            throw new ApiException("商品价格必须大于0");
        }
        //线上线下价格现在一致
        goodsSnapshot.setOfflinePrice(goodsSnapshot.getOnlinePrice());

        if (null == goodsSnapshot.getWeight()) {
            throw new ApiException("商品重量不能为空");
        }
        if (goodsSnapshot.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException("商品重量必须大于0");
        }
    }

    /**
     * 校验分成比例的合法性
     *
     * @param goodsSnapshot 商品快照
     * @throws ApiException
     */
    public void verifyRate(GoodsSnapshot goodsSnapshot) throws ApiException {

        if (StringUtils.equals(goodsSnapshot.getType(), Constants.GoodsType.ALL)) {
            BigDecimal adFee = goodsSnapshot.getAdFee();
            verifyRateNumber("广告费", adFee);
            BigDecimal handelFee = goodsSnapshot.getHandleFee();
            verifyRateNumber("修理工手工费", handelFee);
        }
        BigDecimal promoteFee = goodsSnapshot.getPromoteFee();
        verifyRateNumber("引流费", promoteFee);
    }

    public void verifyRateNumber(String rateErrorStr, BigDecimal rate) throws ApiException {
        if (null != rate) {
            if (rate.signum() < 0) {
                throw new ApiException(rateErrorStr + "不能为负数");
            }
        } else {
            throw new ApiException(rateErrorStr + "必须设定");
        }
    }

    /**
     * 校验product的合法性
     *
     * @param goodsSnapshot 商品快照
     * @throws ApiException
     */
    public void verifyProduct(GoodsSnapshot goodsSnapshot) throws ApiException {
        Example productExample = new Example(Product.class);
        productExample.or()
                .andEqualTo("productId", goodsSnapshot.getProductId())
                .andCondition(Constants.ConditionDeleteFlag.DELETE_FALSE);
        int productCount = productMapper.selectCountByExample(productExample);
        if (1 != productCount) {
            throw new ApiException("产品id错误.");
        }
        //检查meta信息是否存在重复
    }

    public void verifyType(GoodsSnapshot goodsSnapshot) throws ApiException {
        String type = goodsSnapshot.getType();
        if (StringUtils.isEmpty(type)) {
            throw new ApiException("type类型未设置");
        }

        if (!Constants.GoodsType.TYPE_LIST.contains(type)) {
            throw new ApiException("type类型错误");
        }
    }

    private void verifyInfoConflict(GoodsSnapshot goodsSnapshot) throws ApiException {
        boolean addFlag = true;
        String productId = goodsSnapshot.getProductId();
        List<Goods> goodsList = goodsMapper.getAllGoodsList(productId);
        if (StringUtils.isNotEmpty(goodsSnapshot.getGoodsId())) {
            addFlag = false;
        }
        List<GoodsSnapshotInfo> infoList = goodsSnapshot.getGoodsSnapshotInfoList();
        if (CollectionUtils.isNotEmpty(goodsList)) {
            for (Goods goods : goodsList) {
                GoodsSnapshot preSnapshot = goods.getGoodsSnapshot();
                boolean equalFlag = true;
                for (GoodsSnapshotInfo preInfo : preSnapshot.getGoodsSnapshotInfoList()) {
                    for (GoodsSnapshotInfo nowInfo : infoList) {
                        if (StringUtils.equals(nowInfo.getKeyName(), preInfo.getKeyName())) {
                            if (!StringUtils.equals(nowInfo.getValue(), preInfo.getValue())) {
                                equalFlag = false;
                                break;
                            }
                        }
                    }
                }
                if (equalFlag) {
                    if (addFlag) {
                        throw new ApiException("相同规格的产品已经存在");
                    } else {
                        if (!StringUtils.equals(goods.getGoodsId(), goodsSnapshot.getGoodsId())) {
                            throw new ApiException("相同规格的产品已经存在");
                        }
                    }
                }
            }
        }
    }

    /**
     * 创建goods
     *
     * @param goodsSnapshot 商品快照
     * @throws ApiException
     */
    public void saveGoods(GoodsSnapshot goodsSnapshot) throws ApiException {
        try {
            // 创建 Goods
            Goods goods = new Goods(goodsSnapshot);
            goods.setSaleFlag(goodsSnapshot.getGoodsSaleFlag());
            goodsMapper.insert(goods);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * 保存 goods 相关的信息
     *
     * @param goodsSnapshot 商品快照
     * @throws ApiException
     */
    public void saveSnapshotInfoList(GoodsSnapshot goodsSnapshot) throws ApiException {
        try {
            String snapshotId = goodsSnapshot.getGoodsSnapshotId();

            // 创建 GoodsSnapshotInfo
            List<GoodsSnapshotInfo> goodsSnapshotInfoList = goodsSnapshot.getGoodsSnapshotInfoList();
            if (CollectionUtils.isNotEmpty(goodsSnapshotInfoList)) {
                for (GoodsSnapshotInfo goodsSnapshotInfo : goodsSnapshotInfoList) {
                    goodsSnapshotInfo.setGoodsSnapshotInfoId(CodeGenerator.uuid());
                    goodsSnapshotInfo.setGoodsSnapshotId(snapshotId);
                }
                goodsSnapshotInfoMapper.insertList(goodsSnapshotInfoList);
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * 创建SnapshotMeta
     *
     * @param goodsSnapshot 商品快照
     * @throws ApiException
     */
    public void saveSnapshotMeta(GoodsSnapshot goodsSnapshot) throws ApiException {
        String snapshotId = goodsSnapshot.getGoodsSnapshotId();
        try {
            // 创建 GoodsSnapshotMeta
            GoodsSnapshotMeta goodsSnapshotMeta = goodsSnapshot.getGoodsSnapshotMeta();
            if (null != goodsSnapshotMeta) {
                goodsSnapshotMeta.setGoodsSnapshotId(snapshotId);
                goodsSnapshotMetaDao.save(goodsSnapshotMeta);
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    public Integer setBasicIntegerProperty(Integer number) {
        if (null == number) {
            return 0;
        }
        return number;
    }

    public String setBasicStringProperty(String string) {
        if (StringUtils.isEmpty(string)) {
            return "";
        }
        return string;
    }

    public BigDecimal setBasicBigDecimalProperty(BigDecimal number) {
        if (null == number) {
            return new BigDecimal(0);
        }
        return number;
    }

    /**
     * 获取未被审核过的snapshot列表
     *
     * @param goodsSnapshot 商品快照
     * @return snapshot 列表
     * @throws ApiException
     */
    public List<GoodsSnapshot> getGoodsSnapUnCheckedList(GoodsSnapshot goodsSnapshot) throws ApiException {
        Example example = new Example(GoodsSnapshot.class);
        example.or()
                .andEqualTo("goodsId", goodsSnapshot.getGoodsId())
                .andEqualTo("status", Constants.GoodsSnapshotStatus.TO_CHECK)
                .andCondition(Constants.ConditionDeleteFlag.DELETE_FALSE);
        return goodsSnapshotMapper.selectByExample(example);
    }

    @Override
    public GoodsSnapshot getGoodsSnapShotById(String goodsSnapshotId) throws ApiException {
        Example example = new Example(GoodsSnapshot.class);
        example.createCriteria().andEqualTo("deleteFlag", false).andEqualTo("goodsSnapshotId", goodsSnapshotId);
        try {
            GoodsSnapshot snapshot = new GoodsSnapshot();
            snapshot.setDeleteFlag(false);
            snapshot.setGoodsSnapshotId(goodsSnapshotId);
            GoodsSnapshot goodsSnapshot = goodsSnapshotMapper.selectOne(snapshot);
            if (null == goodsSnapshot) {
                throw new ApiException("未找到指定的商品快照");
            }
            // 设置 meta 信息
            goodsSnapshot.setGoodsSnapshotMeta(getGoodsSnapshotMetaByGoodsSnapshotId(goodsSnapshotId));
            // 设置 goodsInfoList 信息
            goodsSnapshot.setGoodsSnapshotInfoList(getGoodsSnapshotInfoList(goodsSnapshotId));

            return goodsSnapshot;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * 根据goodsId 获取商品快照信息,前端展示的,是 goodsSnapshot信息.
     *
     * @param goodsId 商品id
     * @return GoodsSnapshot
     * @throws ApiException
     */
    @Override
    public GoodsSnapshot getGoodsSnapshotInfoByGoodsId(String goodsId, String status) throws ApiException {
        LOG.info("get goodsSnapShot info by goods's id");
        if (!Constants.GoodsStatus.QUERY_LIST.contains(status)) {
            throw new ApiException("参数非法");
        }
        Goods goodsQuery = new Goods();
        goodsQuery.setDeleteFlag(false);
        goodsQuery.setGoodsId(goodsId);
        Goods goods = goodsMapper.selectOne(goodsQuery);
        if (null == goods) {
            throw new ApiException("商品不存在");
        }

        GoodsSnapshot snapshotQuery = new GoodsSnapshot();
        snapshotQuery.setGoodsId(goodsId);
        if (StringUtils.equals(status, Constants.GoodsStatus.CHECKED)) {
            // goods 绑定的 snapshotId
            snapshotQuery.setGoodsSnapshotId(goods.getGoodsSnapshotId());
        } else if (StringUtils.equals(status, Constants.GoodsStatus.TO_CHECK)) {
            // status 为 to_check
            snapshotQuery.setSnapshotCheckStatus(Constants.GoodsSnapshotStatus.TO_CHECK);
//            snapshotQuery.setGoodsSnapshotId(goods.getCheckSnapshotId());
        }
        GoodsSnapshot goodsSnapshot = goodsSnapshotMapper.selectOne(snapshotQuery);
        if (null == goodsSnapshot) {
            throw new ApiException("该商品在审核状态中，请在审核列表中打开");
        }

        // 设置 product 信息
        goodsSnapshot.setProduct(productService.getProduct(goodsSnapshot.getProductId()));
        // 设置 meta 信息
        goodsSnapshot.setGoodsSnapshotMeta(getGoodsSnapshotMetaByGoodsSnapshotId(goodsSnapshot.getGoodsSnapshotId()));
        // 设置 goodsInfoList 信息
        goodsSnapshot.setGoodsSnapshotInfoList(getGoodsSnapshotInfoList(goodsSnapshot.getGoodsSnapshotId()));

        return goodsSnapshot;
    }

    public GoodsSnapshotMeta getGoodsSnapshotMetaByGoodsSnapshotId(String goodsSnapshotId) throws ApiException {
        List<GoodsSnapshotMeta> goodsSnapshotMetaList = goodsSnapshotMetaDao.find(Query.query(Criteria.where("goodsSnapshotId")
                .is(goodsSnapshotId).and("deleteFlag").ne("true")));
        if (CollectionUtils.isEmpty(goodsSnapshotMetaList)) {
            return null;
        } else {
            return goodsSnapshotMetaList.get(0);
        }
    }

    @Override
    public List<GoodsSnapshotInfo> getGoodsSnapshotInfoList(String goodsSnapshotId) {
        Example example = new Example(GoodsSnapshotInfo.class);
        example.createCriteria().andEqualTo("goodsSnapshotId", goodsSnapshotId);
        return goodsSnapshotInfoMapper.selectByExample(example);
    }

    @Override
    public void passGoodsSnapshot(String goodsSnapshotId) throws ApiException {
        try {
            GoodsSnapshot goodsSnapshot = getGoodsSnapShotById(goodsSnapshotId);
            Goods goods = getGoodsById(goodsSnapshot.getGoodsId());
            if (null == goods) {
                throw new ApiException("商品信息异常");
            }
            basicVerify(goodsSnapshot);

            // 更新goodsSnapshot
            goodsSnapshot.setCheckTime(DateUtil.getDate());
            goodsSnapshot.setCheckId(LoginUtil.getSessionInfo().getLoginId());
            goodsSnapshot.setStatus(Constants.GoodsSnapshotStatus.CHECKED);
            goodsSnapshot.setSnapshotCheckStatus(Constants.GoodsSnapshotStatus.CHECKED);
            goodsSnapshotMapper.updateByPrimaryKeySelective(goodsSnapshot);

            // 绑定productId
            goods.setProductId(goodsSnapshot.getProductId());
            // 绑定snapshotId
            goods.setGoodsSnapshotId(goodsSnapshotId);
            goods.setName(goodsSnapshot.getName());
            goods.setNameEn(goodsSnapshot.getNameEn());
            goods.setOnlinePrice(goodsSnapshot.getOnlinePrice());
            goods.setOfflinePrice(goodsSnapshot.getOfflinePrice());
            goods.setType(goodsSnapshot.getType());
            goods.setStatus(Constants.GoodsStatus.CHECKED);
            goods.setFactoryId(goodsSnapshot.getFactoryId());
            goods.setFactoryFee(goodsSnapshot.getFactoryFee());
            goods.setAdFee(goodsSnapshot.getAdFee());
            goods.setHandleFee(goodsSnapshot.getHandleFee());
            goods.setPromoteFee(goodsSnapshot.getPromoteFee());
            goods.setCheckTime(DateUtil.getDate());
            goods.setCheckId(LoginUtil.getSessionInfo().getLoginId());
            //审核通过应该设置checkSnapshotId 为 snapshotId
            goods.setCheckSnapshotId(goods.getGoodsSnapshotId());

            goodsMapper.updateByPrimaryKeySelective(goods);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public void refuseGoodsSnapshot(String goodsSnapshotId) throws ApiException {
        try {
            GoodsSnapshot goodsSnapshot = getGoodsSnapShotById(goodsSnapshotId);
            if (null == goodsSnapshot) {
                throw new ApiException("商品不存在");
            }
            Goods goods = getGoodsById(goodsSnapshot.getGoodsId());
            if (null == goods) {
                throw new ApiException("商品不存在");
            }
            //审核不同过应该将商品的checkSnapshotId 设置为原来的 goodsSnapshotId
            //特别注意 如果商品是新建的则不做任何操作
            if (!StringUtils.equals(goods.getStatus(), Constants.GoodsStatus.TO_CHECK)) {
                goodsSnapshot.setSnapshotCheckStatus(Constants.GoodsSnapshotStatus.FAILURE);
                goodsSnapshot.setStatus(Constants.GoodsSnapshotStatus.FAILURE);

                //审核失败了商品的checkGoodsSnapshotId 应该修改为 null
                goodsSnapshotMapper.updateByPrimaryKeySelective(goodsSnapshot);
                goods.setCheckSnapshotId(goods.getGoodsSnapshotId());
                goodsMapper.updateByPrimaryKey(goods);
            }
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }
}
