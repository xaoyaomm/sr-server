/**
 * PushServiceImpl.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年11月29日
 */
package com.store.api.mongo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.store.api.common.Constant;
import com.store.api.mongo.entity.vo.PushVo;
import com.store.api.mongo.service.PushService;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.XingeApp;

/**
 * 
 * Revision History
 * 
 * 2014年11月29日,vincent,created it
 */
@Service
public class PushServiceImpl implements PushService {

    private static final Logger LOG = LoggerFactory.getLogger(Constant.class);

    private static LinkedList<PushVo> pushList = new LinkedList<PushVo>();

    private static final long accessId = 2100065261;

    private static final String secretKey = "30946cb7a81dfbf22808435404fbee28";

    private static int iosEnv = XingeApp.IOSENV_DEV; // IOS 开发环境
    
    private PushQueueTask task=null;

    /** 默认离线时间 **/
    private int defExpire = 300;
    static {
        if (Constant.PUSH_IOS_ENV_DEV)
            iosEnv = XingeApp.IOSENV_DEV;
        else
            iosEnv = XingeApp.IOSENV_PROD;
    }

    /**
     * 推送给ANDROID
     * @param accountList
     * @param content
     * @param title
     * @param expire
     * @return
     */
    private boolean pushAccountAndroid(List<String> accountList, Map<String, Object> content, String title, int expire) {
        XingeApp xinge = new XingeApp(accessId, secretKey);
        Message message = new Message();
        message.setExpireTime(expire);
        message.setCustom(content);
        message.setType(Message.TYPE_MESSAGE);
        JSONObject ret = null;
        if (accountList.size() > 1)
            ret = xinge.pushAccountList(0, accountList, message);
        else
            ret = xinge.pushSingleAccount(0, accountList.get(0), message);
        if (null != ret) {
            LOG.info("android push to:" + accountList + " return:" + ret.toString());
            return ret.getInt("ret_code") == 0;
        } else
            return false;
    }

    /**
     * 推送到IOS
     * @param accountList
     * @param content
     * @param title
     * @param expire
     * @return
     */
    private boolean pushAccountIOS(List<String> accountList, Map<String, Object> content, String title, int expire) {
        XingeApp xinge = new XingeApp(accessId, secretKey);
        MessageIOS message = new MessageIOS();
        message.setExpireTime(expire);
        message.setCustom(content);
        message.setAlert(title);
        message.setBadge(1);
        message.setSound("beep.wav");
        JSONObject ret = null;
        if (accountList.size() > 1)
            ret = xinge.pushAccountList(0, accountList, message, iosEnv);
        else
            ret = xinge.pushSingleAccount(0, accountList.get(0), message, iosEnv);
        if (null != ret) {
            LOG.info("ios push to:" + accountList + " return:" + ret.toString());
            return ret.getInt("ret_code") == 0;
        } else
            return false;
    }

    public void orderPushToMerc(List<String> accountList, String content, String title, long delay) {
        PushVo vo = new PushVo();
        Map<String , Object> map=new HashMap<String, Object>();
        map.put("data", content);
        vo.setAccountList(accountList);
        vo.setContent(map);
        vo.setTitle(title);
        vo.setExpire(defExpire);
        PushDelay push = new PushDelay(vo);
        push.push(delay);

    }

    public void pushToUser(String account, String content, String title) {
        PushVo vo = new PushVo();
        List<String> accountList=new ArrayList<String>();
        Map<String , Object> map=new HashMap<String, Object>();
        map.put("data", content);
        accountList.add(account);
        vo.setAccountList(accountList);
        vo.setContent(map);
        vo.setTitle(title);
        vo.setExpire(defExpire);
        pushList.offer(vo);
        startPushTask();
    }

    /**
     * 处理延迟推送的内部类
     * 
     * Revision History
     * 
     * 2014年12月4日,vincent,created it
     */
    private class PushDelay {
        private Timer timer;

        private PushVo vo;

        PushDelay(PushVo vo) {
            this.vo = vo;
        }

        void push(long delay) {
            timer = new Timer();
            timer.schedule(new PushDelayTask(), delay * 1000);
        }

        class PushDelayTask extends TimerTask {
            @Override
            public void run() {
                pushAccountAndroid(vo.getAccountList(), vo.getContent(), vo.getTitle(), vo.getExpire());
                timer.cancel();
            }

        }
    }

    /**
     * 处理异步推送的TASK
     * 
     * Revision History
     * 
     * 2014年12月4日,vincent,created it
     */
    class PushQueueTask extends TimerTask {
        @Override
        public void run() {
            try{
            PushVo vo = pushList.peek();
            if (null != vo) {
                pushAccountAndroid(vo.getAccountList(), vo.getContent(), vo.getTitle(), vo.getExpire());
                pushAccountIOS(vo.getAccountList(), vo.getContent(), vo.getTitle(), vo.getExpire());
            }
            }catch(Exception e){
                LOG.error("PushQueueTask run fail.",e);
            }
        }
    }
    
    private void startPushTask() {
        if (null == task) {
            LOG.info("PushQueueTask is run...");
            task = new PushQueueTask();
            Timer timer = new Timer();
            timer.schedule(task, 0, 2000);
        }
    }

    public static void main(String[] args) {

    }
}
