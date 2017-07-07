package com.autoask.service.impl;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.StaffDao;
import com.autoask.entity.mongo.Staff;
import com.autoask.service.StaffService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hyy
 * @create 2016-10-08 10:10
 */
@Service("staffService")
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffDao staffDao;

    @Override
    public Staff getById(String id) {
        return staffDao.findOne(Query.query(Criteria.where("id").is(id).and("deleteFlag").is(false)));
    }

    @Override
    public Staff getStaffByPhone(String phone) {
        return staffDao.getStaffByPhone(phone);
    }

    @Override
    public void saveStaff(Staff staff) throws ApiException {
        staff.setId(null);
        staff.setDeleteFlag(false);
        staff.setCreateTime(DateUtil.getDate());
        staff.setCreatorId(LoginUtil.getLoginId());
        staff.setCreatorType(LoginUtil.getLoginType());
        staffDao.save(staff);
    }

    @Override
    public void updateStaff(Staff staff) throws ApiException {
        //TODO 校验数据是否合法
        staffDao.updateSelective(staff.getId(), staff);
    }

    @Override
    public void deleteStaffById(String id) throws ApiException {
        Staff updateStaff = new Staff();
        updateStaff.setDeleteFlag(true);
        updateStaff.setDeleteId(LoginUtil.getLoginId());
        updateStaff.setDeleteType(LoginUtil.getLoginType());
        updateStaff.setDeleteTime(DateUtil.getDate());

        staffDao.updateSelective(id, updateStaff);
    }

    @Override
    public void updateStaffPassword(String id, String password) throws ApiException {
        Staff updateStaff = new Staff();
        updateStaff.setPassword(password);

        updateStaff.setModifyType(LoginUtil.getLoginType());
        updateStaff.setModifyId(LoginUtil.getLoginId());
        updateStaff.setModifyTime(DateUtil.getDate());
        staffDao.updateSelective(id, updateStaff);

    }

    @Override
    public ListSlice<Staff> getStaffList(String role, String phone, String name, Integer start, Integer limit) throws ApiException {
        Criteria criteria = new Criteria();
        criteria.and("deleteFlag").is(false);
        if (StringUtils.isNotEmpty(role)) {
            criteria.and("role").is(role);
        }
        if (StringUtils.isNotEmpty(phone)) {
            String phoneReg = ".*" + phone + ".*";
            criteria.and("phone").regex(phoneReg);
        }
        if (StringUtils.isNotEmpty(name)) {
            String nameReg = ".*" + name + ".*";
            criteria.and("name").regex(nameReg);
        }
        Query query = Query.query(criteria);
        if (null != start) {
            query.skip(start);
        }
        if (null != limit) {
            query.limit(limit);
        }
        List<Staff> staffList = staffDao.find(query);
        long totalNum = staffDao.count(Query.query(criteria));
        return new ListSlice<>(staffList, totalNum);
    }
}
