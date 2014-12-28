package com.store.api.mongo.entity;

import java.io.Serializable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.store.api.mongo.entity.enumeration.UserType;

@Document
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	private long id;
	
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
	
	/** 店铺编号,推广使用 **/
	private long mercNum=0;
	
	/** 推荐人ID **/
	private long recUserId=0L;
	
	/** 推广码 **/
	private String promoCode="";
	
	/** 地址 **/
	private String address="";
	
	/** 坐标点[0]经度  [1]纬度 **/
	@GeoSpatialIndexed(type=GeoSpatialIndexType.GEO_2DSPHERE ,name="location_2ds")
	private double[] location={0D,0D};
	
	/** 注册版本 **/
	private String registerVer="";
	
	/** 当前版本 **/
	private String currVer="";
	
	/** imei **/
	private String imei="";
	
	/** 地址ID **/
	private long addressId=0L;
	
	/** UUID **/
	private String uuid="";
	
	/** 状态 1有效  0无效 **/
	private int status=1;
	
	/** 帐号创建时间 **/
    private long createTime=System.currentTimeMillis();
	
	/** 最后使用时间 **/
	private long lastUserTime=System.currentTimeMillis();
	
	/** 注册地城市代码 **/
	private int cityCode=0;
	
	/** 注册地省份 **/
	private String province="";
	
	/** 注册地城市 **/
	private String city="";
	
	public long getLastUserTime() {
        return lastUserTime;
    }

    public void setLastUserTime(long lastUserTime) {
        this.lastUserTime = lastUserTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public long getRecUserId() {
		return recUserId;
	}

	public void setRecUserId(long recUserId) {
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

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
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

    public long getAddressId() {
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

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCityCode() {
		return cityCode;
	}

	public void setCityCode(int cityCode) {
		this.cityCode = cityCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public long getMercNum() {
		return mercNum;
	}

	public void setMercNum(long mercNum) {
		this.mercNum = mercNum;
	}

}
