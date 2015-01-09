package com.store.api.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 热销商品
 * 
 * Revision History
 *
 * @author vincent,2015年1月9日 created it
 */
@Document
public class HotProduct implements Serializable{

	private static final long serialVersionUID = 7540228840165755771L;
	
	@Id
	private long id;
	
	/** 商品名 **/
	private String name="";
	
	/** 销售数量 **/
	private long total=0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}
