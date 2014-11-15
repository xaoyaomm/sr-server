package com.store.api.mongo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.ProductRepository;
import com.store.api.mongo.entity.Product;
import com.store.api.mongo.service.ProductService;
import com.store.api.mongo.service.SequenceService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
    private SequenceService sequenceService;
	
	@Autowired
	private ProductRepository repository;
	
	@Override
	public void save(Product entity) {
		if (null == entity.getId()) {
            entity.setId(this.sequenceService.getNextSequence(entity));
        }
		entity.setVer(this.sequenceService.getNextSequence(entity.getClass().getSimpleName()+"_ver"));
        repository.save(entity);
	}

	@Override
	public void save(List<Product> entitys) {
		Long ver=this.sequenceService.getNextSequence(entitys.get(0).getClass().getSimpleName()+"_ver");
		for (Product entity : entitys) {
            if (null == entity.getId()) {
                entity.setId(sequenceService.getNextSequence(entity));
            }
            entity.setVer(ver);
        }
        repository.save(entitys);
	}

	@Override
	public void remove(Product entity) {
		repository.delete(entity);
	}

	@Override
	public Product findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public List<Product> findByAreaId(Long areaId) {
		return repository.findByAreaId(areaId);
	}

	@Override
	public List<Product> findByAreaIdAndVerGreaterThan(Long areaId, Long ver) {
		return repository.findByAreaIdAndVerGreaterThan(areaId, ver);
	}

}
