/**
 * OrderOffer.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年11月15日
 */
package com.store.api.mongo.entity;

import java.io.Serializable;

/**
 * 
 * Revision History
 * 
 * 2014年11月15日,vincent,created it
 */
public class OrderOffer implements Serializable{

    private static final long serialVersionUID = -8256242503548068133L;
    
    private Long id;
    
    /** 订单ID **/
    private Long orderId=0L;
    
    /** 卖家ID **/
    private Long merchantsId=0L;
    
    /** 创建时间 **/
    private Long createDate=0L;
    
    /** 抢单状态:0未抢单 1已抢单 2被其它人抢 **/
    private int status=0;

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMerchantsId() {
        return merchantsId;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public int getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setMerchantsId(Long merchantsId) {
        this.merchantsId = merchantsId;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
