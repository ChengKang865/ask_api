package com.autoask.entity.common;

/**
 * 登录的session信息
 * loginType staff 为 员工 permission edit check root
 * loginType Partner Factory ServiceProvider
 *
 * @author hyy
 * @create 2016-10-04 22:22
 */
public class SessionInfo {

    /**
     * sessionId
     */
    private String sessionId;

    /**
     * 登录类型 user factory service_provider partner staff
     */
    private String loginType;

    /**
     * user 对应 user_id
     * factory service_provider partner staff
     */
    private String loginId;

    /**
     * login_type为staff时才拥有permission
     */
    private String permission;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public SessionInfo() {

    }

    public SessionInfo(String sessionId, String loginType, String loginId) {
        this.loginType = loginType;
        this.loginId = loginId;
    }

    public SessionInfo(String sessionId, String loginType, String loginId, String permission) {
        this.loginType = loginType;
        this.loginId = loginId;
        this.permission = permission;
    }
}
