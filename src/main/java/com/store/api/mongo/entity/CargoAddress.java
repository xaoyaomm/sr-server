package com.store.api.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CargoAddress implements Serializable{

    private static final long serialVersionUID = -6313678101102730858L;
    
    @Id
    private Long id;

    private Long cargoId;
    
    private String address;
    
    private String addressShort;
    
    private String addressDetail;
    
    private Double lng;
    
    private Double lat;
    
    private int useCount=0;
    
    //类型   1：发货地址     2：收货地址
    private int type=0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public Long getId() {
        return id;
    }

    public Long getCargoId() {
        return cargoId;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressShort() {
        return addressShort;
    }

    public void setAddressShort(String addressShort) {
        this.addressShort = addressShort;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public Double getLng() {
        return lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
    

}
