//package com.autoask.controller.goodsnum;
//
//import com.autoask.common.ResponseDo;
//import com.autoask.common.exception.ApiException;
//import com.autoask.common.util.CommonUtil;
//import com.autoask.common.util.ListSlice;
//import com.autoask.controller.common.BaseController;
//import com.autoask.entity.mongo.store.GoodsNumRecord;
//import com.autoask.service.merchant.ServiceProviderService;
//import com.autoask.service.product.GoodsNumRecordService;
//import com.autoask.service.product.GoodsNumService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.Date;
//
///**
// * Created by hyy on 2016/12/3.
// */
//@Controller
//@RequestMapping("/merchant/goodsNum/")
//public class GoodsNumController extends BaseController {
//
//    private static final Logger LOG = LoggerFactory.getLogger(GoodsNumController.class);
//
//    @Autowired
//    private GoodsNumService goodsNumService;
//
//    @Autowired
//    private GoodsNumRecordService goodsNumRecordService;
//
////    /**
////     * 添加 发货/退货 单
////     *
////     * @param goodsNumRecord
////     * @return
////     */
////    @RequestMapping(value = "/change/", method = RequestMethod.POST)
////    @ResponseBody
////    public ResponseDo changeNum(@RequestBody @Valid GoodsNumRecord goodsNumRecord) {
////        LOG.info("enter into changeGoodsNum");
////        try {
////            goodsNumService.updateGoodsNum(goodsNumRecord);
////            return ResponseDo.buildSuccess("");
////        } catch (ApiException e) {
////            LOG.error(e.getMessage());
////            return ResponseDo.buildError(e.getMessage());
////        }
////    }
//
////    @RequestMapping(value = "/record/get/", method = RequestMethod.GET)
////    @ResponseBody
////    public ResponseDo getGoodsNumRecord(@RequestParam("id") String id) {
////        try {
////            GoodsNumRecord goodsNumRecord = goodsNumRecordService.getById(id);
////            return ResponseDo.buildSuccess(goodsNumRecord);
////        } catch (ApiException e) {
////            LOG.error(e.getMessage());
////            return ResponseDo.buildError(e.getMessage());
////        }
////    }
//
//    /**
//     * 发货/退货 历史
//     *
//     * @param addType
//     * @param merchantType
//     * @param merchantId
//     * @param startTime
//     * @param endTime
//     * @param page
//     * @param count
//     * @return
//     */
////    @RequestMapping(value = "/change/list/", method = RequestMethod.GET)
////    @ResponseBody
////    public ResponseDo changeRecordList(@RequestParam("addType") Boolean addType,
////                                       @RequestParam(value = "merchantType", required = false) String merchantType,
////                                       @RequestParam(value = "merchantId", required = false) String merchantId,
////                                       @RequestParam(value = "productCategoryId", required = false) String productCategoryId,
////                                       @RequestParam(value = "productId", required = false) String productId,
////                                       @RequestParam(value = "goodsId", required = false) String goodsId,
////                                       @RequestParam(value = "goodsName", required = false) String goodsName,
////                                       @RequestParam(value = "serial", required = false) String serial,
////                                       @RequestParam(value = "startTime", required = false) Date startTime,
////                                       @RequestParam(value = "endTime", required = false) Date endTime,
////                                       @RequestParam("page") Integer page,
////                                       @RequestParam("count") Integer count) {
////        LOG.info("enter into changeRecordList");
////
////        try {
////            Integer limit = CommonUtil.cleanCount(count);
////            Integer start = CommonUtil.pageToSkipStart(page, count);
////            ListSlice goodsNumRecordList = goodsNumRecordService.listGoodsNumRecord(productCategoryId, productId, goodsId, goodsName,
////                    merchantType, merchantId, addType, serial, startTime, endTime, start, limit);
////            return ResponseDo.buildSuccess(goodsNumRecordList);
////        } catch (ApiException e) {
////            LOG.error(e.getMessage());
////            return ResponseDo.buildError(e.getMessage());
////        }
////    }
//
//    /**
//     * 下属商户库存记录
//     *
//     * @param merchantType
//     * @param productCategoryId
//     * @param productId
//     * @param goodsId
//     * @param page
//     * @param count
//     * @return
//     */
////    @RequestMapping("/list/")
////    @ResponseBody
////    public ResponseDo goodsNumList(@RequestParam(value = "merchantType") String merchantType,
////                                   @RequestParam(value = "factoryId", required = false) String factoryId,
////                                   @RequestParam(value = "productCategoryId", required = false) String productCategoryId,
////                                   @RequestParam(value = "productId", required = false) String productId,
////                                   @RequestParam(value = "goodsId", required = false) String goodsId,
////                                   @RequestParam(value = "goodsName", required = false) String goodsName,
////                                   @RequestParam(value = "page") Integer page,
////                                   @RequestParam(value = "count") Integer count) {
////        try {
////            Integer limit = CommonUtil.cleanCount(count);
////            Integer start = CommonUtil.pageToSkipStart(page, count);
////            ListSlice goodsNumList = goodsNumService.getSubMerchantGoodsNumList(merchantType, factoryId, productCategoryId, productId, goodsId, goodsName, start, limit);
////            return ResponseDo.buildSuccess(goodsNumList);
////        } catch (ApiException e) {
////            LOG.error(e.getMessage());
////            return ResponseDo.buildError(e.getMessage());
////        }
////    }
//}