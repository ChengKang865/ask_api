package com.autoask.mapper.base;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author hyy
 * @craete 2016/7/25 14:11
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
