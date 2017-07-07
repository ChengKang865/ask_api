package com.autoask.common.util;

import com.autoask.common.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 16-9-10.
 */
public class HttpRequestUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestUtil.class);

    /**
     * 将 request 转成 Map
     *
     * @param request
     * @return Map
     * @throws ApiException
     */
    public static Map<String, Object> getRequestParamsMap(HttpServletRequest request) throws ApiException {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, Object> requestMap = new HashMap<>();

            for (String key : parameterMap.keySet()) {
                requestMap.put(key, CommonUtil.getArrayFirst(parameterMap.get(key)));
            }
            return requestMap;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    public static boolean isMobile(HttpServletRequest request) {
        boolean isMobile = false;
        String[] mobileAgents = { "iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
                "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
                "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
                "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
                "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
                "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
                "pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
                "240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
                "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
                "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
                "mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
                "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
                "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
                "voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
                "Googlebot-Mobile" };
        if (request.getHeader("User-Agent") != null) {
            for (String mobileAgent : mobileAgents) {
                if (request.getHeader("User-Agent").toLowerCase().indexOf(mobileAgent) >= 0) {
                    isMobile = true;
                    break;
                }
            }
        }
        return isMobile;
    }

}
