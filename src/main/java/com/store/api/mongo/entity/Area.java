package com.store.api.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Area implements Serializable{

	private static final long serialVersionUID = 3280817503409191093L;
	
	@Id
	private Long id;
	
	/** 父ID **/
	private Long parentId;
	
	/** 区域名称 **/
	private String title="";
	
	/** 区域类型 0顶级 1 1级 2 2级 **/
	private int type=0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	

}
