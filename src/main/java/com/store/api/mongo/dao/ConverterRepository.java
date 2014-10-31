package com.store.api.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.ConverterBean;

public interface ConverterRepository extends MongoRepository<ConverterBean, Long>{
    
    public ConverterBean findByName(String name);

}
