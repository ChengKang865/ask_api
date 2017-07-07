package com.autoask.entity.mongo.merchant;

import com.autoask.entity.common.Address;
import com.autoask.entity.common.Landmark;
import com.autoask.entity.mongo.invoice.VatInvoice;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author hyy
 * @craete 2016/7/28 19:56
 */
@Document
public class Factory extends BaseMerchant {

    // 地理位置
    private Address address;

    // 经纬度
    private Landmark landmark;

    //联系人姓名
    private String contact;

    private String logoUrl;

    private String call;

    private VatInvoice vatInvoice;


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Landmark getLandmark() {
        return landmark;
    }

    public void setLandmark(Landmark landmark) {
        this.landmark = landmark;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public VatInvoice getVatInvoice() {
        return vatInvoice;
    }

    public void setVatInvoice(VatInvoice vatInvoice) {
        this.vatInvoice = vatInvoice;
    }
}
