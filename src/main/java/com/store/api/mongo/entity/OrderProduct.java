package com.store.api.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document
public class OrderProduct implements Serializable{

    private static final long serialVersionUID = -3355002394944455433L;
    
    @Id
    private Long id;
    
    /** 订单ID **/
    private Long orderId=0L;
    
    /** 产品ID **/
    @JsonProperty("p_id")
    private Long productId=0L;
    
    /** 产品名称 **/
    private String productName="";
    
    /** 产品单价 **/
    private Double productPrice=0D;
    
    /** 购买数量 **/
    @JsonProperty("p_num")
    private int amount=0;
    
    /** 产品图片 **/
    private String productImg="";

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public int getAmount() {
        return amount;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

}
