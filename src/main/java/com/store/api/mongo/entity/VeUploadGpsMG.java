package com.store.api.mongo.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class VeUploadGpsMG implements java.io.Serializable{
    
    private static final long serialVersionUID = 3491288732256886751L;
    
    @Id
    private Long id;
    /**
     * 车主ID
     */
    @Indexed
    private Long userId;
    /**
     * 车辆ID
     */
    private Long vehicleId;
    
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 经度
     */
    private String lng;
    /**
     * 纬度
     */
    private String lat;
    /**
     * 采集时间
     */
    @Indexed
    private Long uptime;

    
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getLng() {
        return lng;
    }

    public String getLat() {
        return lat;
    }

    public Long getUptime() {
        return uptime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
    
}

