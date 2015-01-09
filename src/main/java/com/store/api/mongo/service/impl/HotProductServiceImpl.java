package com.store.api.mongo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.HotProductRepository;
import com.store.api.mongo.entity.HotProduct;
import com.store.api.mongo.service.HotProductService;
import com.store.api.mongo.service.SequenceService;

@Service
public class HotProductServiceImpl implements HotProductService {

	@Autowired
	private HotProductRepository repository;
	
	@Autowired
    private SequenceService sequenceService;
	
	@Override
	public List<HotProduct> findAll() {
		Sort sort=new Sort(Direction.DESC,"total");
		return repository.findAll(sort);
	}

	@Override
	public boolean exists(long id) {
		return repository.exists(id);
	}

	@Override
	public void save(HotProduct entity) {
		if (0 == entity.getId()) {
            entity.setId(this.sequenceService.getNextSequence(entity));
        }
        repository.save(entity);
	}

	@Override
	public void delete(HotProduct entity) {
		repository.delete(entity);
	}

	@Override
	public void delete(long id) {
		repository.delete(id);
	}

	@Override
	public HotProduct findOne(long id) {
		return repository.findOne(id);
	}

}
