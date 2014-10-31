package com.store.api.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.VehicleVersion;

public interface VehicleVersionRepository extends MongoRepository<VehicleVersion, Long> {

    // 根据vehicleid 查询
    public VehicleVersion findByVehicleId(Long vehicleId);

}
