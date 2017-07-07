package com.autoask.entity.mongo.qr;

import com.autoask.entity.mongo.BaseEntity;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author hyy
 * @create 16/10/23 10:51
 */
@Document
public class QRCode extends BaseEntity {

    @Indexed
    private String code;

    private String merchantType;

    @Indexed
    private String merchantId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
