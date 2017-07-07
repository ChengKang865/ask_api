package com.autoask.controller.staff;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CommonUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.controller.common.BaseController;
import com.autoask.entity.mongo.Staff;
import com.autoask.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by hyy on 2016/12/19.
 */
@Controller
@RequestMapping(value = "/staff/")
public class StaffController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(StaffController.class);

    @Autowired
    private StaffService staffService;


    @RequestMapping(value = "get/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo getStaffById(@RequestParam(value = "id") String id) {
        Staff staff = staffService.getById(id);
        return ResponseDo.buildSuccess(staff);
    }

    @RequestMapping(value = "list/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo staffList(@RequestParam(value = "page") Integer page,
                                @RequestParam(value = "count") Integer count,
                                @RequestParam(value = "role", required = false) String role,
                                @RequestParam(value = "phone", required = false) String phone,
                                @RequestParam(value = "name", required = false) String name) {
        try {
            Integer start = CommonUtil.pageToSkipStart(page, count);
            Integer limit = CommonUtil.cleanCount(count);
            ListSlice<Staff> staffList = staffService.getStaffList(role, phone, name, start, limit);
            return ResponseDo.buildSuccess(staffList);
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "add/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo addStaff(@RequestBody @Valid Staff staff) {
        try {
            staffService.saveStaff(staff);
            return ResponseDo.buildSuccess("");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "update/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo updateStaff(@RequestBody @Valid Staff staff) {

        try {
            staffService.updateStaff(staff);
            return ResponseDo.buildSuccess("");
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    @RequestMapping(value = "delete/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo deleteStaff(@RequestParam(value = "id") String id) {
        try {
            staffService.deleteStaffById(id);
            return ResponseDo.buildSuccess("");
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
