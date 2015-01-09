package com.store.api.test.dao;

import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mongo.dao.CampaignsRepository;

public class CampaignsRepositoryTestCase extends BaseDaoTestCase {
	
	@Autowired
	CampaignsRepository dao;
	
}
