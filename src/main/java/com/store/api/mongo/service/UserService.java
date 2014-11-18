package com.store.api.mongo.service;


import java.util.List;

import com.store.api.mongo.entity.User;
import com.store.api.mongo.entity.enumeration.UserType;

public interface UserService {
	
	public void save(User entity);
	
	public void save(List<User> entitys);
	
	public User findOne(Long id);
	
	public User findByUserName(String userName);
	
	public List<User> findByType(UserType type);
	
}
