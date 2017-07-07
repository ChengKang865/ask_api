package com.autoask.entity.mongo.product;

import java.util.List;

/**
 * @auth hyy
 * @create
 */
public class Meta {

    private String name;

    private List<String> valueList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getValueList() {
        return valueList;
    }

    public void setValueList(List<String> valueList) {
        this.valueList = valueList;
    }
}
