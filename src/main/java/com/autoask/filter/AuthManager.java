package com.autoask.filter;

import com.autoask.common.util.Constants;
import com.autoask.common.util.PropertiesUtil;
import com.autoask.entity.common.SessionInfo;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author hyy
 * @create 2016-09-07 19:49
 */
public class AuthManager {

    private static Map<String, Boolean> USER_LOGIN_MAP = getUserLoginMap();

    private static Map<String, Role> MERCHANT_ROLE_MAP = getMerchantRoleMap();

    public static boolean needUserLogin(String uri) {
        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        if (uri.startsWith("/")) {
            uri = uri.substring(1, uri.length());
        }
        String[] splitStrArr = uri.split("/");
        int nowLen = 0;
        for (int index = splitStrArr.length - 1; index >= 0; index--) {
            String judgeUri = uri.substring(0, uri.length() - nowLen);
            if (!USER_LOGIN_MAP.containsKey(judgeUri)) {
                nowLen = nowLen + splitStrArr[index].length() + 1;
                continue;
            }
            if (USER_LOGIN_MAP.get(judgeUri)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean hasPermission(String uri, SessionInfo sessionInfo) {
        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        if (uri.startsWith("/")) {
            uri = uri.substring(1, uri.length());
        }
        String[] splitStrArr = uri.split("/");
        int nowLen = 0;
        for (int index = splitStrArr.length - 1; index >= 0; index--) {
            String judgeUri = uri.substring(0, uri.length() - nowLen);
            if (!MERCHANT_ROLE_MAP.containsKey(judgeUri)) {
                nowLen = nowLen + splitStrArr[index].length() + 1;
                continue;
            } else {
                //匹配到uri
                Role role = MERCHANT_ROLE_MAP.get(judgeUri);

                if (!StringUtils.equals(sessionInfo.getLoginType(), role.loginType)) {
                    //类型不相同
                    return false;
                } else if (!StringUtils.equals(Constants.LoginType.STAFF, role.loginType)) {
                    //类型相同，非staff
                    return true;
                } else if (StringUtils.isEmpty(role.permission)) {
                    //staff 但是没有设定权限
                    return true;
                } else if (StringUtils.equals(sessionInfo.getPermission(), Constants.PermissionType.ROOT)) {
                    //管理员用户拥有所有的权限
                    return true;
                } else if (StringUtils.equals(sessionInfo.getPermission(), role.permission)) {
                    //非管理员 但是权限匹配
                    return true;
                } else {
                    //非管理员，权限不匹配
                    return false;
                }
            }
        }
        return true;
    }

    private static Map<String, Boolean> getUserLoginMap() {
        String configFileName = PropertiesUtil.class.getClassLoader().getResource("configuration/user-login.properties").getFile();
        Properties properties = PropertiesUtil.loadProperties(new File(configFileName));
        Map<String, Boolean> configMap = new HashMap<>(properties.size(), 1);
        for (Map.Entry<Object, Object> itemEntry : properties.entrySet()) {
            String key = (String) itemEntry.getKey();
            String uri = key.replaceAll("\\.", "/");
            String val = (String) itemEntry.getValue();
            if (StringUtils.equals(Constants.UserPermission.LOGIN, val)) {
                configMap.put(uri, true);
            } else {
                configMap.put(uri, false);
            }
        }
        return configMap;
    }

    private static Map<String, Role> getMerchantRoleMap() {
        String configFileName = PropertiesUtil.class.getClassLoader().getResource("configuration/url-merchant-permission.properties").getFile();
        Properties properties = PropertiesUtil.loadProperties(new File(configFileName));
        Map<String, Role> configMap = new HashMap<>(properties.size(), 1);
        for (Map.Entry<Object, Object> itemEntry : properties.entrySet()) {
            String key = (String) itemEntry.getKey();
            String uri = key.replaceAll("\\.", "/");
            String val = (String) itemEntry.getValue();
            Role role = new Role();
            if (val.contains(":")) {
                String[] splitArr = val.split(":");
                role.loginType = splitArr[0];
                role.permission = splitArr[1];
            } else {
                role.loginType = val;
            }
            configMap.put(uri, role);
        }
        return configMap;
    }

    private static class Role {
        String loginType;
        String permission;
    }
}
