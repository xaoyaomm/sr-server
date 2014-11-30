package com.store.api.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Area implements Serializable{

	private static final long serialVersionUID = 3280817503409191093L;
	
	@Id
	private long id;
	
	/** 父ID **/
	private long parentId;
	
	/** 区域名称 **/
	private String title="";
	
	/** 区域类型 0顶级 1 1级 2 2级 **/
	private int type=0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
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
