package com.sfc.api.test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.store.api.mysql.service.BkBookingService;
import com.store.api.utils.BeanContextUtil;
/**
 * 
 * @author @haipenge 
 * haipenge@gmail.com
*  Create Date:2014年7月8日
 */
public class BeanContextUtilTestCase extends BaseServiceTestCase {

	@Autowired
	private BeanContextUtil beanContextUtil=null;
	@Test
	public void testGetBean() throws Exception{
		BkBookingService bkBookingService=beanContextUtil.getBean("bkBookingServiceImpl");
		Assert.isTrue(bkBookingService!=null&& bkBookingService instanceof BkBookingService);
	}
	@Test
	public void testGetBeanByClass() throws Exception{
		BkBookingService userService=beanContextUtil.getBean(BkBookingService.class);
		Assert.isTrue(userService!=null&& userService instanceof BkBookingService);
	}
}
