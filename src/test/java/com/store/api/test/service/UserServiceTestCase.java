package com.store.api.test.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mongo.entity.User;
import com.store.api.mongo.entity.enumeration.UserType;
import com.store.api.mongo.entity.vo.UserSearch;
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
		System.out.println(user.getId());
	}
	
	@Test
	public void testFindOne(){
		User user=service.findOne(6L);
		Assert.assertNotNull(user);
		log.info(user.getNickName());
	}
	
	@Test
	public void testFindByName(){
		User user=service.findByUserName("vincent");
		Assert.assertNotNull(user);
		log.info(user.getNickName());
	}
	
	@Test
	public void testGeoSearch(){
		List<UserSearch> users=service.geoSearch(UserType.merchants, new double[]{114.036956,22.616613}, 2369);
		System.out.println(users.size());
	}
}
