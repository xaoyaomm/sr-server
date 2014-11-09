package com.store.api.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Address implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	
	/** 用户ID **/
	private Long userId=0L;
	
	/** 地址信息 **/
	private String address="";
	
	/** 坐标点[0]经度  [1]纬度 **/
	private Double[] location={0D,0D};

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double[] getLocation() {
        return location;
    }

    public void setLocation(Double[] location) {
        this.location = location;
    }

}
