package com.store.api.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Address implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	private long id;
	
	/** 用户ID **/
	private long userId=0L;
	
	/** 地址信息 **/
	private String address="";
	
	/** 坐标点[0]经度  [1]纬度 **/
	private double[] location={0D,0D};
	
	/** 联系人姓名 **/
	private String name="";
	
	/** 联系人手机号 **/
	private String phone="";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

}
