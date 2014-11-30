package com.store.api.mongo.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.store.api.mongo.entity.User;
import com.store.api.mongo.entity.enumeration.UserType;

public interface UserRepository extends MongoRepository<User, Long>{
	
	public User findByUserName(String userName);
	
	public List<User> findByType(UserType type);
	
	public List<User> findByUuid(String uuid);
	
	/**
	 * 按地址位置搜索商户
	 * @param type   用户类型
	 * @param location  坐标点([经度,纬度])
	 * @param distance  搜索半径(单位:米)
	 * @return
	 */
	@Query(value="{'type':?0,'status':1,'location':{'$geoWithin':{'$centerSphere':[?1,?2]}}}")
	public List<User> geoSearch(UserType type,double[] location,double distance);

}
