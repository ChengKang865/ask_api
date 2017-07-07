package com.autoask.entity.common.weiixn;
/**
 * click类型按钮
 * @author ck
 *
 * @Create 2017年6月7日下午5:03:58
 */
public class ClickButton extends Button{

	/**
	 * 菜单的响应动作类型
	 */
    private String type;

    /**
     * 菜单KEY值，用于消息接口推送，不超过128字节
     */
    private String key;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

}
