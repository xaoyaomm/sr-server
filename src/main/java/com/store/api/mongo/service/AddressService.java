package com.store.api.mongo.service;

import java.util.List;

import com.store.api.mongo.entity.Address;

public interface AddressService {
    
    public void save(Address entity);
    
    public void save(List<Address> entitys);
    
    public List<Address> findByUserId(Long userId);
    
    public void remove(Long id);
    
    public void remove(Address entity);
    
    public void remove(List<Address> entitys);
    
    public Address findOne(Long id);

}
