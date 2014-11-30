package com.store.api.mongo.entity.subdocument;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderProduct implements Serializable{

    private static final long serialVersionUID = -3355002394944455433L;
    
    /** 产品ID **/
    @JsonProperty("p_id")
    private Long productId=0L;
    
    /** 产品名称 **/
    private String productName="";
    
    /** 产品单价 **/
    private Long productPrice=0L;
    
    /** 购买数量 **/
    @JsonProperty("p_num")
    private int amount=0;
    
    /** 产品图片 **/
    private String productImg="";

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public int getAmount() {
        return amount;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(Long productPrice) {
        this.productPrice = productPrice;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

}
