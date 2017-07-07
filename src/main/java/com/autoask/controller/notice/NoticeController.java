package com.autoask.controller.notice;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.controller.common.BaseController;
import com.autoask.entity.mongo.notice.Notice;
import com.autoask.entity.mongo.notice.NoticeItem;
import com.autoask.service.notice.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * Created by hyy on 2016/12/20.
 */
@Controller
@RequestMapping(value = "/merchant/notice/")
public class NoticeController {

    private static final Logger LOG = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private NoticeService noticeService;


    @RequestMapping(value = "view/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo viewNotice(@RequestParam("id") String id) {
        Notice notice = noticeService.getNoticeById(id);
        return ResponseDo.buildSuccess(notice);
    }


    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo list(@RequestParam(value = "page") Integer page,
                           @RequestParam(value = "count") Integer count,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "startTime", required = false) Date startTime,
                           @RequestParam(value = "endTime", required = false) Date endTime,
                           @RequestParam(value = "merchantType", required = false) String merchantType) {

        try {
            Integer start = CommonUtil.pageToSkipStart(page, count);
            Integer limit = CommonUtil.cleanCount(count);
            ListSlice<Notice> noticeList = noticeService.getNoticeList(name, merchantType, startTime, endTime, start, limit);
            return ResponseDo.buildSuccess(noticeList);
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "insert/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo insertNotice(@RequestBody @Valid Notice notice) {
        try {
            noticeService.insertNotice(notice);
            return ResponseDo.buildSuccess("");
        } catch (ApiException e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "item/list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo listNoticeItem(@RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date starTime, @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, @RequestParam(value = "content", required = false) String content,
                                     @RequestParam(value = "readFlag", required = false) Boolean readFlag,
                                     @RequestParam("page") Integer page, @RequestParam("count") Integer count) {

        try {
            Integer start = CommonUtil.pageToSkipStart(page, count);
            Integer limit = CommonUtil.cleanCount(count);
            ListSlice<NoticeItem> noticeItemList = noticeService.getNoticeItemList(starTime, endTime, content, readFlag, start, limit);
            return ResponseDo.buildSuccess(noticeItemList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "item/get/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getNoticeItem(@RequestParam("id") String id) {

        try {
            NoticeItem noticeItem = noticeService.getNoticeItemById(id);
            return ResponseDo.buildSuccess(noticeItem);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}