package com.store.api.mongo.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.common.Common;
import com.store.api.common.Constant;
import com.store.api.mongo.dao.UserRepository;
import com.store.api.mongo.entity.User;
import com.store.api.mongo.entity.enumeration.UserType;
import com.store.api.mongo.entity.vo.UserSearch;
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
		if (0 == entity.getId()) {
			entity.setId(this.sequenceService.getNextSequence(entity));
			if (entity.getType().equals(UserType.merchants)) {
				if (this.sequenceService.getNextSequence("merc_num") < 1000) {
					this.sequenceService.SetNextSequence("merc_num", 1000);
					entity.setMercNum(this.sequenceService.getNextSequence("merc_num"));
				}
			}
		}
		repository.save(entity);

	}

	@Override
	public void save(List<User> entitys) {
		for (User entity : entitys) {
			if (0 == entity.getId()) {
				entity.setId(sequenceService.getNextSequence(entity));
				if (entity.getType().equals(UserType.merchants))
					if (this.sequenceService.getNextSequence("merc_num") < 1000) {
						this.sequenceService.SetNextSequence("merc_num", 1000);
						entity.setMercNum(this.sequenceService.getNextSequence("merc_num"));
					}
			}
		}
		repository.save(entitys);
	}

	@Override
	public User findOne(long id) {
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
	 * 
	 * @param type 用户类型
	 * @param location 坐标点([经度,纬度])
	 * @param distance 搜索半径(单位:米)
	 * @return
	 */
	@Override
	public List<UserSearch> geoSearch(UserType type, double[] location, long distance) {
		List<User> list = repository.geoSearch(type, location, distance / Constant.EARTH_RADIUS);
		List<UserSearch> voList = new ArrayList<UserSearch>();
		for (User user : list) {
			UserSearch us = new UserSearch();
			us.setDistance(Common.getDistance(location[0], location[1], user.getLocation()[0], user.getLocation()[1]));
			us.setUser(user);
			voList.add(us);
		}
		Collections.sort(voList);
		return voList;
	}
}
