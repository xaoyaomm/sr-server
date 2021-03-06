/**
 * OrderServiceImpl.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年11月15日
 */
package com.store.api.mongo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.OrderRepository;
import com.store.api.mongo.entity.Order;
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
    public Page<Order> findByCustomerId(long id, int page, int size) {
        
        PageRequest pr=new PageRequest(page<0?0:page-1, size, Direction.DESC, "status","id");
        return repository.findByCustomerId(id, pr);
    }

    @Override
    public Page<Order> findByMerchantsId(long id, int page, int size) {
        PageRequest pr=new PageRequest(page<0?0:page-1, size, Direction.DESC, "status","id");
        return repository.findByMerchantsId(id, pr);
    }

    @Override
    public void save(Order entity) {
        if (0 == entity.getId()) {
            entity.setId(this.sequenceService.getNextSequence(entity));
        }
        repository.save(entity);
    }

    @Override
    public void save(List<Order> entitys) {
        for (Order entity : entitys) {
            if (0 == entity.getId()) {
                entity.setId(sequenceService.getNextSequence(entity));
            }
        }
        repository.save(entitys);
    }

    @Override
    public void remove(long id) {
        repository.delete(id);
    }

    @Override
    public void remove(List<Order> entitys) {
        repository.delete(entitys);
    }

	@Override
	public Order findOne(long id) {
		return repository.findOne(id);
	}

	@Override
	public void remove(Order entity) {
		repository.delete(entity);
	}

    @Override
    public int findTadayLostByUserId(long id, long date) {
        return repository.findTadayLostByUserId(id, date);
    }

    @Override
    public Page<Order> findTopOrderWithMercPush(long mercId, long orderId, int page, int size) {
        PageRequest pr=new PageRequest(page<0?0:page-1, size, Direction.DESC, "createDate");
        return repository.findTopOrderWithMercPush(mercId,orderId,pr);
    }

    @Override
    public Page<Order> findTailOrderWithMercPush(long mercId, long orderId, int page, int size) {
        PageRequest pr=new PageRequest(page<0?0:page-1, size, Direction.DESC, "createDate");
        return repository.findTailOrderWithMercPush(mercId,orderId,pr);
    }

	@Override
	public Page<Order> findTopOrderWithCustomer(long customerId, long orderId, int page, int size) {
		PageRequest pr=new PageRequest(page<0?0:page-1, size, Direction.DESC, "createDate");
		return repository.findTopOrderWithCustomer(customerId, orderId, pr);
	}

	@Override
	public Page<Order> findTailOrderWithCustomer(long customerId, long orderId, int page, int size) {
		PageRequest pr=new PageRequest(page<0?0:page-1, size, Direction.DESC, "createDate");
		return repository.findTailOrderWithCustomer(customerId, orderId, pr);
	}

	@Override
	public Page<Order> findTopOrderWithMerc(long mercId, long orderId, int page, int size) {
		PageRequest pr=new PageRequest(page<0?0:page-1, size, Direction.DESC, "createDate");
        return repository.findTopOrderWithMerc(mercId,orderId,pr);
	}

	@Override
	public Page<Order> findTailOrderWithMerc(long mercId, long orderId, int page, int size) {
		PageRequest pr=new PageRequest(page<0?0:page-1, size, Direction.DESC, "createDate");
        return repository.findTailOrderWithMerc(mercId,orderId,pr);
	}

}
