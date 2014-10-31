package com.store.api.mongo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.VehicleVersionRepository;
import com.store.api.mongo.entity.Contact;
import com.store.api.mongo.entity.VehicleVersion;
import com.store.api.mongo.service.SequenceService;
import com.store.api.mongo.service.VehicleVersionService;

@Service
public class VehicleVersionServiceImpl implements VehicleVersionService {
    
    @Autowired
    private SequenceService sequenceService;
    
    @Autowired
    private VehicleVersionRepository repository;

    @Override
    public VehicleVersion findByVehicleId(Long vehicleId) {
        return repository.findByVehicleId(vehicleId);
    }

    @Override
    public void save(VehicleVersion entity) {
        if (null == entity.getId()) {
            entity.setId(this.sequenceService.getNextSequence(entity));
        }
        repository.save(entity);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void save(List<VehicleVersion> entitys) {
        for (VehicleVersion entity : entitys) {
            if (null == entity.getId()) {
                entity.setId(sequenceService.getNextSequence(entity));
            }
        }
        repository.save(entitys);
    }

}
