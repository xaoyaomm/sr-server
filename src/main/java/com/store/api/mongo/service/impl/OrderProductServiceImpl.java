/**
 * OrderProductServiceImpl.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年11月15日
 */
package com.store.api.mongo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.OrderProductRepository;
import com.store.api.mongo.entity.OrderProduct;
import com.store.api.mongo.service.OrderProductService;
import com.store.api.mongo.service.SequenceService;

/**
 * 
 * Revision History
 * 
 * 2014年11月15日,vincent,created it
 */
@Service
public class OrderProductServiceImpl implements OrderProductService {
    
    @Autowired
    private SequenceService sequenceService;
    
    @Autowired
    private OrderProductRepository repository;

    @Override
    public void save(OrderProduct entity) {
        if (null == entity.getId()) {
            entity.setId(this.sequenceService.getNextSequence(entity));
        }
        repository.save(entity);
    }

    @Override
    public void save(List<OrderProduct> entitys) {
        for (OrderProduct entity : entitys) {
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
    public void remove(List<OrderProduct> entitys) {
        repository.delete(entitys);
    }

    @Override
    public List<OrderProduct> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

}
