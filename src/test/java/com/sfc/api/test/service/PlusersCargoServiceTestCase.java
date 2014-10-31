package com.sfc.api.test.service;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mysql.entity.PlUsersCargo;
import com.store.api.mysql.service.PlusersCargoService;

public class PlusersCargoServiceTestCase extends BaseServiceTestCase {

	@Autowired
	PlusersCargoService service;

	@Test
	public void testLogin() {
		PlUsersCargo cargo = service.login("13632434676",
				"E10ADC3949BA59ABBE56E057F20F883E");
		Assert.assertNotNull(cargo);
		System.out.println(cargo.getUserName());
	}
	
	@Test
	public void testFindAllWithMobileSet(){
	    Set<String> users=service.findAllWithMobileSet();
	    System.out.println(users.size());
	}
}
