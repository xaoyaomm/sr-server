package com.store.api.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.Catalog;

public interface CatalogRepository extends MongoRepository<Catalog, Long> {

}
