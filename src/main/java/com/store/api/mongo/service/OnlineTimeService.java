package com.store.api.mongo.service;

import java.util.List;

import com.store.api.mongo.entity.OnlineTime;

public interface OnlineTimeService {

    public OnlineTime findOne(Long id);
    
    public List<OnlineTime> findAll();
    
    public void save(OnlineTime entity);
    
    public void save(List<OnlineTime> entitys);
    
    public void deleteAll();
}
