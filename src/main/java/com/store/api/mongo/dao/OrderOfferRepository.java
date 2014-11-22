/**
 * OrderOfferRepository.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年11月15日
 */
package com.store.api.mongo.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.store.api.mongo.entity.OrderOffer;

/**
 * 
 * Revision History
 * 
 * 2014年11月15日,vincent,created it
 */
public interface OrderOfferRepository extends MongoRepository<OrderOffer, Long> {
    
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
    
    /**
     * 分页查询推送给商户的订单数(top)
     * @param mercId
     * @param orderId
     * @return
     */
    public Page<OrderOffer> findByMerchantsIdAndStatusNotAndOrderIdGreaterThan(Long mercId,int status,Long orderId,Pageable pr);
    
    /**
     * 分页查询推送给商户的订单数(tail)
     * @param mercId
     * @param orderId
     * @return
     */
    public Page<OrderOffer> findByMerchantsIdAndStatusNotAndOrderIdLessThan(Long mercId,int status,Long orderId,Pageable pr);
    
    /**
     * 查询今天商户错过的订单数
     * @param id
     * @param date
     * @return
     */
    @Query(value="{'merchantsId':?0,'createDate':{$gte:?1},'status':{$ne:1}}",count=true)
    public int findTadayLostByUserId(Long id,Long date);
}
