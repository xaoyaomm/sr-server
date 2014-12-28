package com.store.api.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ReleaseVersion implements Serializable{
	
	private static final long serialVersionUID = 1188278355128085148L;

	@Id
	private long id;
	
	/** 客户端类型  1:用户端  2:商户端 **/
	private int clientType=1;
	
	/** 版本号CODE **/
	private int versionCode=0;
	
	/** 版本号 **/
	private String versionCodeName="";
	
	/** 是否强制更新 **/
	private boolean mustUpdate=false;

	/** 下载地址 **/
	private String downloadUrl="";
	
	/** 版本描述 **/
	private String desc="";
	
	/** 创建时间 **/
	private long createDate=0L;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getClientType() {
		return clientType;
	}

	public void setClientType(int clientType) {
		this.clientType = clientType;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionCodeName() {
		return versionCodeName;
	}

	public void setVersionCodeName(String versionCodeName) {
		this.versionCodeName = versionCodeName;
	}

	public boolean isMustUpdate() {
		return mustUpdate;
	}

	public void setMustUpdate(boolean mustUpdate) {
		this.mustUpdate = mustUpdate;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
}
