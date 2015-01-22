package com.store.api.mongo.service;

import com.store.api.mongo.entity.ReleaseVersion;
import com.store.api.mongo.entity.enumeration.UserType;

public interface ReleaseVersionService {
	
	public void save(ReleaseVersion entity);
	
	public void delete(long id);
	
	public ReleaseVersion findMax(UserType type,int versionCode);
	
}
