package com.store.api.session;

import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.store.api.common.Constant;
import com.store.api.utils.Utils;
import com.store.api.utils.security.SecurityUtil;

public class SessionFilter extends HttpServlet implements Filter {

    private static final long serialVersionUID = 1L;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String ssid = null;
        ssid = request.getParameter(Constant.SESSION_NAME);
        if (StringUtils.isEmpty(ssid))
            ssid = request.getHeader(Constant.SESSION_NAME);
        if (StringUtils.isEmpty(ssid)) {
            ssid = getValueByCookie(request, Constant.SESSION_NAME);
        }
        boolean flag = checkSessionId(ssid);

//        printHeaer(request);

        if (flag) {
            request.setAttribute(Constant.SESSION_NAME, SecurityUtil.decrypt(ssid));
            CustomServletRequestWrapper cRequest = new CustomServletRequestWrapper(request);
            if (!flag) {
                if (null != cRequest.getSession().getAttribute(Constant.SESSION_PL_USER) || null != cRequest.getSession().getAttribute(Constant.SESSION_PL_USER_CARGO)) {
                    setValueToCookie(response, Constant.SESSION_NAME, SecurityUtil.encrypt(cRequest.getCustomSession().getId()));
                }
            }
            chain.doFilter(cRequest, response);
            try {
                cRequest.getCustomSession().saveSession();
            } catch (Exception e) {
                LOG.error("save session is fail.", e);
            }
        } else
            chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
    }

    private void setValueToCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(86400 * 365 * 10);
        cookie.setPath("/");
        cookie.setDomain(Constant.COOKIE_DOMAIN);
        response.addCookie(cookie);
    }

    private String getValueByCookie(HttpServletRequest request, String para) {
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

    private boolean checkSessionId(String sessionId) {
        if (Utils.isEmpty(sessionId))
            return false;
        String id = SecurityUtil.decrypt(sessionId);
        StringBuffer pattern = new StringBuffer();
        pattern.append("(?:").append(SessionService.USER_TYPE_CARGO.getType()).append("|");
        pattern.append(SessionService.USER_TYPE_DRIVE.getType()).append("|");
        pattern.append(SessionService.USER_TYPE_STAFF.getType()).append(")+_\\d+_\\d{7}");
        return Pattern.matches(pattern.toString(), id);
    }

    private void printHeaer(HttpServletRequest request) {
        LOG.info(">>Filter--Request Info start------------------------------------------------------");
        // java 获取请求 URL  
        String url = request.getScheme() + "://"; // 请求协议 http 或 https 
        url += request.getHeader("host"); // 请求服务器 
        url += request.getRequestURI(); // 工程名   
        if (request.getQueryString() != null) // 判断请求参数是否为空
        {
            url += "?" + request.getQueryString(); // 参数
        }
        LOG.info("Request URL is:" + url);
        Enumeration<String> headers = request.getHeaderNames();
        if (headers != null) {
            while (headers.hasMoreElements()) {
                Object name = headers.nextElement();
                if (name != null) {
                    String value = request.getHeader(name.toString());
                    LOG.info(">>Filter Header is:" + name + "=" + value);
                }
            }
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie != null) {
                    String name = cookie.getName();
                    String value = cookie.getValue();
                    LOG.info(">>>Filter Header Cookie:" + name + " = " + value);
                }
            }
        }
        LOG.info(">>Filter--Request Info END------------------------------------------------------");
    }

}
