package com.store.api.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring bean的管理
 * @author @haipenge 
 * haipenge@gmail.com
*  Create Date:2014年7月8日
 */
@Component
public class BeanContextUtil implements ApplicationContextAware {
	@Autowired
	private static ApplicationContext applicationContext = null;

	private static BeanContextUtil beanContextUtil = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String beanName) {
		T bean = null;
		if (StringUtils.isNotEmpty(beanName)) {
			bean = (T) this.applicationContext.getBean(beanName);
		}
		return bean;
	}
	
	public <T> T getBean(Class<T> clazz){
		T bean=null;
		bean=this.applicationContext.getBean(clazz);
		return bean;
	}

	public static BeanContextUtil getInstance() {
		if (beanContextUtil == null) {
			beanContextUtil = (BeanContextUtil) applicationContext.getBean("beanContextUtil");
		}
		return beanContextUtil;
	}
}
