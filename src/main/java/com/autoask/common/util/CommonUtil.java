package com.autoask.common.util;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 公共类公共方法
 *
 * @author licheng
 */
public class CommonUtil {

    private static final Logger LOG = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 手机号正则匹配模式
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{11}$");

    /**
     * UUID的正则表达式匹配
     */
    private static final Pattern UUID_PATTERN = Pattern
            .compile("^[0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$", Pattern.CASE_INSENSITIVE);

    /**
     * 检查电话号码是否正确
     *
     * @param phone 电话号码
     * @return 如果是电话号码返回true， 否则返回false
     */
    public static boolean isPhoneNumber(String phone) {
        LOG.debug("Check phone number: {}", phone);
        if (StringUtils.isEmpty(phone)) {
            LOG.warn("phone number is empty");
            return false;
        }

        // 手机号必须为长度为11的数字字符串
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            LOG.warn("phone number is not a number sequence which length is 11");
            return false;
        }

        return true;
    }

    /**
     * 检查字符串是否为UUID生成字符串
     *
     * @param uuid UUID字符串
     * @return 是UUID字符串返回true，否则返回false
     */
    public static boolean isUUID(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            return false;
        }
        return UUID_PATTERN.matcher(uuid).matches();
    }

    /**
     * 获取数组当中的第一个值
     * @param array 目标数组
     * @param <T> 任意类型
     * @return null 或者 数组第一个值
     */
    public static <T> T getArrayFirst(T[] array) {
        if (null != array && array.length > 0) {
            return array[0];
        }
        return null;
    }

    /**
     * 获取分页参数所对应的起始值
     * @param page 页码
     * @param count 数量
     * @return 起始值
     */
    public static Integer pageToSkipStart(Integer page, Integer count) {
        int start = 0;
        if ( page != null && page > 0 ){
            start = ( page - 1 ) * count;
        }
        return start;
    }

    public static Integer cleanCount(Integer count) {
        if (count == null || count <= 0) {
            return null;
        }
        return count;
    }
}
