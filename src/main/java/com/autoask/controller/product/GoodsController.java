package com.autoask.controller.product;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.Constants;
import com.autoask.common.util.HttpRequestUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.controller.common.BaseController;
import com.autoask.entity.mysql.Goods;
import com.autoask.entity.mysql.GoodsSnapshot;
import com.autoask.service.product.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weid on 16-8-21.
 */
@Controller
@RequestMapping("/product/goods")
public class GoodsController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/get/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getGoodsById(@RequestParam(value = "id") String goodsId,
                                   @RequestParam(value = "status") String status) {
        LOG.info("get goods by id");
        try {
            GoodsSnapshot goodsSnapshot = goodsService.getGoodsSnapshotInfoByGoodsId(goodsId, status);
            return ResponseDo.buildSuccess(goodsSnapshot);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getGoodsList(@RequestParam(value = "page") Integer page,
                                   @RequestParam(value = "count") Integer count,
                                   @RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "productCategoryId", required = false) String productCategoryId,
                                   @RequestParam(value = "productId", required = false) String productId
                                   ) {
        LOG.info("get goods list");
        int start = CommonUtil.pageToSkipStart(page, count);
        int limit = CommonUtil.cleanCount(count);
        try {
            ListSlice<Goods> goodsList = goodsService.getGoodsList(start, limit, productCategoryId, productId, name);

            return ResponseDo.buildSuccess(goodsList);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/add/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo addGoods(GoodsSnapshot goodsSnapshot) {
        LOG.info("add goods");

        try {
            goodsService.addGoodsSnapshot(goodsSnapshot);
            return ResponseDo.buildSuccess("添加商品信息成功!等待管理员确认!");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updateGoods(GoodsSnapshot goodsSnapshot) {
        LOG.info("update goods");

        try {
            goodsService.updateGoodsSnapshot(goodsSnapshot);
            return ResponseDo.buildSuccess("更新商品成功");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/delete/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo deleteGoods(@RequestParam(value = "goodsId") String goodsId) {
        LOG.info("delete goods");

        try {
            goodsService.deleteGoodsById(goodsId);
            return ResponseDo.buildSuccess("删除商品成功");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/online/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo onlineGoods(@RequestParam(value = "goodsId") String goodsId) {
        LOG.info("Online goods");

        try {
            goodsService.setGoodsSaleById(goodsId);
            return ResponseDo.buildSuccess("商品上线成功");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/offline/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo offlineGoodsById(@RequestParam(value = "goodsId") String goodsId) {
        LOG.info("offline goods");

        try {
            goodsService.setGoodsNotSaleById(goodsId);
            return ResponseDo.buildSuccess("商品下线成功");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "/confirm/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo confirmGoodsSnapshotById(@RequestParam(value = "goodsSnapshotId") String goodsSnapshotId,
                                               @RequestParam(value = "status") String confirmStatus) {
        LOG.info("confirm goods.");

        try {
            if (!Constants.GoodsConfirmType.CONFIRM_TYPE_LIST.contains(confirmStatus)) {
                throw new ApiException("审核状态不正确.");
            }
            if (StringUtils.equals(Constants.GoodsConfirmType.SUCCESS, confirmStatus)) {
                // 通过审核
                goodsService.passGoodsSnapshot(goodsSnapshotId);
            } else {
                // 审核不通过
                goodsService.refuseGoodsSnapshot(goodsSnapshotId);
            }
            return ResponseDo.buildSuccess("商品审核成功");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
