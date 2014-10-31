package com.store.api.mongo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.VeUploadGpsMGDao;
import com.store.api.mongo.dao.VeUploadGpsMGRepository;
import com.store.api.mongo.entity.UploadGpsGroup;
import com.store.api.mongo.entity.VeUploadGpsMG;
import com.store.api.mongo.service.SequenceService;
import com.store.api.mongo.service.VeUploadGpsMGService;

@Service
public class VeUploadGpsMGServiceImpl implements VeUploadGpsMGService {

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private VeUploadGpsMGRepository repository;

    @Autowired
    private VeUploadGpsMGDao dao;

    @Override
    public void save(VeUploadGpsMG entity) {
        if (null == entity.getId()) {
            entity.setId(this.sequenceService.getNextSequence(entity));
        }
        repository.save(entity);
    }

    @Override
    public void save(List<VeUploadGpsMG> items) {
        if (null != items && items.size() > 0) {
            for (VeUploadGpsMG gps : items) {
                if (null == gps.getId()) {
                    gps.setId(this.sequenceService.getNextSequence(gps));
                }
            }
            repository.save(items);
        }
    }

    @Override
    public List<VeUploadGpsMG> findByUserIdAndUptimeBetweenOrderByIdAsc(Long userId, Long freTime, Long arrTime) {
        return repository.findByUserIdAndUptimeBetweenOrderByIdAsc(userId, freTime - 1, arrTime + 1);
    }

    @Override
    public List<VeUploadGpsMG> findByUserIdAndUptimeBetweenOrderByUptimeDesc(Long userId, Long freTime, Long arrTime) {
        return repository.findByUserIdAndUptimeBetweenOrderByUptimeDesc(userId, freTime - 1, arrTime + 1);
    }

    @Override
    public VeUploadGpsMG findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<UploadGpsGroup> groupUploadGpsWithId(Long firstDate, Long lastDate) {
        return dao.groupUploadGpsWithId(firstDate, lastDate);
    }

	@Override
	public List<UploadGpsGroup> getUploadGpsGroupByIdAndTime(Long firstDate,
			Long lastDate, Long userId) {
		return dao.getUploadGpsGroupByIdAndTime(firstDate, lastDate, userId);
	}

}
