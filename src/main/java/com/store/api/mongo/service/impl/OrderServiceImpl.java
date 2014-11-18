/**
 * OrderServiceImpl.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年11月15日
 */
package com.store.api.mongo.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.OrderRepository;
import com.store.api.mongo.entity.Order;
import com.store.api.mongo.entity.Product;
import com.store.api.mongo.service.OrderService;
import com.store.api.mongo.service.SequenceService;

/**
 * 
 * Revision History
 * 
 * 2014年11月15日,vincent,created it
 */
@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private SequenceService sequenceService;
    
    @Autowired
    private OrderRepository repository;

    @Override
    public Page<Order> findByCustomerId(Long id, int page, int size) {
        
        PageRequest pr=new PageRequest(page<0?0:page-1, size, Direction.DESC, "status","id");
        return repository.findByCustomerId(id, pr);
    }

    @Override
    public Page<Order> findByMerchantsId(Long id, int page, int size) {
        PageRequest pr=new PageRequest(page<0?0:page-1, size, Direction.DESC, "status","id");
        return repository.findByMerchantsId(id, pr);
    }

    @Override
    public void save(Order entity) {
        if (null == entity.getId()) {
            entity.setId(this.sequenceService.getNextSequence(entity));
        }
        repository.save(entity);
    }

    @Override
    public void save(List<Order> entitys) {
        for (Order entity : entitys) {
            if (null == entity.getId()) {
                entity.setId(sequenceService.getNextSequence(entity));
            }
        }
        repository.save(entitys);
    }

    @Override
    public void remove(Long id) {
        repository.delete(id);
    }

    @Override
    public void remove(List<Order> entitys) {
        repository.delete(entitys);
    }

	@Override
	public Order findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void remove(Order entity) {
		repository.delete(entity);
	}

	@Override
	public List<Order> findAll(Set<Long> ids) {
		Iterator<Order> it =repository.findAll(ids).iterator();
		List<Order> list=new ArrayList<Order>();
		while(it.hasNext()){
			Order order=it.next();
			list.add(order);
		}
		return list;
	}

}
