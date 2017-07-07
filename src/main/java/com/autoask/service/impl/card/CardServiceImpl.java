package com.autoask.service.impl.card;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.*;
import com.autoask.entity.common.SessionInfo;
import com.autoask.entity.info.CardTypeStatistic;
import com.autoask.entity.mysql.*;
import com.autoask.mapper.CardMapper;
import com.autoask.mapper.CardTypeMapper;
import com.autoask.mapper.GoodsCardTypeMapper;
import com.autoask.mapper.OrderGoodsCardMapper;
import com.autoask.service.card.CardService;
import com.ibm.icu.text.SimpleDateFormat;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author hyy
 * @create 16/11/19 07:51
 */
@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private CardTypeMapper cardTypeMapper;

    @Autowired
    private GoodsCardTypeMapper goodsCardTypeMapper;

    @Autowired
    private OrderGoodsCardMapper orderGoodsCardMapper;

    @Override
    public void saveCards(CardType cardType) throws ApiException {
        SessionInfo session = LoginUtil.getSessionInfo();
        String cardTypeId = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + CodeGenerator.getStringRandom(4);

        //1. 保存卡片类别信息
        cardType.setCardTypeId(cardTypeId);
        cardType.setCreatorId(session.getLoginId());
        cardType.setStatus(Constants.CardTypeStatus.TO_CHECK);
        cardType.setCreateTime(DateUtil.getDate());
        cardType.setDeleteFlag(false);
        cardTypeMapper.insert(cardType);

        //2. 保存卡片关联的商品信息
        for (GoodsCardType item : cardType.getGoodsCardTypeList()) {
            item.setCardTypeId(cardTypeId);
        }
        goodsCardTypeMapper.insertList(cardType.getGoodsCardTypeList());

        //3. 生成卡片并保存卡片信息
        Long count = cardType.getNum();
        List<Card> cardList = new ArrayList<>();
        for (long index = 1; index <= count; index++) {
            Card card = new Card();
            card.setCardTypeId(cardTypeId);
            card.setTypeSort(index);
            card.setCardId(CodeGenerator.generateCardId());
            card.setStatus(Constants.CardStatus.TO_CHECK);
            card.setDeleteFlag(false);
            cardList.add(card);
        }
        cardMapper.insertList(cardList);
    }

    @Override
    public CardType viewCardType(String cardTypeId) throws ApiException {
        CardType cardType = cardTypeMapper.getCardTypeDetail(cardTypeId);
        if (null == cardType) {
            throw new ApiException("卡不存在");
        }
        List<Map<String, Object>> useNumList = cardTypeMapper.getUseNumList(Arrays.asList(cardType.getCardTypeId()));
        if (CollectionUtils.isEmpty(useNumList)) {
            cardType.setUseNum(0);
        } else {
            Map<String, Object> useNumMap = useNumList.get(0);
            Long useNum = (Long) useNumMap.get("useNum");
            cardType.setUseNum(useNum.intValue());
        }
        return cardType;
    }

    @Override
    public ListSlice carTypeUseList(String cardTypeId, Integer start, Integer limit) throws ApiException {
        Long totalNum = cardTypeMapper.countCardTypeStatistic(cardTypeId);
        List<CardTypeStatistic> cardTypeStatisticList = cardTypeMapper.getCardTypeStatisticList(cardTypeId, start, limit);
        return new ListSlice(cardTypeStatisticList, totalNum);
    }

    @Override
    public void updateCardTypeActive(String cardTypeId) throws ApiException {
        Example cardTypeExp = new Example(CardType.class);
        cardTypeExp.createCriteria().andEqualTo("cardTypeId", cardTypeId).andGreaterThan("expireTime", DateUtil.getDate())
                .andEqualTo("deleteFlag", false).andEqualTo("status", Constants.CardTypeStatus.TO_CHECK);
        CardType typeEntity = new CardType();
        typeEntity.setModifyTime(DateUtil.getDate());
        typeEntity.setModifyId(LoginUtil.getSessionInfo().getLoginId());
        typeEntity.setStatus(Constants.CardTypeStatus.CHECKED);
        int num = cardTypeMapper.updateByExampleSelective(typeEntity, cardTypeExp);
        if (num == 0) {
            throw new ApiException("已经失效或者已经激活");
        }
        Example cardExp = new Example(Card.class);
        cardExp.createCriteria().andEqualTo("cardTypeId", cardTypeId);
        Card cardEntity = new Card();
        cardEntity.setStatus(Constants.CardStatus.CHECKED);
        cardEntity.setCheckTime(DateUtil.getDate());
        cardEntity.setCheckId(LoginUtil.getSessionInfo().getLoginId());
        cardMapper.updateByExampleSelective(cardEntity, cardExp);
    }

    @Override
    public void deleteCardType(String cardTypeId) throws ApiException {
        Example typeExp = new Example(CardType.class);
        typeExp.createCriteria().andEqualTo("cardTypeId", cardTypeId).andGreaterThan("expireTime", DateUtil.getDate())
                .andEqualTo("deleteFlag", false);
        CardType typeEntity = new CardType();
        typeEntity.setDeleteFlag(true);
        typeEntity.setModifyTime(DateUtil.getDate());
        typeEntity.setModifyId(LoginUtil.getSessionInfo().getLoginId());
        int num = cardTypeMapper.updateByExampleSelective(typeEntity, typeExp);
        if (num == 0) {
            throw new ApiException("卡已经删除或不存在");
        }

        Example cardExp = new Example(Card.class);
        cardExp.createCriteria().andEqualTo("cardTypeId", cardTypeId);
        Card cardEntity = new Card();
        cardEntity.setDeleteFlag(true);
        cardMapper.updateByExampleSelective(cardEntity, cardExp);
    }

    @Override
    public ListSlice getCardTypeList(String name, String productCategoryId, String productId, String goodsId, int start, int limit) throws ApiException {
        Long totalNum = cardTypeMapper.countCardTypeNum(name, productCategoryId, productId, goodsId);
        List<String> cardTypeIds = cardTypeMapper.getGoodsTypeIds(name, productCategoryId, productId, goodsId, start, limit);
        List<CardType> cardTypeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(cardTypeIds)) {
            cardTypeList = cardTypeMapper.getGoodsTypeList(cardTypeIds);
            List<Map<String, Object>> useNumList = cardTypeMapper.getUseNumList(cardTypeIds);
            HashMap<String, Integer> countMap = new HashMap<>(useNumList.size());
            for (Map<String, Object> item : useNumList) {
                String cardTypeId = (String) item.get("cardTypeId");
                Long useNum = (Long) item.get("useNum");
                countMap.put(cardTypeId, useNum.intValue());
            }
            for (CardType cardType : cardTypeList) {
                Integer num = countMap.get(cardType.getCardTypeId()) == null ? 0 : countMap.get(cardType.getCardTypeId());
                cardType.setUseNum(num);
                List<GoodsCardType> goodsCardTypeList = cardType.getGoodsCardTypeList();
                if (CollectionUtils.isNotEmpty(goodsCardTypeList)) {
                    String bindGoodsNames = goodsCardTypeList.get(0).getGoods().getName();
                    if (goodsCardTypeList.size() > 1) {
                        bindGoodsNames = bindGoodsNames + " 等";
                    }
                    cardType.setBindGoodsNames(bindGoodsNames);
                }
            }
        }
        return new ListSlice(cardTypeList, totalNum);
    }

    @Override
    public void updateCardUsed(List<OrderInfo> orderInfoList) throws ApiException {
        List<String> orderGoodsIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderInfoList)) {
            for (OrderInfo orderInfo : orderInfoList) {
                List<OrderGoods> orderGoodsList = orderInfo.getOrderGoodsList();
                if (CollectionUtils.isNotEmpty(orderGoodsList)) {
                    for (OrderGoods orderGoods : orderGoodsList) {
                        orderGoodsIdList.add(orderGoods.getOrderGoodsId());
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(orderGoodsIdList)) {
            List<String> cardIdList = orderGoodsCardMapper.getCardIdListByOrderGoodsIdList(orderGoodsIdList);
            if (CollectionUtils.isNotEmpty(cardIdList)) {
                //卡的状态是to_use  当收到支付回调的时候更行状态为used
                cardMapper.updateCardUsed(cardIdList, Constants.CardStatus.USED);
            }
        }
    }

    @Override
    public void updateCardRestByOrderId(OrderInfo orderInfo) throws ApiException {
        if (null == orderInfo) {
            throw new ApiException("订单不存在");
        }
        if (CollectionUtils.isEmpty(orderInfo.getOrderGoodsList())) {
            throw new ApiException("订单状态非法");
        }
        ArrayList<String> orderGoodsIdList = new ArrayList<>(orderInfo.getOrderGoodsList().size());
        for (OrderGoods orderGoods : orderInfo.getOrderGoodsList()) {
            orderGoodsIdList.add(orderGoods.getOrderGoodsId());
        }
        List<String> cardIdList = orderGoodsCardMapper.getCardIdListByOrderGoodsIdList(orderGoodsIdList);
        if (CollectionUtils.isNotEmpty(cardIdList)) {
            cardMapper.updateCardRest(cardIdList);
        }
    }


    @Override
    public List<Map<String, Object>> getCardSnapshotIdList(String cardId) throws ApiException {
        Card card = cardMapper.getCardDetail(cardId);
        if (null == card) {
            throw new ApiException("卡不存在");
        }
        Card.canUse(card);
        CardType cardType = card.getCardType();
        List<GoodsCardType> goodsCardTypeList = cardType.getGoodsCardTypeList();
        if (CollectionUtils.isNotEmpty(goodsCardTypeList)) {
            ArrayList<Map<String, Object>> snapshotIdList = new ArrayList<>(goodsCardTypeList.size());
            for (GoodsCardType goodsCardType : goodsCardTypeList) {
                String goodsSnapshotId = goodsCardType.getGoods().getGoodsSnapshotId();
                BigDecimal price = goodsCardType.getGoods().getOnlinePrice();
                HashMap<String, Object> itemObj = new HashMap<>();
                itemObj.put("goodsSnapshotId", goodsSnapshotId);
                itemObj.put("serial", cardId);
                itemObj.put("goodsPrice", price);
                snapshotIdList.add(itemObj);
            }
            return snapshotIdList;
        } else {
            throw new ApiException("卡已失效无法兑换");
        }
    }

    @Override
    public List<String> exportCardType(String cardTypeId) throws ApiException {
        String loginType = LoginUtil.getLoginType();
        if (!StringUtils.equals(loginType, Constants.LoginType.STAFF)) {
            throw new ApiException("权限非法");
        }
        return cardMapper.getCardIdsByType(cardTypeId);
    }

    @Override
    public ListSlice<Card> listCardByType(String cardTypeId, List<String> statusList, String cardId, Integer typeSort, Integer start, Integer limit) throws ApiException {
        Long totalNum = cardMapper.countCardNumByType(cardTypeId, statusList, cardId, typeSort);
        List<Card> cardList = cardMapper.listCardByType(cardTypeId, statusList, cardId, typeSort, start, limit);
        return new ListSlice<>(cardList, totalNum);
    }

    @Override
    public void updateCardChecked(String cardId) throws ApiException {
        Card example = new Card();
        example.setCardId(cardId);
        Card card = cardMapper.selectOne(example);
        if (null == card) {
            throw new ApiException("卡号不存在");
        }
        if (card.getDeleteFlag()) {
            throw new ApiException("卡已经被删除");
        }
        if (!StringUtils.equals(Constants.CardStatus.TO_CHECK, card.getStatus())) {
            throw new ApiException("卡已经激活");
        }

        Card update = new Card();
        update.setId(card.getId());
        update.setCheckTime(DateUtil.getDate());
        update.setCheckId(LoginUtil.getLoginId());
        update.setStatus(Constants.CardStatus.CHECKED);
        cardMapper.updateByPrimaryKeySelective(update);
    }

    @Override
    public void updateCardCheckedRange(String cardTypeId, Integer start, Integer end) throws ApiException {
        if (null == start || null == end) {
            throw new ApiException("参数非法");
        }
        Example example = new Example(Card.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cardTypeId", cardTypeId).
                andLessThanOrEqualTo("typeSort", end).andGreaterThanOrEqualTo("typeSort", start).andEqualTo("deleteFlag", false).
                andEqualTo("status", Constants.CardStatus.TO_CHECK);
        int matchNum = cardMapper.selectCountByExample(example);
        if (matchNum != (end - start + 1)) {
            throw new ApiException("范围内存在非法的卡状态，请仔细检查");
        }
        Card update = new Card();
        update.setCheckTime(DateUtil.getDate());
        update.setCheckId(LoginUtil.getLoginId());
        update.setStatus(Constants.CardStatus.CHECKED);
        cardMapper.updateByExampleSelective(update, example);
    }

    @Override
    public void updateCardDeleted(String cardId) throws ApiException {
        Card example = new Card();
        example.setCardId(cardId);
        Card card = cardMapper.selectOne(example);
        if (null == card) {
            throw new ApiException("卡号不存在");
        }
        if (card.getDeleteFlag()) {
            throw new ApiException("卡已经被删除");
        }
        if (Constants.CardStatus.NO_DEL_LIST.contains(card.getStatus())) {
            throw new ApiException("卡正在被使用，不能删除");
        }

        Card update = new Card();
        update.setDeleteFlag(true);
        update.setDeleteId(LoginUtil.getLoginId());
        update.setDeleteTime(DateUtil.getDate());
        cardMapper.updateByExampleSelective(update, example);
    }
    
	@Override
	public List<Card> getBackCarIdList() throws ApiException {
		 try {
			 return cardMapper.getBackCarIdList();
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
	}

	@Override
	public void updateCardToUseAndStatus(String cardTypeId, String status) throws ApiException {
		 try {
			 if(StringUtils.isEmpty(cardTypeId)){
				 throw new ApiException("id参数错误");
			 }
			 if(StringUtils.isEmpty(status)){
				 throw new ApiException("状态错误");
			 }
			 cardMapper.updateCardToUseAndStatus(cardTypeId, status);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
	}

	@Override
	public Map<String, String> getCarIdList(String orderId) throws ApiException {
		try {
			if(StringUtils.isEmpty(orderId)){
				 throw new ApiException("orderId参数错误");
			 }
			 return cardMapper.getCarIdList(orderId);
       } catch (Exception e) {
           throw new ApiException(e.getMessage());
       }
	}
}
