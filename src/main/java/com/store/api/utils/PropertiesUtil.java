package com.store.api.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * Revision History
 * 
 * 2014年4月29日,vincent,created it
 */
public class PropertiesUtil {
    private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * 从Properties获取整型，默认值为零
     */
    public static int getIntProperty(Properties props, String key) {
        return getIntProperty(props,key,0);
    }
    
    /**
     * 从Properties获取整型
     * @param defaultValue 默认值
     */
    public static int getIntProperty(Properties props, String key, int defaultValue) {
        int result = defaultValue;
        if (props != null) {
            String value = props.getProperty(key);
            try {
                result = Integer.parseInt(value);
            } catch (Exception ex) {
                LOG.error("get property fail:" + ex.getMessage());
            }
        }
        return result;
    }

    /**
     * 从Properties获取长整型，默认值为零
     */
    public static long getLongProperty(Properties props, String key) {
        return getLongProperty(props,key,0);
    }
    
    /**
     * 从Properties获取长整型
     * @param defaultValue 默认值
     */
    public static long getLongProperty(Properties props, String key, long defaultValue) {
        long result = defaultValue;
        if (props != null) {
            String value = props.getProperty(key);
            try {
                result = Long.parseLong(value);
            } catch (Exception ex) {
                LOG.error("get property fail:" + ex.getMessage());
            }
        }
        return result;
    }
    
    /**
     * 从Properties获取DOUBLE型
     * @param defaultValue 默认值
     */
    public static double getDoubleProperty(Properties props, String key, double defaultValue) {
    	double result = defaultValue;
        if (props != null) {
            String value = props.getProperty(key);
            try {
                result = Double.parseDouble(value);
            } catch (Exception ex) {
                LOG.error("get property fail:" + ex.getMessage());
            }
        }
        return result;
    }

    /**
     * 从Properties获取boolean型，默认false
     */
    public static boolean getBooleanProperty(Properties props, String key) {
        return getBooleanProperty(props,key,false);
    }
    
    /**
     * 从Properties获取boolean型
     * @param defaultValue 默认值
     */
    public static boolean getBooleanProperty(Properties props, String key, boolean defaultValue) {
        boolean result = defaultValue;
        if (props != null) {
            String value = props.getProperty(key);
            try {
                result = Boolean.parseBoolean(value);
            } catch (Exception ex) {
                LOG.error("get property fail:" + ex.getMessage());
            }
        }
        return result;
    }

}