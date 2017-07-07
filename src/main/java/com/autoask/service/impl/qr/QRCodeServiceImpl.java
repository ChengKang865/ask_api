package com.autoask.service.impl.qr;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.qr.QRCodeDao;
import com.autoask.entity.mongo.merchant.BaseMerchant;
import com.autoask.entity.mongo.qr.QRCode;
import com.autoask.service.merchant.MerchantService;
import com.autoask.service.qr.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @author hyy
 * @create 2016-12-19 13:47
 */
@Service
public class QRCodeServiceImpl implements QRCodeService {

    @Autowired
    private QRCodeDao qrCodeDao;

    @Autowired
    private MerchantService merchantService;

    @Override
    public QRCode getQRCode(String merchantType, String merchantId) throws ApiException {
        return qrCodeDao.findOne(Query.query(Criteria.where("merchantType").is(merchantType).and("merchantId").is(merchantId)));
    }

    @Override
    public void bindQRCode(String code, String merchantType, String merchantId) throws ApiException {
        BaseMerchant merchant = merchantService.getMerchantInfo(merchantType, merchantId);
        if (null == merchant) {
            throw new ApiException("商户不存在");
        }
        QRCode preCode = qrCodeDao.findOne(Query.query(Criteria.where("code").is(code).and("deleteFlag").is(false)));
        if (null != preCode) {
            throw new ApiException("该二维码已经被绑定");
        }
        QRCode preMerchantQrCode = qrCodeDao.findOne(Query.query(Criteria.where("merchantType").is(merchantType).and("merchantId").is(merchantId).and("deleteFlag").is(false)));
        if (null != preMerchantQrCode) {
            QRCode updateQRCode = new QRCode();
            updateQRCode.setCode(code);
            updateQRCode.setModifyTime(DateUtil.getDate());
            updateQRCode.setModifyType(LoginUtil.getLoginType());
            updateQRCode.setModifyId(LoginUtil.getLoginId());

            qrCodeDao.updateSelective(preMerchantQrCode.getId(), updateQRCode);
        } else {
            //新增的
            QRCode qrCode = new QRCode();
            qrCode.setCode(code);
            qrCode.setMerchantType(merchantType);
            qrCode.setMerchantId(merchantId);
            qrCode.setCreateTime(DateUtil.getDate());
            qrCode.setCreatorType(LoginUtil.getLoginType());
            qrCode.setCreatorId(LoginUtil.getLoginId());
            qrCode.setDeleteFlag(false);

            qrCodeDao.save(qrCode);
        }


    }

    @Override
    public String getQRCodeRedirect(String code) {
        QRCode qrCode = qrCodeDao.findOne(Query.query(Criteria.where("deleteFlag").is(false).and("code").is(code)));
        String baseUrl = Constants.M_INDEX_URL;
        if (null == qrCode) {
            return baseUrl;
        }
        String merchantId = qrCode.getMerchantId();
        String merchantType = qrCode.getMerchantType();
        switch (merchantType) {
            case Constants.MerchantType.FACTORY:
                return baseUrl + "?factoryId=" + merchantId;
            case Constants.MerchantType.SERVICE_PROVIDER:
                return baseUrl + "?serviceProviderId" + merchantId;
            case Constants.MerchantType.MECHANIC:
                return baseUrl + "?mechanicId=" + merchantId;
            case Constants.MerchantType.OUTLETS:
                return baseUrl + "?outletsId=" + merchantId;
        }
        return baseUrl;
    }
}
