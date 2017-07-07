package com.autoask.service.card;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mysql.Card;
import com.autoask.entity.mysql.CardType;
import com.autoask.entity.mysql.OrderInfo;

import java.util.List;
import java.util.Map;

/**
 * @author hyy
 * @create 16/11/19 07:51
 */
public interface CardService {

    /**
     * @throws ApiException
     */
    void saveCards(CardType cardType) throws ApiException;

    CardType viewCardType(String cardTypeId) throws ApiException;

    ListSlice carTypeUseList(String cardTypeId, Integer start, Integer limit) throws ApiException;

    void updateCardTypeActive(String cardTypeId) throws ApiException;

    void deleteCardType(String cardTypeId) throws ApiException;

    ListSlice getCardTypeList(String name, String productCategoryId, String productId, String goodsId, int start, int limit) throws ApiException;

    void updateCardUsed(List<OrderInfo> orderInfoList) throws ApiException;

    //退款等回滚卡的状态
    void updateCardRestByOrderId(OrderInfo orderInfo) throws ApiException;

    //用户前台用户 返回 卡对应的各个商品的goods_snapshot_id
    List<Map<String, Object>> getCardSnapshotIdList(String cardId) throws ApiException;

    List<String> exportCardType(String cardTypeId) throws ApiException;

    //
    ListSlice<Card> listCardByType(String cardTypeId, List<String> statusList,String cardId,Integer typeSort,Integer start,Integer limit) throws ApiException;

    void updateCardChecked(String cardId) throws ApiException;

    void updateCardCheckedRange(String cardTypeId, Integer start, Integer end) throws ApiException;

    void updateCardDeleted(String cardId) throws ApiException;
    
    List<Card> getBackCarIdList() throws ApiException;
    
    Map<String, String> getCarIdList(String orderId) throws ApiException;
    
    void updateCardToUseAndStatus(String cardTypeId, String status) throws ApiException;
}
