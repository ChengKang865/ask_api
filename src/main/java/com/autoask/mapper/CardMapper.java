package com.autoask.mapper;

import com.autoask.entity.mysql.Card;
import com.autoask.mapper.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CardMapper extends MyMapper<Card> {

    List<Card> getCardDetailList(@Param("cardIds") List<String> cardIds);

    //用户前台调用
    Long updateCardToUse(@Param("cardId") String cardId,@Param("orderGoodsId")String orderGoodsId, @Param("status") String status);

    Long updateCardUsed(@Param("cardIds") List<String> cardIds, @Param("status") String status);

    Long updateCardRest(@Param("cardIds") List<String> cardIds);

    Card getCardDetail(@Param("cardId") String cardId);

    List<String> getCardIdsByType(@Param("cardTypeId") String cardTypeId);

    //计算卡类型下符合条件的卡片的数量
    Long countCardNumByType(@Param("cardTypeId") String cardTypeId, @Param("statusList") List<String> statusList,@Param("cardId") String cardId, @Param("typeSort") Integer typeSort);

    List<Card> listCardByType(@Param("cardTypeId") String cardTypeId, @Param("statusList") List<String> statusList, @Param("cardId") String cardId, @Param("typeSort") Integer typeSort, @Param("start") Integer start, @Param("limit") Integer limit);
    
    List<Card> getBackCarIdList();
    
    Map<String, String> getCarIdList(String orderId);
    
    Long updateCardToUseAndStatus(@Param("cardId") String cardId, @Param("status") String status);
}