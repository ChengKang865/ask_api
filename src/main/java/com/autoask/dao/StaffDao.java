package com.autoask.dao;

import com.autoask.entity.mongo.Staff;

/**
 * @author hyy
 * @create 2016-10-08 10:12
 */
public interface StaffDao extends BaseDao<Staff,String> {

    Staff getStaffByPhone(String phone);

}
