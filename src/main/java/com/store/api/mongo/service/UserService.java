package com.store.api.mongo.service;


import java.util.List;

import com.store.api.mongo.entity.User;

public interface UserService {
	
	public void save(User entity);
	
	public void save(List<User> entitys);
	
	public User findOne(Long id);
	
	public User findByUserName(String userName);

}
