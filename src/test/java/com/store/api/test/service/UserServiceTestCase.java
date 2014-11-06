package com.store.api.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.store.api.mongo.entity.User;
import com.store.api.mongo.entity.enumeration.UserType;
import com.store.api.mongo.service.UserService;

public class UserServiceTestCase extends BaseServiceTestCase {
	
	@Autowired
	private UserService service;
	
	@Test
	public void testSave(){
		User user=new User();
		user.setAddress("测试地址");
		user.setNickName("测试_A");
		user.setPhone("13662241734");
		user.setType(UserType.customer);
		user.setUserName("testa");
		service.save(user);
	}
	
	@Test
	public void testFindOne(){
		User user=service.findOne(2L);
		Assert.assertNotNull(user);
		log.info(user.getNickName());
	}
	
	@Test
	public void testFindByName(){
		User user=service.findByUserName("testa");
		Assert.assertNotNull(user);
		log.info(user.getNickName());
	}

}
