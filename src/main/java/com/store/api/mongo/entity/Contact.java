package com.store.api.mongo.entity;

import java.io.Serializable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.store.api.mongo.entity.enumeration.UserType;

@Document
public class Contact implements Serializable {

    private static final long serialVersionUID = -1926081581821331761L;
    
    @Id
    private Long id;
    
    /** 用户类型 **/
    @Enumerated(EnumType.STRING)
    private UserType userType;

    /** 联系人手机号 **/
    private String mobile;
    
    /** 联系人名称 **/
    private String name;
    
    /** 用户ID **/
    private Long userId;
    
    /** 创建时间 **/
    private Long createDate;

    public Long getId() {
        return id;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }
    
}
