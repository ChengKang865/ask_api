package com.autoask.common.util;

import org.apache.commons.collections.MapUtils;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by hp on 16-9-24.
 */
public class MapUtil {
    /**
     * Map 按 key 的升序排列
     *
     * @param originMap 原始的Map
     * @return Map
     */
    public static Map<String, String> sortMapByKey(Map<String, String> originMap) {
        if (MapUtils.isEmpty(originMap)) {
            return null;
        }

        Map<String, String> resultMap = new TreeMap<>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 升序排序
                        return obj1.compareTo(obj2);
                    }
                });

        Set<String> keySet = originMap.keySet();
        for (String key : keySet) {
            resultMap.put(key, originMap.get(key));
        }
        return resultMap;
    }
}