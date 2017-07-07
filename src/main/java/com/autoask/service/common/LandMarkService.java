package com.autoask.service.common;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.common.Address;
import com.autoask.entity.common.Landmark;

/**
 * Created by hp on 16-8-17.
 */
public interface LandMarkService {
    Landmark getLandMark(Address address) throws ApiException;
}
