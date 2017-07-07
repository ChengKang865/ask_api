package com.autoask.service.impl.merchant;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.LoginUtil;
import com.autoask.entity.mongo.merchant.*;
import com.autoask.entity.mysql.MerchantShareApply;
import com.autoask.mapper.MerchantShareApplyMapper;
import com.autoask.service.merchant.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author hyy
 * @create 16/11/6 15:48
 */
@Service
public class MerchantShareApplyServiceImpl implements MerchantShareApplyService {

    @Autowired
    private MerchantShareApplyMapper merchantShareApplyMapper;

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private OutletsService outletsService;

    @Autowired
    private FactoryService factoryService;

    @Override
    public ListSlice selectMerchantApplies(String merchantType, String merchantId, String status, String startTime, String endTime, int start, int limit) throws ApiException {

        //参数检查
        if (StringUtils.isNotEmpty(status)) {
            if (!Constants.MerchantShareApplyStatus.TYPE_LIST.contains(status)) {
                throw new ApiException("status 参数非法");
            }
        }
        //除了autoask之外的其他人只能看到自己的申请记录
        String loginType = LoginUtil.getLoginType();
        if (!StringUtils.equals(loginType, Constants.LoginType.STAFF)) {
            merchantType = LoginUtil.getLoginType();
            merchantId = LoginUtil.getLoginId();
        }

        Long totalNum = merchantShareApplyMapper.countApplyHistoryNum(merchantType, merchantId, status, startTime, endTime);
        List<MerchantShareApply> shareApplies = merchantShareApplyMapper.selectApplyHistory(merchantType, merchantId, status, startTime, endTime, start, limit);
        if (CollectionUtils.isNotEmpty(shareApplies)) {
            for (MerchantShareApply merchantShareApply : shareApplies) {
                String mType = merchantShareApply.getMerchantType();
                String mId = merchantShareApply.getMerchantId();
                if (StringUtils.equals(mType, Constants.MerchantType.MECHANIC)) {
                    Mechanic mechanic = mechanicService.findById(mId);
                    merchantShareApply.setMerchantName(mechanic.getName());
                } else if (StringUtils.equals(mType, Constants.MerchantType.SERVICE_PROVIDER)) {
                    ServiceProvider serviceProvider = serviceProviderService.findById(mId);
                    merchantShareApply.setMerchantName(serviceProvider.getName());
                } else if (StringUtils.equals(mType, Constants.MerchantType.OUTLETS)) {
                    Outlets outlets = outletsService.findById(mId);
                    merchantShareApply.setMerchantName(outlets.getName());
                } else if (StringUtils.equals(mType, Constants.MerchantType.FACTORY)) {
                    Factory factory = factoryService.findById(mId);
                    merchantShareApply.setMerchantName(factory.getName());
                }
            }
        }
        return new ListSlice(shareApplies, totalNum);
    }
}
