package com.store.api.mongo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.CargoAddressRepository;
import com.store.api.mongo.entity.CargoAddress;
import com.store.api.mongo.service.CargoAddressService;
import com.store.api.mongo.service.SequenceService;

@Service
public class CargoAddressServiceImpl implements CargoAddressService {
    
    @Autowired
    private SequenceService sequenceService=null;
    
    @Autowired
    private CargoAddressRepository repository=null;

    @Override
    public CargoAddress findByCargoIdAndLngAndLat(int type,Long cargoId, Double lng, Double lat) {
        return repository.findByTypeAndCargoIdAndLngAndLat(type,cargoId, lng, lat);
    }

    @Override
    public Page<CargoAddress> findByCargoId(int type,Long cargoId) {
        PageRequest pr=new PageRequest(0, 200, Direction.DESC, "useCount","id");
        return repository.findByTypeAndCargoIdAndUseCountGreaterThan(type,cargoId, 1,pr);
    }

    @Override
    public void save(CargoAddress entity) {
        if(null==entity.getId()){
            entity.setId(this.sequenceService.getNextSequence(entity));
        }
        repository.save(entity);
        
    }

    @Override
    public void remove(CargoAddress entity) {
        repository.delete(entity);
    }

    @Override
    public void remove(Long id) {
        repository.delete(id);
    }

    @Override
    public CargoAddress findByTypeAndCargoIdAndAddress(int type, Long cargoId, String address, String addressShort, String addressDetail) {
        return repository.findByTypeAndCargoIdAndAddressAndAddressShortAndAddressDetail(type, cargoId, address, addressShort, addressDetail);
    }

    @Override
    public CargoAddress findOne(Long id) {
        return repository.findOne(id);
    }

}
