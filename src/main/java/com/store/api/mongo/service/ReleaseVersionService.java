package com.store.api.mongo.service;

import com.store.api.mongo.entity.ReleaseVersion;

public interface ReleaseVersionService {
	
	public void save(ReleaseVersion entity);
	
	public void delete(long id);
	
	public ReleaseVersion findMax(int type,int versionCode);
	
}
