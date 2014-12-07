/**
 * OftenProductService.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年12月7日
 */
package com.store.api.mongo.service;

import com.store.api.mongo.entity.OftenProduct;
import com.store.api.mongo.entity.subdocument.OrderProduct;

import java.util.List;

/**
 * 
 * Revision History
 * 
 * 2014年12月7日,vincent,created it
 */
public interface OftenProductService {
    
    /**
     * 按用户ID查询
     * @param userId
     * @return
     */
    public List<OftenProduct> findByUserId(long userId);
    
    /**
     * 按用户ID查询购买最多的TOP商品
     * @param userId
     * @param top
     * @return
     */
    public List<OftenProduct> findTopByUserId(long userId,int top);
    
    /**
     * 将商品加入到常购商品中
     * @param userId
     * @param products
     */
    public void addToOftenProduct(long userId,List<OrderProduct> products);
    
    public void save(OftenProduct entity);
    
    public void save(List<OftenProduct> entitys);
    
    public void delete(long id);
    
    public void delete(OftenProduct entity);

}
