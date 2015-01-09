package com.store.api.mongo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.CampaignsRepository;
import com.store.api.mongo.entity.Campaigns;
import com.store.api.mongo.service.CampaignsService;
import com.store.api.mongo.service.SequenceService;

@Service
public class CampaignsServiceImpl implements CampaignsService {
	
	@Autowired
	private CampaignsRepository repository;
	
	@Autowired
    private SequenceService sequenceService;

	@Override
	public void save(Campaigns entity) {
		if (0 == entity.getId()) {
            entity.setId(this.sequenceService.getNextSequence(entity));
        }
        repository.save(entity);
	}

	@Override
	public void delete(Campaigns entity) {
		repository.delete(entity);
	}

	@Override
	public void delete(long id) {
		repository.delete(id);
	}

	@Override
	public Campaigns findOne(long id) {
		return repository.findOne(id);
	}

	@Override
	public List<Campaigns> findValidData(long now) {
		return repository.findByStartLessThanAndEndGreaterThan(now, now);
	}

}
