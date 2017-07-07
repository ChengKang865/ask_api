package com.autoask.entity.mongo.merchant;

import com.autoask.entity.common.Address;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author hyy
 * @craete 2016/7/28 20:16
 */
@Document
public class Outlets extends BaseMerchant {
    /**
     * 地址
     */
    private Address address;

    /**
     * 联系人姓名
     */
    private String contact;

    private String call;

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

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }
}
