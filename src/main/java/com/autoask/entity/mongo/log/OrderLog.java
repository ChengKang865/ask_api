package com.autoask.entity.mongo.log;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author hyy
 * @create 2016-12-14 18:23
 */

@Document
public class OrderLog {

    @Id
    private String id;

    /**
     * 订单id
     */
    @Indexed
    private String orderId;

    /**
     * 操作人员类型
     */
    private String loginType;

    /**
     * 操作人员id
     */
    @Indexed
    private String loginId;


    /**
     * 内容类型
     */
    private String operateType;

    /**
     * 操作内容
     */
    private String content;

    @Indexed
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
