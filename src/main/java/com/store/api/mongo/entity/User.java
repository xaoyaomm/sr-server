package com.store.api.mongo.entity;

import java.io.Serializable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.store.api.mongo.entity.enumeration.UserType;

@Document
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	
	/** 用户名 **/
	private String userName="";
	
	/** 昵称 **/
	private String nickName="";
	
	/** 密码 **/
	private String pwd="";
	
	/** 手机号 **/
	private String phone="";
	
	/** 用户类型 **/
	@Enumerated(EnumType.STRING)
	private UserType type;
	
	/** 推荐人ID **/
	private Long recUserId=0L;
	
	/** 推广码 **/
	private String promoCode="";
	
	/** 地址 **/
	private String address="";
	
	/** 坐标点[0]经度  [1]纬度 **/
	@Indexed
	private Double[] location={0D,0D};
	
	/** 注册版本 **/
	private String registerVer="";
	
	/** 当前版本 **/
	private String currVer="";
	
	/** imei **/
	private String imei="";
	
	/** 地址ID **/
	private Long addressId=0L;
	
	/** UUID **/
	private String uuid="";
	
	/** 帐号创建时间 **/
    private Long createTime=System.currentTimeMillis();
	
	/** 最后使用时间 **/
	private Long lastUserTime=System.currentTimeMillis();
	
	public Long getLastUserTime() {
        return lastUserTime;
    }

    public void setLastUserTime(Long lastUserTime) {
        this.lastUserTime = lastUserTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public Long getRecUserId() {
		return recUserId;
	}

	public void setRecUserId(Long recUserId) {
		this.recUserId = recUserId;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

    public Double[] getLocation() {
        return location;
    }

    public void setLocation(Double[] location) {
        this.location = location;
    }

    public String getRegisterVer() {
        return registerVer;
    }

    public String getCurrVer() {
        return currVer;
    }

    public String getImei() {
        return imei;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setRegisterVer(String registerVer) {
        this.registerVer = registerVer;
    }

    public void setCurrVer(String currVer) {
        this.currVer = currVer;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
