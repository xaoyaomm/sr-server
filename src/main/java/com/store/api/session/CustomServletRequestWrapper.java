package com.store.api.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.store.api.common.Constant;
import com.store.api.utils.Utils;

/**
 * 扩展Request对象
 * 
 * Revision History
 *
 * @author vincent,2014年11月6日 created it
 */
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

}
