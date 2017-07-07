package com.autoask.controller.card;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mysql.Card;
import com.autoask.entity.mysql.CardType;
import com.autoask.service.card.CardService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hyy on 2016/12/4.
 */
@Controller
@RequestMapping("merchant/card/")
public class CardController {

    private static final Logger LOG = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private CardService cardService;

    /**
     * 添加卡片信息
     *
     * @param cardType
     * @return
     */
    @RequestMapping(value = "add/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo addCardType(@RequestBody @Valid CardType cardType) {
        try {
            if (CollectionUtils.isEmpty(cardType.getGoodsCardTypeList())) {
                throw new ApiException("商品信息不能为空");
            }

            cardService.saveCards(cardType);

            return ResponseDo.buildSuccess("添加成功");
        } catch (ApiException e) {
            LOG.error("Save cards failure", e);
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo listCardType(@RequestParam(value = "productCategoryId", required = false) String productCategoryId,
                                   @RequestParam(value = "productId", required = false) String productId,
                                   @RequestParam(value = "goodsId", required = false) String goodsId,
                                   @RequestParam(value = "name", required = false) String name,
                                   @RequestParam("page") int page, @RequestParam("count") int count) {
        try {
            int start = CommonUtil.pageToSkipStart(page, count);
            int limit = CommonUtil.cleanCount(count);
            ListSlice cardTypeList = cardService.getCardTypeList(name, productCategoryId, productId, goodsId, start, limit);
            return ResponseDo.buildSuccess(cardTypeList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }


    @RequestMapping(value = "view/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo viewCardType(@RequestParam("cardTypeId") String cardTypeId) {

        try {
            CardType cardType = cardService.viewCardType(cardTypeId);
            return ResponseDo.buildSuccess(cardType);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "use/list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo cardTypeUseList(@RequestParam("cardTypeId") String cardTypeId,
                                      @RequestParam("page") Integer page, @RequestParam("count") Integer count) {

        try {
            Integer start = CommonUtil.pageToSkipStart(page, count);
            Integer limit = CommonUtil.cleanCount(count);
            ListSlice useRecordList = cardService.carTypeUseList(cardTypeId, start, limit);
            return ResponseDo.buildSuccess(useRecordList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "active/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo activeCard(@RequestParam("cardTypeId") String cardTypeId) {

        try {
            cardService.updateCardTypeActive(cardTypeId);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "list/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo deleteCard(@RequestParam("cardTypeId") String cardTypeId) {
        try {
            cardService.deleteCardType(cardTypeId);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildSuccess(e.getMessage());
        }
    }

    @RequestMapping(value = "export/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo exportCardType(@RequestParam("cardTypeId") String cardTypeId) {

        try {
            List<String> cardIdList = cardService.exportCardType(cardTypeId);
            return ResponseDo.buildSuccess(cardIdList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "item/list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo listCardByType(@RequestParam("cardTypeId") String cardTypeId, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "cardId",required = false) String cardId, @RequestParam(value = "typeSort",required = false) Integer typeSort, @RequestParam("page") int page, @RequestParam("count") int count) {

        try {
            Integer start = CommonUtil.pageToSkipStart(page, count);
            Integer limit = CommonUtil.cleanCount(count);
            List<String> statusList = null;
            if (StringUtils.isNotEmpty(status)) {
                statusList = Arrays.asList(status);
            }
            ListSlice<Card> cardList = cardService.listCardByType(cardTypeId, statusList,cardId,typeSort, start, limit);
            return ResponseDo.buildSuccess(cardList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "item/active/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo activeCardItem(@RequestParam("cardId") String cardId) {
        try {
            cardService.updateCardChecked(cardId);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "item/active-list/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo activeCardItemList(@RequestParam("cardTypeId") String cardTypeId, @RequestParam("start") Integer start, @RequestParam("end") Integer end) {
        try {
            cardService.updateCardCheckedRange(cardTypeId, start, end);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "item/delete/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo deleteCardItem(@RequestParam("cardId") String cardId) {
        try {
            cardService.updateCardDeleted(cardId);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
