package com.autoask.entity.mongo.invoice;

/**
 * @author hyy
 * @create 16/11/19 19:44
 */
public class VatInvoice {

    private String company;

    private String taxSerial;

    private String address;

    private String phone;

    private String bankName;

    private String account;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTaxSerial() {
        return taxSerial;
    }

    public void setTaxSerial(String taxSerial) {
        this.taxSerial = taxSerial;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
