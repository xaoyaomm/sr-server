package com.store.api.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.OnlineTime;

public interface OnlineTimeRepository extends MongoRepository<OnlineTime, Long>{
    
}
