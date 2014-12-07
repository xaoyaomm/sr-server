/**
 * OftenProductServiceImpl.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年12月7日
 */
package com.store.api.mongo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.OftenProductRepository;
import com.store.api.mongo.entity.OftenProduct;
import com.store.api.mongo.entity.subdocument.OrderProduct;
import com.store.api.mongo.service.OftenProductService;
import com.store.api.mongo.service.SequenceService;

/**
 * 
 * Revision History
 * 
 * 2014年12月7日,vincent,created it
 */
@Service
public class OftenProductServiceImpl implements OftenProductService {
    
    @Autowired
    private SequenceService sequenceService;
    
    @Autowired
    private OftenProductRepository repository;

    @Override
    public List<OftenProduct> findByUserId(long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<OftenProduct> findTopByUserId(long userId, int top) {
        PageRequest pr=new PageRequest(0, top, Direction.DESC, "num");
        Page<OftenProduct> page=repository.findByUserId(userId, pr);
        return page.getContent();
    }

    @Override
    public void save(OftenProduct entity) {
        if (0 == entity.getId()) {
            entity.setId(this.sequenceService.getNextSequence(entity));
        }
        repository.save(entity);
    }

    @Override
    public void save(List<OftenProduct> entitys) {
        for (OftenProduct entity : entitys) {
            if (0 == entity.getId()) {
                entity.setId(sequenceService.getNextSequence(entity));
            }
        }
        repository.save(entitys);
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }

    @Override
    public void delete(OftenProduct entity) {
        repository.delete(entity);
    }

    @Override
    public void addToOftenProduct(long userId, List<OrderProduct> products) {
        List<Long> ids=new ArrayList<Long>();
        for (OrderProduct pro : products) {
            ids.add(pro.getProductId());
        }
        List<OftenProduct> opList = repository.findByUserIdAndProductIdIn(userId, ids);
        List<OftenProduct> saveList=new ArrayList<OftenProduct>();
        for (OrderProduct pro : products) {
            boolean flag=false;
            for (OftenProduct op : opList) {
                if(pro.getProductId()==op.getProductId()){
                    op.setNum(op.getNum()+1);
                    flag=true;
                }
            }
            if(!flag){
                OftenProduct newOp=new OftenProduct();
                newOp.setNum(1);
                newOp.setProductId(pro.getProductId());
                newOp.setUserId(userId);
                saveList.add(newOp);
            }
        }
        saveList.addAll(opList);
        save(saveList);
    }

}
