package com.store.api.mongo.service;

import java.util.List;

import com.store.api.mongo.entity.Campaigns;

public interface CampaignsService {
	
	public void save(Campaigns entity);
	
	public void delete(Campaigns entity);
	
	public void delete(long id);
	
	public Campaigns findOne(long id);
	
	/**
	 * 查询生效的活动记录
	 * @param now
	 * @return
	 */
	public List<Campaigns> findValidData(long now);

}
