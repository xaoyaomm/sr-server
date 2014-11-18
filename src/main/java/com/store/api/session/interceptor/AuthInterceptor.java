package com.store.api.session.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.store.api.common.Constant;
import com.store.api.mongo.entity.User;
import com.store.api.session.annotation.Authorization;
import com.store.api.utils.JsonUtils;
import com.store.api.utils.Utils;

public class AuthInterceptor extends HandlerInterceptorAdapter {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
        	response.setContentType("text/plain;charset=UTF-8");
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
                    obj = request.getSession().getAttribute(auth.type());
                if (null != obj) {// 验证成功
                    Object status = request.getSession().getAttribute(Constant.SESSION_INVALID_KEY);
                    if (null != status && ((String) status).equals(Constant.SESSION_INVALID_VALUE)) {
                        response.getWriter().print(JsonUtils.resultJson(1, "帐号已在其它设备上登录，请重新登录", null));
                        return false;
                    } else{
                        User user=(User) obj;
                        user.setCurrVer(parseVersionName(request));
                        user.setLastUserTime(System.currentTimeMillis());
                        request.getSession().setAttribute(auth.type(),user);
                        return true;
                    }
                } else {// 验证失败
                    response.getWriter().print(JsonUtils.resultJson(1, "尚未登录，请先登录", null));
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
        String client = request.getHeader(Constant.HEADER_STORERUN_CLIENT);
        if (!Utils.isEmpty(client))
            return client.trim();
        else
            return "-";
    }

    private String parseVersionName(HttpServletRequest request) {
        String version = request.getHeader(Constant.HEADER_STORERUN_VERSION);
        if (!Utils.isEmpty(version))
            return version.trim();
        else
            return "-";
    }

    private String parseImei(HttpServletRequest request) {
        String imei = request.getHeader(Constant.HEADER_STORERUN_IMEI);
        if (!Utils.isEmpty(imei))
            return imei.trim();
        else
            return "-";
    }
}
