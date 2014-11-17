package com.store.api.mongo.service;

import java.util.List;
import java.util.Map;

import com.store.api.mongo.entity.Product;

/**
 * 
 * Revision History
 *
 * @author vincent,2014年11月10日 created it
 */
public interface ProductService {
	
	public void save(Product entity);
	
	public void save(List<Product> entitys);
	
	public void remove(Product entity);
	
	public Product findOne(Long id);
	
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
	 * 查询最大版本号
	 * @param areaId
	 * @return
	 */
	public Long findMaxVer(Long areaId);
	
	/**
	 * 按ID列表查询
	 * @param ids
	 * @return
	 */
	public Map<Long,Product> findByIds(List<Long> ids);

}
