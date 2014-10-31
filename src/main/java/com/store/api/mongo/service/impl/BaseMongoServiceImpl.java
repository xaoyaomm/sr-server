package com.store.api.mongo.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.service.BaseService;

/**
 *  基于Mongo的底层服务实现
 * @author @haipenge 
 * haipenge@gmail.com
*  Create Date:2014年8月10日
 * @param <T>
 * @param <ID>
 * @param <D>
 */
public class BaseMongoServiceImpl<T, ID extends Serializable, D extends MongoRepository<T, ID>> implements BaseService<T, ID> {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected D dao = null;

	public BaseMongoServiceImpl(D dao) {
		this.dao = dao;
	}

	@Override
	public void save(T entity) throws ServiceException {
		dao.save(entity);
	}

	@Override
	public void save(Iterable<T> entities) throws ServiceException {
		dao.save(entities);
	}

	@Override
	public void saveAndFlush(T entity) throws ServiceException {

	}

	@Override
	public void remove(ID id) throws ServiceException {
		dao.delete(id);
	}

	@Override
	public void remove(T entity) throws ServiceException {
		dao.delete(entity);
	}

	@Override
	public void removeAll() throws ServiceException {
		dao.deleteAll();
		;
	}

	@Override
	public void removeAllInBatch() throws ServiceException {
		this.removeAll();
	}

	@Override
	public void removeInBatch(Iterable<T> entities) throws ServiceException {
		this.dao.delete(entities);
	}

	@Override
	public T get(ID id) throws ServiceException {
		return dao.findOne(id);
	}

	@Override
	public List<T> getAll() throws ServiceException {
		return dao.findAll();
	}

	@Override
	public List<T> getAll(Iterable<ID> ids) throws ServiceException {
		List<T> res = null;
		Iterable<T> its = dao.findAll(ids);
		res = (List) its;
		return res;
	}

	@Override
	public Page<T> getPage(Map<String, Object> searchParams, int page, int size) throws ServiceException {
		if (page != 0) {
			page = page - 1;
		}
		Pageable pageable = new PageRequest(page, size);
		Page<T> res = this.dao.findAll(pageable);
		return res;
	}

}
