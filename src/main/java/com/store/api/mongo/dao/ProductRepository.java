package com.store.api.mongo.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.Product;

/**
 * 
 * Revision History
 * 
 * 2014年11月15日,vincent,created it
 */
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
	
	/**
	 * 分页查询
	 * @param areaId
	 * @param pr
	 * @return
	 */
	public Page<Product> findByAreaId(Long areaId,Pageable pr);

}
