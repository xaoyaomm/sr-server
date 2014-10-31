package com.store.api.mongo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.store.api.mongo.entity.CargoAddress;

public interface CargoAddressService {
    
    /**
     * 按货主ID、经纬度查询
     * @param type 1:发货地址  2：收货地址
     * @param cargoId
     * @param lng
     * @param lat
     * @return
     */
    public CargoAddress findByCargoIdAndLngAndLat(int type,Long cargoId,Double lng,Double lat);
    
    /**
     * 按类型、货主ID、地址查询
     * @param type 1:发货地址  2：收货地址
     * @param cargoId
     * @return
     */
    public CargoAddress findByTypeAndCargoIdAndAddress(int type,Long cargoId,String address,String addressShort,String addressDetail);
    
    /**
     * 查询货主地址列表（地址使用次数>1的）
     * @param type 1:发货地址  2：收货地址
     * @param cargoId
     * @return
     */
    public Page<CargoAddress> findByCargoId(int type,Long cargoId);
    
    /**
     * 保存对象
     * @param entity
     */
    public void save(CargoAddress entity);
    
    /**
     * 删除对象
     * @param entity
     */
    public void remove(CargoAddress entity);
    
    /**
     * 删除对象
     * @param id
     */
    public void remove(Long id);
    
    /**
     * 按ID查找
     * @param id
     * @return
     */
    public CargoAddress findOne(Long id);
    
}
