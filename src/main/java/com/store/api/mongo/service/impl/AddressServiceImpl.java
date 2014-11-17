package com.store.api.mongo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.AddressRepository;
import com.store.api.mongo.entity.Address;
import com.store.api.mongo.service.AddressService;
import com.store.api.mongo.service.SequenceService;

@Service
public class AddressServiceImpl implements AddressService {
    
    @Autowired
    private SequenceService sequenceService;
    
    @Autowired
    private AddressRepository repository;

    @Override
    public void save(Address entity) {
        if (null == entity.getId()) {
            entity.setId(this.sequenceService.getNextSequence(entity));
        }
        repository.save(entity);
    }

    @Override
    public void save(List<Address> entitys) {
        for (Address entity : entitys) {
            if (null == entity.getId()) {
                entity.setId(sequenceService.getNextSequence(entity));
            }
        }
        repository.save(entitys);
    }

    @Override
    public List<Address> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public void remove(Long id) {
        repository.delete(id);
    }

    @Override
    public void remove(Address entity) {
        repository.delete(entity);
    }

    @Override
    public Address findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public void remove(List<Address> entitys) {
        repository.delete(entitys);
    }

}
