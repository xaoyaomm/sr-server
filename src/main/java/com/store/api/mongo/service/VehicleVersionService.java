package com.store.api.mongo.service;

import java.util.List;

import com.store.api.mongo.entity.VehicleVersion;

public interface VehicleVersionService {
    
    // 根据vehicleid 查询
    public VehicleVersion findByVehicleId(Long vehicleId);

    // 保存数据
    public void save(VehicleVersion entity);
    
    //批量保存数据
    public void save(List<VehicleVersion> entitys);
    
    public void deleteAll();

}
