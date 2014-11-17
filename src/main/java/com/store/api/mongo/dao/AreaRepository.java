package com.store.api.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.Area;

public interface AreaRepository extends MongoRepository<Area, Long> {

}
