package com.autoask.controller.user.card;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.service.card.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hyy
 * @create 16/11/19 07:44
 */
@Controller
@RequestMapping("user/card/")
public class UserCardController {

    private static final Logger LOG = LoggerFactory.getLogger(UserCardController.class);

    @Autowired
    private CardService cardService;

    @RequestMapping(value = "info/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo cardInfo(@RequestParam("cardId") String cardId) {
        try {
            List<Map<String,Object>> cardSnapshotIdList = cardService.getCardSnapshotIdList(cardId);

            return ResponseDo.buildSuccess(cardSnapshotIdList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
