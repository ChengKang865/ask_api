package com.autoask.pay.wechat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by hp on 16-9-7.
 */
public class MapUtil {
    /**
     * 对map根据key进行排序 ASCII 顺序
     */
    public static SortedMap<String, Object> sortMap(Map<String, Object> map) {

        List<Map.Entry<String, Object>> infoIds = new ArrayList<>(map.entrySet());

        // 排序
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return (o1.getKey()).compareTo(o2.getKey());
            }
        });
        // 排序后
        SortedMap<String, Object> sortMap = new TreeMap<>();
        for (Map.Entry<String, Object> infoId : infoIds) {
            String[] split = infoId.toString().split("=");
            sortMap.put(split[0], split[1]);
        }
        return sortMap;
    }

    public void getSortMap() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("nonceStr", PayCommonUtil.createNonceString());
        params.put("package", "prepay_id=" + ("prepay_id"));
        params.put("partnerId", ConfigUtil.MCH_ID);
        params.put("prepayId", ("prepay_id"));
        params.put("signType", ConfigUtil.SIGN_TYPE);
        params.put("timeStamp", Long.toString(new Date().getTime()));
        SortedMap<String, Object> sortMap = MapUtil.sortMap(params);
        System.out.println(sortMap);
    }

}
