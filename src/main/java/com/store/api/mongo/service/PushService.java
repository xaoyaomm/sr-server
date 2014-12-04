/**
 * PushService.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年11月29日
 */
package com.store.api.mongo.service;

import java.util.List;

/**
 * 
 * Revision History
 * 
 * 2014年11月29日,vincent,created it
 */
public interface PushService {
    
    /**
     * PUSH给商户(只推android)
     * @param accountList
     * @param content 内容JSON
     * @param title
     * @param delay 延时PUSH时长，单位：秒
     */
    public void orderPushToMerc(List<String> accountList,String content,String title,long delay);
    
    /**
     * PUSH用户（推送android和ios）
     * @param account
     * @param content 内容JSON
     * @param title
     */
    public void pushToUser(String account,String content,String title);

}
