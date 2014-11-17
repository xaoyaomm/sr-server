package com.store.api.mongo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.UserRepository;
import com.store.api.mongo.entity.User;
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

}
