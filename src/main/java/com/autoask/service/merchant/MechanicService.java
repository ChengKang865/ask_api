package com.autoask.service.merchant;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mongo.merchant.Mechanic;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by weid on 16-8-14.
 */
public interface MechanicService {

    void create(Mechanic mechanic) throws ApiException;

    Mechanic findById(String id);

    void updateSelective(Mechanic mechanic) throws ApiException;

    void deleteById(String id) throws ApiException;

    List<Mechanic> getMechanicByServiceProviderId(String serviceProviderId);

    ListSlice<Mechanic> getMechanicList( String serviceProviderId, String phone, String name, Integer start, Integer length) throws ApiException;

    /**
     *  用户前台
     * @param serviceProviderId
     * @return
     * @throws ApiException
     */
    List<Mechanic> getServiceProviderMechanicList(String serviceProviderId) throws ApiException;

}
