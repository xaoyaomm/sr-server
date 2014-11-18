package com.store.api.mongo.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.User;
import com.store.api.mongo.entity.enumeration.UserType;

public interface UserRepository extends MongoRepository<User, Long>{
	
	public User findByUserName(String userName);
	
	public List<User> findByType(UserType type);

}
