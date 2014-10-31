package com.store.api.mongo.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.CargoAddress;

public interface CargoAddressRepository extends MongoRepository<CargoAddress, Long> {
    
    /**
     * 按类型、货主ID、经纬度查询
     * @param lng
     * @param lat
     * @return
     */
    public CargoAddress findByTypeAndCargoIdAndLngAndLat(int type,Long cargoId,Double lng,Double lat);
    
    /**
     * 按类型、货主ID、地址查询
     * @param lng
     * @param lat
     * @return
     */
    public CargoAddress findByTypeAndCargoIdAndAddressAndAddressShortAndAddressDetail(int type,Long cargoId,String address,String addressShort,String addressDetail);
    
    /**
     * 查询货主地址列表（地址使用次数>1的）
     * @param type 1:发货地址  2：收货地址
     * @param cargoId
     * @return
     */
    public Page<CargoAddress> findByTypeAndCargoIdAndUseCountGreaterThan(int type,Long cargoId,int useCount,Pageable pr);
    
}
