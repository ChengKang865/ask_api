package com.autoask.service.merchant;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.ListSlice;

import java.util.Date;
import java.util.List;

/**
 * @author hyy
 * @create 16/11/6 15:47
 */
public interface MerchantShareApplyService {

    ListSlice selectMerchantApplies(String merchantType, String merchantId, String status,
                                    String startTime, String endTime, int start, int limit) throws ApiException;
}
