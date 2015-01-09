package com.store.api.mongo.service;

import java.util.List;

import com.store.api.mongo.entity.HotProduct;

public interface HotProductService {
	
	/**
	 * 查询所有热销商品
	 * @return
	 */
	public List<HotProduct> findAll();
	
	/**
	 * 查询是否已存在此热销商品
	 * @param id
	 * @return
	 */
	public boolean exists(long id);
	
	public void save(HotProduct entity);
	
	public void delete(HotProduct entity);
	
	public void delete(long id);
	
	public HotProduct findOne(long id);

}
