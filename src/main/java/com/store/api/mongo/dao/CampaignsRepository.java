package com.store.api.mongo.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.Campaigns;

public interface CampaignsRepository extends MongoRepository<Campaigns, Long> {
	
	/**
	 * 查询
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Campaigns> findByStartLessThanAndEndGreaterThan(long start,long end);

}
