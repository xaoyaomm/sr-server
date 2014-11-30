/**
 * PushServiceImpl.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年11月29日
 */
package com.store.api.mongo.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.store.api.common.Constant;
import com.store.api.mongo.service.PushService;
import com.store.api.utils.PropertiesUtil;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.XingeApp;

/**
 * 
 * Revision History
 * 
 * 2014年11月29日,vincent,created it
 */
public class PushServiceImpl implements PushService {
    
    private static final Logger LOG = LoggerFactory.getLogger(Constant.class);
    
    private long accessId=2100065261;
    private String secretKey="30946cb7a81dfbf22808435404fbee28";
    private static int iosEnv=XingeApp.IOSENV_DEV; //IOS 开发环境
    static{
        InputStream infile = Constant.class
                .getResourceAsStream("/conf/config.properties");
        Properties props = new Properties();
        try {
            props.load(infile);
        } catch (IOException ex) {
            LOG.error("load config.properties file fail:" + ex.getMessage());
        }
        boolean flag=PropertiesUtil.getBooleanProperty(props, "ios_env_dev", true);
        if(flag)
            iosEnv=XingeApp.IOSENV_DEV;
        else
            iosEnv=XingeApp.IOSENV_PROD;
    }
    
    public boolean pushAccountList(List<String> accountList,Map<String, Object> content,String title,int expire) {
        XingeApp xinge = new XingeApp(accessId, secretKey);
        Message message = new Message();
        message.setExpireTime(expire);
        message.setCustom(content);
        message.setType(Message.TYPE_MESSAGE);
        JSONObject ret = xinge.pushAccountList(0, accountList, message);
        return ret.getInt("ret_code")==0;
    }
    
    public boolean pushSingleAccount(String account,Map<String, Object> content,String title,int expire) {
        XingeApp xinge = new XingeApp(accessId, secretKey);
        Message message = new Message();
        message.setExpireTime(expire);
        message.setCustom(content);
        message.setType(Message.TYPE_MESSAGE);
        JSONObject ret = xinge.pushSingleAccount(0, account, message);
        return ret.getInt("ret_code")==0;
    }
    
    public boolean pushSingleAccountIOS(String account,Map<String, Object> content,String title,int expire) {
        XingeApp xinge = new XingeApp(accessId, secretKey);
        MessageIOS message = new MessageIOS();
        message.setExpireTime(expire);
        message.setCustom(content);
        message.setAlert(title);
        message.setBadge(1);
        message.setSound("beep.wav");
        JSONObject ret = xinge.pushSingleAccount(0, account, message,iosEnv);
        return ret.getInt("ret_code")==0;
    }
    
    public boolean pushAccountListIOS(List<String> accountList,Map<String, Object> content,String title,int expire) {
        XingeApp xinge = new XingeApp(accessId, secretKey);
        MessageIOS message = new MessageIOS();
        message.setExpireTime(expire);
        message.setCustom(content);
        message.setAlert(title);
        message.setBadge(1);
        message.setSound("beep.wav");
        JSONObject ret = xinge.pushAccountList(0, accountList, message,iosEnv);
        return ret.getInt("ret_code")==0;
    }

    
    public static void  main(String[] args){
        PushServiceImpl service=new PushServiceImpl();
        
    }
}
