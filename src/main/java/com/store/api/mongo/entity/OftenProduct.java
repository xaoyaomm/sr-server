/**
 * OftenProduct.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年12月7日
 */
package com.store.api.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 常购商品
 * 
 * Revision History
 * 
 * 2014年12月7日,vincent,created it
 */
@Document
public class OftenProduct implements Serializable{

    private static final long serialVersionUID = -4717580517222363809L;
    
    @Id
    private long id;
    
    /** 商品ID **/
    private long productId=0;
    
    /** 用户ID **/
    private long userId=0;
    
    /** 购买次数 **/
    private int num=0;

    public long getId() {
        return id;
    }

    public long getProductId() {
        return productId;
    }

    public int getNum() {
        return num;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}
