package com.autoask.entity.mongo.invoice;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author hyy
 * @create 16/11/19 19:40
 */
@Document
public class Invoice {

    @Id
    private String id;

    private String userId;

    private String type;


    private CommonInvoice commonInvoice;

    private VatInvoice vatInvoice;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CommonInvoice getCommonInvoice() {
        return commonInvoice;
    }

    public void setCommonInvoice(CommonInvoice commonInvoice) {
        this.commonInvoice = commonInvoice;
    }

    public VatInvoice getVatInvoice() {
        return vatInvoice;
    }

    public void setVatInvoice(VatInvoice vatInvoice) {
        this.vatInvoice = vatInvoice;
    }
}
