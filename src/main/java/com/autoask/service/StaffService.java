package com.autoask.service;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mongo.Staff;

import java.util.List;

/**
 * @author hyy
 * @create 2016-10-08 10:09
 */
public interface StaffService {

    Staff getById(String id);

    Staff getStaffByPhone(String phone);

    void saveStaff(Staff staff) throws ApiException;

    void updateStaff(Staff staff) throws ApiException;

    void deleteStaffById(String id) throws ApiException;

    void updateStaffPassword(String id, String password) throws ApiException;

    ListSlice<Staff> getStaffList(String role, String phone, String name, Integer start, Integer limit) throws ApiException;
}
