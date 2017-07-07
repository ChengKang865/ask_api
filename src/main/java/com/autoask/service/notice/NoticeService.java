package com.autoask.service.notice;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.common.Address;
import com.autoask.entity.mongo.notice.Notice;
import com.autoask.entity.mongo.notice.NoticeItem;

import java.util.Date;
import java.util.List;

/**
 * Created by hyy on 2016/12/20.
 */
public interface NoticeService {

    void insertNotice(Notice notice) throws ApiException;

    ListSlice<Notice> getNoticeList(String name, String merchantType, Date startTime, Date endTime, Integer start, Integer limit) throws ApiException;

    Notice getNoticeById(String id);

    NoticeItem getNoticeItemById(String id) throws ApiException;

    ListSlice<NoticeItem> getNoticeItemList(Date startTime, Date endTime, String content, Boolean readFlag, Integer start, Integer limit) throws ApiException;
}