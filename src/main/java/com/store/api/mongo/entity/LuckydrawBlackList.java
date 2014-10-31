package com.store.api.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 抽奖黑名单
 * 
 * Revision History
 * 
 * 2014年9月11日,vincent,created it
 */
@Document
public class LuckydrawBlackList implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id
    private Long id;
    
    private Long userId;
    
    private String mobile;
    
    private String name;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

}
