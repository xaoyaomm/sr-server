/**
 * OrderOfferService.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年11月15日
 */
package com.store.api.mongo.service;

import java.util.List;

import com.store.api.mongo.entity.OrderOffer;

/**
 * 
 * Revision History
 * 
 * 2014年11月15日,vincent,created it
 */
public interface OrderOfferService {
    
    public void save(OrderOffer entity);
    
    public void save(List<OrderOffer> entitys);
    
    public void remove(Long id);
    
    public void remove(List<OrderOffer> entitys);
    
    /**
     * 按订单ID查询
     * @param id
     * @return
     */
    public List<OrderOffer> findByOrderId(Long id);
    
    /**
     * 按卖家ID查询
     * @param id
     * @return
     */
    public List<OrderOffer> findByMerchantsId(Long id);

}
