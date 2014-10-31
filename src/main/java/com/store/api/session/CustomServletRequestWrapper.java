package com.store.api.session;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.store.api.common.Constant;
import com.store.api.utils.Utils;

public class CustomServletRequestWrapper extends HttpServletRequestWrapper {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	private String ssid = "";
	private CustomSession session;

	public CustomServletRequestWrapper(HttpServletRequest request) {
		super(request);
		ssid = (String) request.getAttribute(Constant.SESSION_NAME);
		try {
			if (Utils.isEmpty(ssid))
				throw new Exception(
						"ssid is null in customRequestWrapper position");
			session = new CustomSession(ssid);
		} catch (Exception e) {
			LOG.error("******init customSession is error!\n", e);
		}
		LOG.debug(Constant.SESSION_NAME + ":" + ssid);
	}

	public HttpSession getSession() {
		return session;
	}

	public CustomSession getCustomSession() {
		return session;
	}

	private void printHeaer(HttpServletRequest request) {
		LOG.info(">>CustomRequest--Request Info start------------------------------------------------------");
		// java 获取请求 URL  
		String url = request.getScheme() + "://"; // 请求协议 http 或 https 
		url += request.getHeader("host"); // 请求服务器 
		url += request.getRequestURI(); // 工程名   
		if (request.getQueryString() != null) // 判断请求参数是否为空
		{
			url += "?" + request.getQueryString(); // 参数
		}
		LOG.info("Request URL is:"+url);

		Enumeration headers = request.getHeaderNames();
		if (headers != null) {
			while (headers.hasMoreElements()) {
				Object name = headers.nextElement();
				if (name != null) {
					String value = request.getHeader(name.toString());
					LOG.info(">>CustomRequest Header is:" + name + "=" + value);
				}
			}
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie != null) {
					String name = cookie.getName();
					String value = cookie.getValue();
					LOG.info(">>>CustomRequest Cookie:" + name + " = " + value);
				}
			}
		}
		Enumeration<String> en = request.getAttributeNames();
		if (null != en) {
			while (en.hasMoreElements()) {
				String key = en.nextElement();
				LOG.info(">>>CustomRequest Attribute:" + key + " = "
						+ request.getAttribute(key));
			}
		}
		LOG.info(">>CustomRequest--Request Info end------------------------------------------------------");
	}
}
