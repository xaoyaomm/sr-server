package com.store.api.mongo.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;


public class UploadGpsGroup implements Serializable{

    private static final long serialVersionUID = 1064727841146044926L;
    
    private Long first;
    
    private Long last;
    
    private Long userId;
    
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getFirst() {
        return first;
    }

    public Long getLast() {
        return last;
    }

    public Long getUserId() {
        return userId;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public void setLast(Long last) {
        this.last = last;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

 
}
