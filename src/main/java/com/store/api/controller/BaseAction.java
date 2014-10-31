package com.store.api.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.store.api.common.Constant;
import com.store.api.utils.Utils;

public class BaseAction {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected HttpSession session;

    private String imei; // 手机串号

    private String versionName; // 客户端版本号 eg:2.1.2

    private Long versionCode; // 客户端版本号编码 eg:212

    private String clientType; // 客户端类型 android_cargo,ios_cargo,android_drive

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getHeader("Content-Type");
        if (request.getMethod().equalsIgnoreCase("post") && !Utils.isEmpty(contentType) && contentType.contains("multipart/form-data"))
            this.request = ((DefaultMultipartHttpServletRequest) request).getRequest();
        else
            this.request = request;
        this.response = response;
        this.session = this.request.getSession();
        this.imei = parseImei();
        this.versionName = parseVersionName();
        this.versionCode = parseVersionCode();
        this.clientType = parseClientType();
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
        String imei = request.getHeader(Constant.HEADER_SFC_IMEI);
        if (!Utils.isEmpty(imei))
            return imei.trim();
        else {
            String ua = request.getHeader("user-agent");
            if (!Utils.isEmpty(ua)) {
                String[] fs = ua.split(",");
                for (int i = 0; i < fs.length; i++) {
                    if (fs[i].contains("imei=")) {
                        return fs[i].replace("imei=", "");
                    }
                }

            }
        }
        return null;
    }

    private String parseVersionName() {
        String version = request.getHeader(Constant.HEADER_SFC_VERSION);
        if (!Utils.isEmpty(version))
            return version.trim();
        else {
            String ua = request.getHeader("user-agent");
            if (!Utils.isEmpty(ua)) {
                String[] fs = ua.split(",");
                for (int i = 0; i < fs.length; i++) {
                    if (fs[i].contains("versionName")) {
                        return fs[i].replace("versionName=", "");
                    }
                }
            }
        }
        return null;
    }

    private Long parseVersionCode() {
        String version = request.getHeader(Constant.HEADER_SFC_VERSION);
        if (!Utils.isEmpty(version)) {
            if (version.contains("."))
                version = version.replace(".", "");
            if (Utils.isNumber(version))
                return Long.valueOf(version);
        } else {
            String ua = request.getHeader("user-agent");
            if (!Utils.isEmpty(ua)) {
                String[] fs = ua.split(",");
                for (int i = 0; i < fs.length; i++) {
                    if (fs[i].contains("versionCode")) {
                        String code = fs[i].replace("versionCode", "");
                        if (Utils.isNumber(code))
                            return Long.valueOf(code);
                    }
                }
            }

        }
        return null;
    }

    private String parseClientType() {
        String client = request.getHeader(Constant.HEADER_SFC_CLIENT);
        if (!Utils.isEmpty(client))
            return client.trim();
        else{
            String ua = request.getHeader("user-agent");
            if (!Utils.isEmpty(ua)) {
                String[] fs = ua.split(",");
                for (int i = 0; i < fs.length; i++) {
                    if (fs[i].contains("sfc365_client_")) {
                        return fs[i].replace("sfc365_client_", "");
                    } else if (fs[i].contains("iOS")) {
                        return "IOS";
                    }
                }
            }
        }
        return null;
    }
}
