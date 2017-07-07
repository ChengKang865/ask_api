package com.autoask.common.util;

/**
 * Created by weid on 16-9-3.
 */

import java.util.List;

/**
 * 列表结果返回对象
 */
public class ListSlice <T> {

    // 返回的对象列表
    private List<T> result;

    // 符合查询的总数目
    private Long total;

    public ListSlice(List<T> result, Long count) {
        this.result = result;
        this.total = count;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
