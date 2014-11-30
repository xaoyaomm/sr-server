package com.store.api.mongo.entity;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Catalog implements Serializable{
	
	private static final long serialVersionUID = -6091301219300179699L;
	
	@Id
	private long id;
	
	/** 分类名称 **/
	private String name="";
	
	/** 排序 **/
	private long order=0L;

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

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

}
