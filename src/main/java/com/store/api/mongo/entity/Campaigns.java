package com.store.api.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 推广活动
 * 
 * Revision History
 *
 * @author vincent,2015年1月9日 created it
 */
@Document
public class Campaigns implements Serializable{

	private static final long serialVersionUID = 4418463397713692272L;
	
	@Id
	private long id;
	
	/** 活动名称 **/
	private String name="";
	
	/** banner地址 **/
	private String bannerUrl="";
	
	/** 活动页面地址 **/
	private String pageUrl="";
	
	/** 活动开始时间 **/
	private long start=0;
	
	/** 活动结束时间 **/
	private long end=0;

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

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

}
