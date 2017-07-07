package com.autoask.entity.common;

import com.autoask.common.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hyy
 * @craete 2016/7/26 16:48
 */
public class Address {

    private static final Logger LOG = LoggerFactory.getLogger(Address.class);

    private String province;

    private String city;

    /**
     * 区以及同级别的县
     */
    private String region;

    /**
     * 街道以及同级别的镇
     */
    private String street;

    /**
     * 详细地址
     */
    private String detail;

    private String name;

    private String phone;

    public Address() {
    }

    public Address(String province, String city, String region, String street, String detail) {
        this.province = province;
        this.city = city;
        this.region = region;
        this.street = street;
        this.detail = detail;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String toString() {
        StringBuffer addressString = new StringBuffer();
        addressString.append(this.province);
        addressString.append(this.city);
        addressString.append(this.region);
        addressString.append(this.street);
        addressString.append(this.detail);

        return String.valueOf(addressString);
    }

    public static Address cleanAddress(Address address) throws ApiException {
        if (null == address) {
            LOG.warn("address is null");
            throw new ApiException("address is null");
        }
        if (StringUtils.isEmpty(address.getProvince())) {
            address.setProvince(null);
        }
        if (StringUtils.isEmpty(address.getCity())) {
            address.setCity(null);
        }
        if (StringUtils.isEmpty(address.getRegion())) {
            address.setRegion(null);
        }
        if (StringUtils.isEmpty(address.getStreet())) {
            address.setStreet(null);
        }
        return address;
    }

    public static Address cleanWithNull(Address address) {
        if (null == address) {
            return null;
        }
        if (StringUtils.isEmpty(address.getProvince())) {
            address.setProvince(null);
        }
        if (StringUtils.isEmpty(address.getCity())) {
            address.setCity(null);
        }
        if (StringUtils.isEmpty(address.getRegion())) {
            address.setRegion(null);
        }
        if (StringUtils.isEmpty(address.getStreet())) {
            address.setStreet(null);
        }
        return address;
    }

    public static void checkFullInfo(Address address) throws ApiException {
        if (null == address) {
            throw new ApiException("地址参数非法");
        }
        if (StringUtils.isEmpty(address.getProvince())
                || StringUtils.isEmpty(address.getCity())
                || StringUtils.isEmpty(address.getRegion())
                || StringUtils.isEmpty(address.getStreet())
                || StringUtils.isEmpty(address.getDetail())
                || StringUtils.isEmpty(address.getName())
                || StringUtils.isEmpty(address.getPhone())) {
            throw new ApiException("地址参数非法");
        }
    }
}
