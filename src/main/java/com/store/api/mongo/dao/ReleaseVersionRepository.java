package com.store.api.mongo.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.ReleaseVersion;

public interface ReleaseVersionRepository extends MongoRepository<ReleaseVersion, Long> {
	
	public Page<ReleaseVersion> findByClientTypeAndVersionCodeGreaterThan(int clientType,int versionCode,Pageable pr);

}
