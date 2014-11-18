package com.store.api.controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.store.api.common.Constant;
import com.store.api.mongo.entity.User;
import com.store.api.mongo.entity.enumeration.UserType;
import com.store.api.session.SessionService;
import com.store.api.utils.JsonUtils;
import com.store.api.utils.Utils;
import com.store.api.utils.security.SecurityUtil;

public class BaseAction {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    
    protected Map<String, Object> result = new HashMap<String, Object>();

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected HttpSession session;

    private String imei; // 手机串号

    private String versionName; // 客户端版本号

    private Long versionCode; // 客户端版本号编码

    private String clientType; // 客户端类型
    
    protected  DecimalFormat nfmt = new DecimalFormat();

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = this.request.getSession();
        this.imei = parseImei();
        this.versionName = parseVersionName();
        this.versionCode = parseVersionCode();
        this.clientType = parseClientType();
        nfmt.setMaximumFractionDigits(1); 
    }

    protected String getImei() {
        if (Utils.isEmpty(imei))
            return "";
        else
            return imei.trim();
    }

    protected String getVersionName() {
        if (Utils.isEmpty(versionName))
            return "";
        else
            return versionName.trim();
    }

    protected Long getVersionCode() {
        if (null == versionCode)
            return 0L;
        else
            return versionCode;
    }

    protected Long getClientType() {
        if (Utils.isEmpty(clientType))
            return 0L;
        else if (clientType.trim().toLowerCase().contains("android")) {
            return 1L;
        } else if (clientType.trim().toLowerCase().contains("ios")) {
            return 2L;
        } else {
            return 0L;
        }
    }

    protected String getValueByCookie(String para) {
        Cookie cookies[] = request.getCookies();
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                if (para.equals(cookies[i].getName()) && null != cookies[i].getValue()) {
                    return cookies[i].getValue();
                }
            }
        }
        return null;
    }

    protected void setValueToCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(86400 * 365 * 10);
        cookie.setPath("/");
        cookie.setDomain(Constant.COOKIE_DOMAIN);
        response.addCookie(cookie);
    }

    private String parseImei() {
        String imei = request.getHeader(Constant.HEADER_STORERUN_IMEI);
        if (!Utils.isEmpty(imei))
            return imei.trim();
        return null;
    }

    private String parseVersionName() {
        String version = request.getHeader(Constant.HEADER_STORERUN_VERSION);
        if (!Utils.isEmpty(version))
            return version.trim();
        return null;
    }

    private Long parseVersionCode() {
        String version = request.getHeader(Constant.HEADER_STORERUN_VERSION);
        if (!Utils.isEmpty(version)) {
            if (version.contains("."))
                version = version.replace(".", "");
            if (Utils.isNumber(version))
                return Long.valueOf(version);
        }
        return 0L;
    }

    private String parseClientType() {
        String client = request.getHeader(Constant.HEADER_STORERUN_CLIENT);
        if (!Utils.isEmpty(client))
            return client.trim();
        return null;
    }
    
    protected void initSession (UserType type,User user,boolean delOld)throws Exception{
        String oldSessionId = null;
        String sessid = null;
        Map<String, String> map = new HashMap<String, String>();
        if (type.equals(UserType.merchants)) {
            if (null != request.getSession().getAttribute(Constant.SESSION_USER))
                oldSessionId = request.getSession().getId();
            map.put(Constant.SESSION_USER, JsonUtils.object2Json(user));
            sessid = SessionService.getInstance().createSession(user.getId() + "", UserType.merchants, map, null);
        } else if(type.equals(UserType.customer)) {
            if (null != request.getSession().getAttribute(Constant.SESSION_USER))
                oldSessionId = request.getSession().getId();
            map.put(Constant.SESSION_USER, JsonUtils.object2Json(user));
            sessid = SessionService.getInstance().createSession(user.getId() + "", UserType.customer, map, delOld?oldSessionId:null);
        }else{
            map.put(Constant.SESSION_USER, JsonUtils.object2Json(user));
            sessid = SessionService.getInstance().createSession(user.getId() + "", UserType.visitor, map, null);
        }
        if (!Utils.isEmpty(sessid))
            setValueToCookie(Constant.SESSION_NAME, SecurityUtil.encrypt(sessid));
    }
}
