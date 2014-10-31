package com.store.api.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 车主客户端版本
 * 
 * Revision History
 * 
 * 2014年9月16日,vincent,created it
 */
@Document
public class VehicleVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private Long vehicleId;

    private String version;

    public Long getId() {
        return id;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public String getVersion() {
        return version;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
