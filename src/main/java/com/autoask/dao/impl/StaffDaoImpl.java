package com.autoask.dao.impl;

import com.autoask.dao.StaffDao;
import com.autoask.entity.mongo.Staff;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 * @create 2016-10-08 10:12
 */
@Repository("staffDao")
public class StaffDaoImpl extends BaseDaoImpl<Staff, String> implements StaffDao {

    @Override
    public Staff getStaffByPhone(String phone) {
        return this.findOne(Query.query(Criteria.where("phone").is(phone).and("deleteFlag").is(false)));
    }
}
