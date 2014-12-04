/**
 * PushVo.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年12月4日
 */
package com.store.api.mongo.entity.vo;

import java.util.List;
import java.util.Map;

/**
 * 
 * Revision History
 * 
 * 2014年12月4日,vincent,created it
 */
public class PushVo {
    
    /** 帐户列表 **/
    private List<String> accountList;
    
    /** 推送内容 **/
    private Map<String, Object> content;
    
    /** 离线保存时间:秒 **/
    private int expire;
    
    /** 消息标题 **/
    private String title;

    public List<String> getAccountList() {
        return accountList;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public int getExpire() {
        return expire;
    }

    public String getTitle() {
        return title;
    }

    public void setAccountList(List<String> accountList) {
        this.accountList = accountList;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
