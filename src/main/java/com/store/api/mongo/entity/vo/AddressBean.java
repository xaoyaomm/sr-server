package com.store.api.mongo.entity.vo;


public class AddressBean {
    
    private String province;
    
    private String city;
    
    private String district;
    
    private String street;
    
    private String streetNumber;
    
    private int cityCode;

    public int getCityCode() {
		return cityCode;
	}

	public void setCityCode(int cityCode) {
		this.cityCode = cityCode;
	}

	public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }
}
