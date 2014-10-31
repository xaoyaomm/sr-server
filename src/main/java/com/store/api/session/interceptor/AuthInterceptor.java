package com.store.api.session.interceptor;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.store.api.common.Constant;
import com.store.api.session.annotation.Authorization;
import com.store.api.utils.JsonUtils;
import com.store.api.utils.Utils;

public class AuthInterceptor extends HandlerInterceptorAdapter {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            Authorization auth = ((HandlerMethod) handler).getMethodAnnotation(Authorization.class);
            // 没有声明需要权限
            StringBuffer sbUrl = new StringBuffer();
            Enumeration<String> en = request.getParameterNames();// 请求参数-值
            sbUrl.append(request.getRequestURI().toString());
            for (int i = 0; en.hasMoreElements(); i++) {
                String arg = en.nextElement().toString();
                if (i == 0) {
                    sbUrl.append("?");
                } else {
                    sbUrl.append("&");
                }
                sbUrl.append(arg + "=" + request.getParameterValues(arg)[0]);
            }
            
            LOG.error("{}|{}|{}|{}", sbUrl.toString(), request.getSession().getId(), clientVersion(request), parseImei(request));
            

            if (auth == null || Utils.isEmpty(auth.type())) {
                return true;
            } else {
                Object obj = null;
                String contentType = request.getHeader("Content-Type");
                if (request.getMethod().equalsIgnoreCase("post") && !Utils.isEmpty(contentType) && contentType.contains("multipart/form-data")) {
                    obj = ((DefaultMultipartHttpServletRequest) request).getRequest().getSession().getAttribute(auth.type());
                } else
                    obj = request.getSession().getAttribute(auth.type());
                if (null != obj) {// 验证成功

                    Object status = request.getSession().getAttribute(Constant.SESSION_INVALID_KEY);
                    if (null != status && ((String) status).equals(Constant.SESSION_INVALID_VALUE)) {
                        response.getWriter().print(JsonUtils.resultJson(1, "帐号已在其它设备上登录，请重新登录", null));
                        return false;
                    } else
                        return true;
                } else {// 验证失败
                    response.getWriter().print(JsonUtils.resultJson(1, "登录超时，请重新登录", null));
                    return false;
                }
            }
        } else
            return true;
    }

    public String clientVersion(HttpServletRequest request) {
        return parseClientType(request) + "_" + parseVersionName(request);
    }

    private String parseClientType(HttpServletRequest request) {
        String client = request.getHeader(Constant.HEADER_SFC_CLIENT);
        if (!Utils.isEmpty(client))
            return client.trim();
        else {
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
        return "-";
    }

    private String parseVersionName(HttpServletRequest request) {
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
        return "-";
    }

    private String parseImei(HttpServletRequest request) {
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
        return "-";
    }
}
