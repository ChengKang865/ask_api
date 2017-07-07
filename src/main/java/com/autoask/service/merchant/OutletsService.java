package com.autoask.service.merchant;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mongo.merchant.Outlets;

import java.util.List;

/**
 * Created by weid on 16-8-14.
 */
public interface OutletsService {

    Outlets findById(String id);

    void create(Outlets outlets) throws ApiException;

    void updateSelective(String outletId, Outlets outlets) throws ApiException;

    void deleteById(String id) throws ApiException;

    ListSlice<Outlets> getOutletsList(String phone, String name, Integer start, Integer length) throws ApiException;
}
