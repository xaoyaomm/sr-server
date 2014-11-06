package com.store.api.session;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.store.api.common.Constant;
import com.store.api.utils.JsonUtils;

/**
 * 自定义session
 * 
 * Revision History
 *
 * @author vincent,2014年11月6日 created it
 */
public class CustomSession implements HttpSession {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private String ssid;

	private Map valueMap;

	private boolean changed = false;// 记录属性是否变化

	/**
	 * 构造函数
	 * 
	 * @param ssid
	 */
	@SuppressWarnings("unchecked")
	public CustomSession(String ssid) throws Exception {
		super();
		this.ssid = ssid;
		// 初始化 session Map对象
		valueMap = new HashMap();
		Map map = SessionService.getInstance().getSession(ssid);
		if (map != null) {// 缓存中存在,load
			valueMap.putAll(map);
		}
	}

	@Override
	public Object getAttribute(String key) {
		if (valueMap.containsKey(key)) {
			if (key.equals(Constant.SESSION_USER_CUSTOMER)
					|| key.equals(Constant.SESSION_USER_MERCHANTS))
				return JsonUtils.json2Object(valueMap.get(key).toString(),
						Object.class);
		}
		return valueMap.get(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		if ("isCache".equalsIgnoreCase(key) && null != value) {
			// fileter链处理完时，设置同步的标记
			try {
				saveSession();
			} catch (Exception e) {
				LOG.error("session setAttribute is fail.key-->" + key, e);
			}
		} else {
			changed = true;
			if (null != value
					&& (key.equals(Constant.SESSION_USER_CUSTOMER) || key
							.equals(Constant.SESSION_USER_MERCHANTS)))
				valueMap.put(key, JsonUtils.object2Json(value));
			else
				valueMap.put(key, value);
		}
	}

	public void removeAttribute(String arg0) {
		valueMap.remove(arg0);
		SessionService.getInstance().removeField(ssid, arg0);
		// changed = true;
	}

	/**
	 * 将session缓存
	 */
	public void saveSession() throws Exception {
		if (changed) {
			SessionService.getInstance().saveSession(ssid, valueMap);
			changed = false;
		}
	}

	public Enumeration getAttributeNames() {
		return Collections.enumeration(valueMap.keySet());
	}

	public long getCreationTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return ssid;
	}

	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(String arg0) {
		return valueMap.get(arg0);
	}

	public String[] getValueNames() {
		return (String[]) valueMap.keySet().toArray();
	}

	public void invalidate() {
		valueMap.clear();
		changed = true;
	}

	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	public void putValue(String arg0, Object arg1) {
		setAttribute(arg0, arg1);
	}

	public void removeValue(String arg0) {
		removeAttribute(arg0);
	}

	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub

	}

}
