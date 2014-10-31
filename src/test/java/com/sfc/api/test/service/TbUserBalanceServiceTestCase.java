package com.sfc.api.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mysql.entity.TbUserBalance;
import com.store.api.mysql.entity.enumeration.UserType;
import com.store.api.mysql.service.TbUserBalanceService;

public class TbUserBalanceServiceTestCase extends BaseServiceTestCase {

	@Autowired
	TbUserBalanceService tbUserBalanceService;

	@Test
	public void testFindByUserIdAndUserType() {
		TbUserBalance bean = tbUserBalanceService.findByUserIdAndUserType(213L,
				UserType.cargo);
		Assert.assertNotNull(bean);
	}
}
