package com.store.api.mongo.entity;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Catalog implements Serializable{
	
	private static final long serialVersionUID = -6091301219300179699L;
	
	@Id
	private Long id;
	
	/** 分类名称 **/
	private String name="";
	
	/** 排序 **/
	private Long order=0L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

}
