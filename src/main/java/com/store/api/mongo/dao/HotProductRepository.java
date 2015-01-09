package com.store.api.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.HotProduct;

public interface HotProductRepository extends MongoRepository<HotProduct, Long> {

}
