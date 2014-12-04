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
    public static final String HEADER_STORERUN_IMEI="SR-IM";
    public static final String HEADER_STORERUN_VERSION="SR-VER";
    public static final String HEADER_STORERUN_CLIENT="SR-CLI";
    
    /**
     * 位置计算弧度系数
     */
    public static final double EARTH_RADIUS = 6378137;
    
    /**
     * 图片URL地址前缀
     */
    public static final String IMG_URL_PRE;
    
    /**
     * SESSION中用户对象对应KEY值
     */
    public static final String SESSION_USER="user";
   
    /**
     * SESSION是否失效的标志key
     */
    public static final String SESSION_INVALID_KEY="invalid";
    
    /**
     * SESSION是否失效标志value
     */
    public static final String SESSION_INVALID_VALUE="yes";
    
    /**
     * 搜索距离(米)
     */
    public static Long SEARCH_DISTANCE;
    
    /**
     * COOKIE域
     */
    public static final String COOKIE_DOMAIN;
    
    /**
     * IOS推送环境，是否是开发环境
     */
    public static final boolean PUSH_IOS_ENV_DEV;
    
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
        SEARCH_DISTANCE=PropertiesUtil.getLongProperty(props, "searchDistance");
        COOKIE_DOMAIN=props.getProperty("cookieDomain","202.96.155.42");
        PUSH_IOS_ENV_DEV=PropertiesUtil.getBooleanProperty(props, "ios_env_dev", true);
    }
}
