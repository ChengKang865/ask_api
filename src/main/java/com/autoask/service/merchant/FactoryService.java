package com.autoask.service.merchant;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mongo.merchant.Factory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public interface FactoryService {

    Factory findById(String id) throws ApiException;

    void create(Factory factory) throws ApiException;

    void updateSelective(String factoryId, Factory factory) throws ApiException;

    void deleteById(String id);

    Factory getFactoryByPhone(String phone);

    ListSlice<Factory> getFactoryList(String phone, String name, Integer start, Integer length);

}
