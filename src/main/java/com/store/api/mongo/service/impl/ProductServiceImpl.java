package com.store.api.mongo.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.ProductRepository;
import com.store.api.mongo.entity.Product;
import com.store.api.mongo.service.ProductService;
import com.store.api.mongo.service.SequenceService;


/**
 * 
 * Revision History
 * 
 * 2014年11月15日,vincent,created it
 */
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
    private SequenceService sequenceService;
	
	@Autowired
	private ProductRepository repository;
	
	@Override
	public void save(Product entity) {
		if (null == entity.getId()) {
            entity.setId(this.sequenceService.getNextSequence(entity));
        }
		entity.setVer(this.sequenceService.getNextSequence(entity.getClass().getSimpleName()+"_ver"));
        repository.save(entity);
	}

	@Override
	public void save(List<Product> entitys) {
		Long ver=this.sequenceService.getNextSequence(entitys.get(0).getClass().getSimpleName()+"_ver");
		for (Product entity : entitys) {
            if (null == entity.getId()) {
                entity.setId(sequenceService.getNextSequence(entity));
            }
            entity.setVer(ver);
        }
        repository.save(entitys);
	}

	@Override
	public void remove(Product entity) {
		repository.delete(entity);
	}

	@Override
	public Product findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public List<Product> findByAreaId(Long areaId) {
		return repository.findByAreaId(areaId);
	}

	@Override
	public List<Product> findByAreaIdAndVerGreaterThan(Long areaId, Long ver) {
		return repository.findByAreaIdAndVerGreaterThan(areaId, ver);
	}

    @Override
    public Long findMaxVer(Long areaId) {
        PageRequest pr=new PageRequest(0, 1, Direction.DESC, "ver");
        Page<Product> page=repository.findByAreaId(areaId, pr);
        if(page.hasContent())
            return page.getContent().get(0).getVer();
        return 0L;
    }

	@Override
	public Map<Long,Product> findByIds(List<Long> ids) {
		Iterator<Product> it =repository.findAll(ids).iterator();
		Map<Long,Product> map=new HashMap<Long, Product>();
		while(it.hasNext()){
			Product pro=it.next();
			map.put(pro.getId(), pro);
		}
		return map;
	}

}
