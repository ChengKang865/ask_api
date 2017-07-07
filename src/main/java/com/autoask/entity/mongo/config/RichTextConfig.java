package com.autoask.entity.mongo.config;

import com.autoask.entity.mongo.BaseEntity;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hyy on 2016/12/4.
 */
@Document
public class RichTextConfig extends BaseEntity {

    /**
     * 类型 活动/服务/探索
     */
    private String type;

    /**
     * 渠道信息
     */
    private String channel;

    /**
     * 活动名称/探索标题
     */
    @NotBlank
    private String title;

    /**
     * 富文本内容，html格式
     */
    @NotBlank
    private String content;

    private String picUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
