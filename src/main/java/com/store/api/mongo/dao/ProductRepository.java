package com.store.api.mongo.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.Product;

public interface ProductRepository extends MongoRepository<Product, Long> {
	
	/**
	 * 按区域ID查询
	 * @param areaId
	 * @return
	 */
	public List<Product> findByAreaId(Long areaId);
	
	/**
	 * 按区域ID和版本号查询
	 * @param areaId
	 * @param ver
	 * @return
	 */
	public List<Product> findByAreaIdAndVerGreaterThan(Long areaId,Long ver);

}
