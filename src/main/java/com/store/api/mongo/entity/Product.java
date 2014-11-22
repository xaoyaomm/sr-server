package com.store.api.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Product implements Serializable{

	private static final long serialVersionUID = 6064717319673556320L;
	
	@Id
	private Long id;
	
	/** 产品名称 **/
	private String name="";
	
	/** 产品单价(分) **/
	private Long price=0L;
	
	/** 产品图片地址 **/
	private String imgUrl="";
	
	/** 产品类别 **/
	private Long catalogId=0L;
	
	/** 产品所属于区域 **/
	private int areaId=0;
	
	/** 产品排序 **/
	private Long order=0L;
	
	/** 产品状态 1新增 2修改 3删除 **/
	private int status=1;
	
	/** 产品版本 **/
	@Indexed
	private Long ver=0L;

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

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getVer() {
		return ver;
	}

	public void setVer(Long ver) {
		this.ver = ver;
	}
	
	

}
