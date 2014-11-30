package com.store.api.mongo.service;

import java.util.List;

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
    public Page<Order> findByCustomerId(long id,int page,int size);
    
    /**
     * 按卖家ID分页查询
     * @param id
     * @param page
     * @param size
     * @return
     */
    public Page<Order> findByMerchantsId(long id,int page,int size);
    
    public void save(Order entity);
    
    public void save(List<Order> entitys);
    
    public void remove(long id);
    
    public void remove(List<Order> entitys);
    
    public void remove(Order entity);
    
    public Order findOne(long id);
    
    /**
     * 查询今天商户错过的订单数
     * @param id
     * @param date
     * @return
     */
    public int findTadayLostByUserId(long id,long date);
    
    /**
     * 分页查询推送给商户的订单数(top)
     * @param mercId
     * @param orderId
     * @return
     */
    public Page<Order> findTopOrder(long mercId,long orderId,int page,int size);
    
    /**
     * 分页查询推送给商户的订单数(tail)
     * @param mercId
     * @param orderId
     * @return
     */
    public Page<Order> findTailOrder(long mercId,long orderId,int page,int size);
}
