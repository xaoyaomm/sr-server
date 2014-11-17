/**
 * OrderProductService.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年11月15日
 */
package com.store.api.mongo.service;

import java.util.List;

import com.store.api.mongo.entity.OrderProduct;

/**
 * 
 * Revision History
 * 
 * 2014年11月15日,vincent,created it
 */
public interface OrderProductService {

    public void save(OrderProduct entity);
    
    public void save(List<OrderProduct> entitys);
    
    public void remove(Long id);
    
    public void remove(List<OrderProduct> entitys);
    
    /**
     * 按订单ID查询
     * @param orderId
     * @return
     */
    public List<OrderProduct> findByOrderId(Long orderId);
}
