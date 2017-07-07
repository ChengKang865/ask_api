package com.autoask.entity.mongo.log;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author hyy
 * @create 2016-11-05 14:35
 */
@Document
public class PayLog {

    @Id
    private String id;

    /**
     * 日志创建时间
     */
    private String createTime;

    /**
     * 操作类型
     */
    private String type;

    /**
     * 日志内容
     */
    private Object content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
