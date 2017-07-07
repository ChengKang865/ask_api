package com.autoask.service.qr;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.qr.QRCode;

/**
 * @author hyy
 * @create 2016-12-19 13:47
 */
public interface QRCodeService {

    void bindQRCode(String code, String merchantType, String merchantId) throws ApiException;

    QRCode getQRCode(String merchantType, String merchantId) throws ApiException;

    String getQRCodeRedirect(String code);
}
