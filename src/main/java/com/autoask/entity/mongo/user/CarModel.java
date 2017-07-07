package com.autoask.entity.mongo.user;

import org.apache.commons.lang3.StringUtils;

/**
 * 用户的车型
 */
public class CarModel {

    /**
     * 品牌
     */
    private String brand;

    /**
     * 型号
     */
    private String model;

    /**
     * 年份
     */
    private String year;

    /**
     * 详情
     */
    private String detail;


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public static boolean isIllegal(CarModel carModel) {
        if (null == carModel) {
            return true;
        }
        if (StringUtils.isEmpty(carModel.getBrand()) || StringUtils.isEmpty(carModel.getModel())
                || StringUtils.isEmpty(carModel.getYear()) || StringUtils.isEmpty(carModel.getDetail())) {
            return true;
        }
        return false;
    }
}
