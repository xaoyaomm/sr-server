package com.store.api.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.LuckydrawBlackList;

public interface LuckdrawBlackListRepository extends MongoRepository<LuckydrawBlackList, Long>{

}
