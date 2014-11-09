package com.store.api.session;

import java.io.IOException;
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
import com.store.api.mongo.entity.enumeration.UserType;
import com.store.api.utils.Utils;
import com.store.api.utils.security.SecurityUtil;

/**
 * session处理Filter
 * 
 * Revision History
 *
 * @author vincent,2014年11月6日 created it
 */
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

        if (flag) {
            request.setAttribute(Constant.SESSION_NAME, SecurityUtil.decrypt(ssid));
            CustomServletRequestWrapper cRequest = new CustomServletRequestWrapper(request);
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
        pattern.append("(?:").append(UserType.customer).append("|");
        pattern.append(UserType.merchants);
        pattern.append(")+_\\d+_(\\d{7})*");
        return Pattern.matches(pattern.toString(), id);
    }

}
