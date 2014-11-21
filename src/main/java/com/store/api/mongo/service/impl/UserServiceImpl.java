package com.store.api.mongo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.store.api.common.Constant;
import com.store.api.mongo.dao.UserRepository;
import com.store.api.mongo.entity.User;
import com.store.api.mongo.entity.enumeration.UserType;
import com.store.api.mongo.service.SequenceService;
import com.store.api.mongo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private UserRepository repository;

	@Override
	public void save(User entity) {
		if (null == entity.getId()) {
			entity.setId(this.sequenceService.getNextSequence(entity));
		}
		repository.save(entity);

	}

	@Override
	public void save(List<User> entitys) {
		for (User entity : entitys) {
			if (null == entity.getId()) {
				entity.setId(sequenceService.getNextSequence(entity));
			}
		}
		repository.save(entitys);
	}

	@Override
	public User findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public User findByUserName(String userName) {
		return repository.findByUserName(userName);
	}

	@Override
	public List<User> findByType(UserType type) {
		return repository.findByType(type);
	}

	@Override
	public User findByUuid(String uuid) {
		List<User> list = repository.findByUuid(uuid);
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	/**
	 * 按地址位置搜索商户
	 * @param type   用户类型
	 * @param location  坐标点([经度,纬度])
	 * @param distance  搜索半径(单位:米)
	 * @return
	 */
	@Override
	public List<User> geoSearch(UserType type, Double[] location,Double distance) {
		List<User> list=repository.geoSearch(type, location, distance/Constant.EARTH_RADIUS);
		return list;
	}
}
