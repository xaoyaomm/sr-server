/**
 * OrderOffer.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年11月15日
 */
package com.store.api.mongo.entity.subdocument;

import java.io.Serializable;

/**
 * 
 * Revision History
 * 
 * 2014年11月15日,vincent,created it
 */
public class Offer implements Serializable{

    private static final long serialVersionUID = -8256242503548068133L;
    
    /** 卖家ID **/
    private long merchantsId=0L;
    
    /** 创建时间 **/
    private long createDate=0L;
    
    /** 是否有抢单动作 **/
    private boolean isAct=false;
    
    /** 抢单状态:0未抢单 1已抢到单 2被其它人抢 **/
    private int status=0;

    public long getMerchantsId() {
        return merchantsId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public int getStatus() {
        return status;
    }

    public void setMerchantsId(long merchantsId) {
        this.merchantsId = merchantsId;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public void setStatus(int status) {
        this.status = status;
    }

	public boolean isAct() {
		return isAct;
	}

	public void setAct(boolean isAct) {
		this.isAct = isAct;
	}

}
