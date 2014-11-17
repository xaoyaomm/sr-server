package com.store.api.mongo.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.Order;

public interface OrderRepository extends MongoRepository<Order, Long> {
    
    public Page<Order> findByCustomerId(Long id,Pageable pr);
    
    public Page<Order> findByMerchantsId(Long id,Pageable pr);

}
