package com.store.api.mongo.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.store.api.mongo.entity.Order;

/**
 * 
 * Revision History
 * 
 * 2014年11月15日,vincent,created it
 */
public interface OrderService {

    /**
     * 按买家ID分页查询
     * @param id
     * @param page
     * @param size
     * @return
     */
    public Page<Order> findByCustomerId(Long id,int page,int size);
    
    /**
     * 按卖家ID分页查询
     * @param id
     * @param page
     * @param size
     * @return
     */
    public Page<Order> findByMerchantsId(Long id,int page,int size);
    
    public List<Order> findAll(Set<Long> ids);
    
    public void save(Order entity);
    
    public void save(List<Order> entitys);
    
    public void remove(Long id);
    
    public void remove(List<Order> entitys);
    
    public void remove(Order entity);
    
    public Order findOne(Long id);
}
