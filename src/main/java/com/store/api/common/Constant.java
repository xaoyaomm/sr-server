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
    public static final String SESSION_NAME="SFCSESSID";
    public static final String HEADER_SFC_IMEI="sfc-im";
    public static final String HEADER_SFC_VERSION="sfc-ver";
    public static final String HEADER_SFC_CLIENT="sfc-cli";
    /** 文本下单报价 时限(秒) **/
    public static final int OFFER_TIME_LIMIT=600;
    
    /** 实时订单、预约订单区分的时间(毫秒) **/
    public static final long REAL_RESERVE_DIFF_TIME=30*60*1000;
    
    /**
     * 计算TOKEN的密钥
     */
    public static final String SFC_KEY="sfc&2012#^%888";
    
    /**
     * 图片URL地址前缀
     */
    public static final String IMG_URL_PRE;

    
    /**
     * 语音文件url地址前缀
     */
    public static final String VOICE_FILE_URL;
    
    /**
     * 搜索接口地址前缀
     */
    public static final String SEARCH_API_DOMAIN;
    
    /**
     * 地理位置接口地址
     */
    public static final String GEO_API_URL;
    
    /**
     * 搜索引擎车辆接口地址
     */
    public static final String VEHICLE_WEBSERVICE_URL;
    
    /**
     * 搜索引擎webservice namespace
     */
    public static final String COSMOS_NAMESPACE = "http://cosmos.sfc365.com/";
    
    /**
     * SESSION中车主对象对应KEY值
     */
    public static final String SESSION_PL_USER="pl_user";
    
    /**
     * SESSION中货主对象对应KEY值
     */
    public static final String SESSION_PL_USER_CARGO="pl_user_cargo";
    
    /**
     * SESSION中业务员对象对应KEY值
     */
    public static final String SESSION_TB_STAFF="tb_staff";
    
    /**
     * SESSION中车辆ID对应KEY值
     */
    public static final String SESSION_VE_VEHICLE_ID="ve_vehicle_id";
    
    /**
     * SESSION是否失效的标志key
     */
    public static final String SESSION_INVALID_KEY="invalid";
    
    /**
     * SESSION是否失效标志value
     */
    public static final String SESSION_INVALID_VALUE="yes";
    
    /**
     * XMPP注册接口地址
     */
    public static final String XMPP_REG_URL;
    
    /**
     * XMPP推送接口地址
     */
    public static final String XMPP_PUSH_URL;

    /**
     * 语音文件存放物理路径
     */
    public static final String AUDIO_PATH;
    
    /**
     * 语音文件请求路径URI前缀
     */
    public static final String AUDIO_PATH_URI_PRE="/attachment/audio/";
    
    /**
     * 图片文件存放物理路径
     */
    public static final String PHOTO_PATH;
    
    /**
     * 图片文件请求路径URI前缀
     */
    public static final String PHOTO_PATH_URI_PRE="/attachment/photo/";
    
    public static final String ATTACH_PATH;
    
    public static final String WSDLFILE_PATH ="/usr/local/wwwroot/api.sfc365.com/Lib/Webservice/";
    
    public static final String WATER_FILE_PATH;
    
    
    /**
     * 网站地址
     */
    public static final String WEBSITE_URL;
    
    /**
     * COOKIE域
     */
    public static final String COOKIE_DOMAIN;
    
    /**
     * 是否在日志中显示坐标上报日志
     */
    public static boolean REPORT_LOG;
    
    /**
     * 搜索距离(米)
     */
    public static Long SEARCH_DISTANCE;
    
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
        VOICE_FILE_URL=props.getProperty("voiceAddress","http://192.168.1.51:81");
        SEARCH_API_DOMAIN=props.getProperty("seApiDomain","http://192.168.1.51:8365");
        GEO_API_URL=props.getProperty("geoApiUrl","http://192.168.1.51:8365/geospatial/spatial?wsdl");
        VEHICLE_WEBSERVICE_URL=props.getProperty("seApiDomain","http://192.168.1.51:8365")+"/cosmos/vehicle?wsdl";
        XMPP_REG_URL=props.getProperty("xmppRegUrl","http://192.168.1.53:9090/plugins/userService");
        XMPP_PUSH_URL=props.getProperty("xmppPushUrl","http://192.168.1.53:9090/plugins/pushmessage");
        AUDIO_PATH=props.getProperty("attachPath","/usr/local/wwwroot/www.sfc365.com")+"/attachment/audio";
        PHOTO_PATH=props.getProperty("attachPath","/usr/local/wwwroot/www.sfc365.com")+"/attachment/photo";
        ATTACH_PATH=props.getProperty("attachPath","/usr/local/wwwroot/www.sfc365.com");
        WEBSITE_URL=props.getProperty("websiteUrl","http://202.96.155.42:81");
        WATER_FILE_PATH=props.getProperty("waterFilePre","/data/api/api/images/");
        COOKIE_DOMAIN=props.getProperty("cookieDomain","api.sfc365.com");
        REPORT_LOG=PropertiesUtil.getBooleanProperty(props, "showReportLocationLog", false);
        SEARCH_DISTANCE=PropertiesUtil.getLongProperty(props, "searchDistance");
    }
}
