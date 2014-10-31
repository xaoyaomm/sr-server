package com.store.api.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.store.api.utils.PropertiesUtil;

public class Constant {
    private static final Logger LOG = LoggerFactory.getLogger(Constant.class);
    public static final String SUCCESS="SUCCESS";
    public static final String FAIL="FAIL";
    public static final String SESSION_NAME="STORERUNSSID";
    public static final String HEADER_SFC_IMEI="STORERUN-IM";
    public static final String HEADER_SFC_VERSION="STORERUN-VER";
    public static final String HEADER_SFC_CLIENT="STORERUN-CLI";
    
    /**
     * 图片URL地址前缀
     */
    public static final String IMG_URL_PRE;
    
    /**
     * SESSION中车主对象对应KEY值
     */
    public static final String SESSION_PL_USER="pl_user";
    
    /**
     * SESSION中货主对象对应KEY值
     */
    public static final String SESSION_PL_USER_CARGO="pl_user_cargo";
    
    /**
     * SESSION是否失效的标志key
     */
    public static final String SESSION_INVALID_KEY="invalid";
    
    /**
     * SESSION是否失效标志value
     */
    public static final String SESSION_INVALID_VALUE="yes";
    
    /**
     * 推送注册接口地址
     */
    public static final String XMPP_REG_URL;
    
    /**
     * 推送接口地址
     */
    public static final String XMPP_PUSH_URL;

    
    /**
     * 搜索距离(米)
     */
    public static Long SEARCH_DISTANCE;
    
    /**
     * COOKIE域
     */
    public static final String COOKIE_DOMAIN;
    
    static{
        InputStream infile = Constant.class
                .getResourceAsStream("/conf/config.properties");
        Properties props = new Properties();
        try {
            props.load(infile);
        } catch (IOException ex) {
            LOG.error("load config.properties file fail:" + ex.getMessage());
        }
        IMG_URL_PRE=props.getProperty("imgAddress","http://192.168.1.51:81");
        XMPP_REG_URL=props.getProperty("xmppRegUrl","http://192.168.1.53:9090/plugins/userService");
        XMPP_PUSH_URL=props.getProperty("xmppPushUrl","http://192.168.1.53:9090/plugins/pushmessage");
        SEARCH_DISTANCE=PropertiesUtil.getLongProperty(props, "searchDistance");
        COOKIE_DOMAIN=props.getProperty("cookieDomain","api.sfc365.com");
    }
}
