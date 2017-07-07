package com.autoask.entity.mongo.merchant;

import com.autoask.entity.common.Address;
import com.autoask.entity.common.Landmark;
import com.autoask.entity.mongo.invoice.VatInvoice;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;

/**
 * Created by nate_chen on 16-7-26.
 * 修理厂
 */
@Document
public class ServiceProvider extends BaseMerchant {

    private String contact;

    private Address address;

    @GeoSpatialIndexed(useGeneratedName = true, type = GeoSpatialIndexType.GEO_2D)
    private Landmark landmark;

    private String call;

    private String startTime;

    private String endTime;

    private String serviceContent;

    private String logoUrl;

    private List<String> picUrlList;

    private VatInvoice vatInvoice;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Landmark getLandmark() {
        return landmark;
    }

    public void setLandmark(Landmark landmark) {
        this.landmark = landmark;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getServiceContent() {
        return serviceContent;
    }

    public void setServiceContent(String serviceContent) {
        this.serviceContent = serviceContent;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public List<String> getPicUrlList() {
        return picUrlList;
    }

    public void setPicUrlList(List<String> picUrlList) {
        this.picUrlList = picUrlList;
    }

    public VatInvoice getVatInvoice() {
        return vatInvoice;
    }

    public void setVatInvoice(VatInvoice vatInvoice) {
        this.vatInvoice = vatInvoice;
    }
}
