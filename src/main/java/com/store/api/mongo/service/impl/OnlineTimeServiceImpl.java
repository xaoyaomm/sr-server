package com.store.api.mongo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.OnlineTimeRepository;
import com.store.api.mongo.entity.OnlineTime;
import com.store.api.mongo.service.OnlineTimeService;
import com.store.api.mongo.service.SequenceService;

@Service
public class OnlineTimeServiceImpl implements OnlineTimeService {
    
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private OnlineTimeRepository repository;

    @Override
    public OnlineTime findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public void save(OnlineTime entity) {
        repository.save(entity);
    }

    @Override
    public void save(List<OnlineTime> entitys) {
        repository.save(entitys);
    }

    @Override
    public List<OnlineTime> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

}
