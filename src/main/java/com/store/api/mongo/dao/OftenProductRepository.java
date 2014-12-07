/**
 * OftenProductRepository.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年12月7日
 */
package com.store.api.mongo.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.OftenProduct;

/**
 * 
 * Revision History
 * 
 * 2014年12月7日,vincent,created it
 */
public interface OftenProductRepository extends MongoRepository<OftenProduct, Long> {
    
    public List<OftenProduct> findByUserId(long userId);
    
    public Page<OftenProduct> findByUserId(long userId,Pageable pr);
    
    public List<OftenProduct> findByUserIdAndProductIdIn(long userId,List<Long> pids);

}
