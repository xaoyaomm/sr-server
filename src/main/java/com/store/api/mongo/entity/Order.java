package com.store.api.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * Revision History
 * 
 * 2014年11月15日,vincent,created it
 */
@Document
public class Order implements Serializable{

    private static final long serialVersionUID = -4718253039843718803L;
    
    @Id
    private Long id;
    
    /** 买家ID **/
    private Long customerId=0L;
    
    /** 卖家ID **/
    private Long merchantsId=0L;
    
    /** 买家名称 **/
    private String customerName="";
    
    /** 卖家名称 **/
    private String merchantsName="";
    
    /** 买家电话 **/
    private String customerPhone="";
    
    /** 卖家电话 **/
    private String merchantsPhone="";
    
    /** 订单状态:0创建 1已抢单 2送货中 4已送达 6已确认 10取消 **/
    private int status=0;
    
    /** 订单创建时间 **/
    private Long createDate=0L;
    
    /** 成单时间 **/
    private Long offerDate=0L;
    
    /** 送达时间 **/
    private Long arrivedDate=0L;
    
    /** 确认收货时间 **/
    private Long confirmDate=0L;
    
    /** 取消时间 **/
    private Long cancelDate=0L;
    
    /** 订单总价 **/
    private Double totalPrice=0D;
    
    /** 卖家地址 **/
    private String fromAddress="";
    
    /** 买家地址 **/
    private String toAddress="";
    
    /** 卖家位置 [0]纬度  [1]经度**/
    private Double[] fromLocation={0D,0D};
    
    /** 买家位置 [0]纬度  [1]经度**/
    private Double[] toLocation={0D,0D};

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getMerchantsId() {
        return merchantsId;
    }

    public int getStatus() {
        return status;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public Long getOfferDate() {
        return offerDate;
    }

    public Long getArrivedDate() {
        return arrivedDate;
    }

    public Long getConfirmDate() {
        return confirmDate;
    }

    public Long getCancelDate() {
        return cancelDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public Double[] getFromLocation() {
        return fromLocation;
    }

    public Double[] getToLocation() {
        return toLocation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setMerchantsId(Long merchantsId) {
        this.merchantsId = merchantsId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public void setOfferDate(Long offerDate) {
        this.offerDate = offerDate;
    }

    public void setArrivedDate(Long arrivedDate) {
        this.arrivedDate = arrivedDate;
    }

    public void setConfirmDate(Long confirmDate) {
        this.confirmDate = confirmDate;
    }

    public void setCancelDate(Long cancelDate) {
        this.cancelDate = cancelDate;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public void setFromLocation(Double[] fromLocation) {
        this.fromLocation = fromLocation;
    }

    public void setToLocation(Double[] toLocation) {
        this.toLocation = toLocation;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getMerchantsName() {
        return merchantsName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getMerchantsPhone() {
        return merchantsPhone;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setMerchantsName(String merchantsName) {
        this.merchantsName = merchantsName;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public void setMerchantsPhone(String merchantsPhone) {
        this.merchantsPhone = merchantsPhone;
    }

}
