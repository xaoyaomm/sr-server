package com.store.api.mongo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.store.api.mongo.entity.subdocument.Offer;
import com.store.api.mongo.entity.subdocument.OrderProduct;

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
    private long id;
    
    /** 买家ID **/
    private long customerId=0L;
    
    /** 卖家ID **/
    private long merchantsId=0L;
    
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
    private long createDate=0L;
    
    /** 成单时间 **/
    private long offerDate=0L;
    
    /** 送达时间 **/
    private long arrivedDate=0L;
    
    /** 确认收货时间 **/
    private long confirmDate=0L;
    
    /** 取消时间 **/
    private long cancelDate=0L;
    
    /** 订单总价 **/
    private long totalPrice=0L;
    
    /** 商品总数量 **/
    private int totalAmount=0;
    
    /** 购买商品描述 **/
    private String prosDesc="";
    
    /** 卖家地址 **/
    private String fromAddress="";
    
    /** 买家地址 **/
    private String toAddress="";
    
    /** 卖家位置 [0]纬度  [1]经度**/
    private double[] fromLocation={0D,0D};
    
    /** 买家位置 [0]纬度  [1]经度**/
    private double[] toLocation={0D,0D};
    
    /** 报价信息**/
    private List<Offer> offers=new ArrayList<Offer>();
    
    /** 购买产品列表**/
    private List<OrderProduct> products=new ArrayList<OrderProduct>();

    public long getId() {
        return id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public long getMerchantsId() {
        return merchantsId;
    }

    public int getStatus() {
        return status;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getOfferDate() {
        return offerDate;
    }

    public long getArrivedDate() {
        return arrivedDate;
    }

    public long getConfirmDate() {
        return confirmDate;
    }

    public long getCancelDate() {
        return cancelDate;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public double[] getFromLocation() {
        return fromLocation;
    }

    public double[] getToLocation() {
        return toLocation;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public void setMerchantsId(long merchantsId) {
        this.merchantsId = merchantsId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public void setOfferDate(long offerDate) {
        this.offerDate = offerDate;
    }

    public void setArrivedDate(long arrivedDate) {
        this.arrivedDate = arrivedDate;
    }

    public void setConfirmDate(long confirmDate) {
        this.confirmDate = confirmDate;
    }

    public void setCancelDate(long cancelDate) {
        this.cancelDate = cancelDate;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setFromLocation(double[] fromLocation) {
        this.fromLocation = fromLocation;
    }

    public void setToLocation(double[] toLocation) {
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

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getProsDesc() {
		return prosDesc;
	}

	public void setProsDesc(String prosDesc) {
		this.prosDesc = prosDesc;
	}

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }

}
