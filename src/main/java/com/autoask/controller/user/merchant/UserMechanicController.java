package com.autoask.controller.user.merchant;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.merchant.Mechanic;
import com.autoask.service.merchant.MechanicService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @author hyy
 * @create 16/10/9 02:44
 */
@Controller
@RequestMapping("user/mechanic")
public class UserMechanicController {

    private static final Logger LOG = LoggerFactory.getLogger(UserMechanicController.class);

    @Autowired
    private MechanicService mechanicService;

    /**
     * 获取一个修理厂所有的修理工
     *
     * @param serviceProviderId
     * @return
     */
    @RequestMapping("list/")
    @ResponseBody
    public ResponseDo list(@RequestParam("serviceProviderId") String serviceProviderId) {
        try {
            List<Mechanic> mechanicList = mechanicService.getServiceProviderMechanicList(serviceProviderId);
            if (CollectionUtils.isNotEmpty(mechanicList)) {
                ArrayList<HashMap<String, String>> resultList = new ArrayList<>(mechanicList.size());
                for (Mechanic mechanic : mechanicList) {
                    if (null != mechanic.getActivated() && mechanic.getActivated()) {
                        HashMap<String, String> itemMap = new HashMap<>(2, 1);
                        itemMap.put("mechanicId", mechanic.getId());
                        itemMap.put("name", mechanic.getName());

                        resultList.add(itemMap);
                    }
                }
                return ResponseDo.buildSuccess(resultList);
            }
            throw new ApiException("该修理厂不存在修理工");
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }
}
