package com.store.api.common;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.api.mongo.entity.vo.AddressBean;
import com.store.api.utils.http.HttpCallResult;
import com.store.api.utils.http.HttpUtil;

public class Common {
	
	private static final Logger LOG = LoggerFactory.getLogger(Common.class);

    
    /**
     * 隐藏字符串中间部分
     * @param plate
     * @return
     */
    public static String hidePlate(String plate){
        if(null!=plate){
            int mod=plate.length()%2;
            int len=plate.length();
            if(mod>0){
                String pre=plate.substring(0,(len-mod)/2-1);
                String suf=plate.substring(len+mod-(len-mod)/2);
                int fill=plate.length()-pre.length()-suf.length();
                StringBuffer sb=new StringBuffer();
                for (int i = 0; i < fill; i++) {
                    sb.append("*");
                }
                return new StringBuffer().append(pre).append(sb).append(suf).toString();
            }else{
                String pre=plate.substring(0,len/2/2+1);
                String suf=plate.substring(len/2+len/2/2);
                int fill=plate.length()-pre.length()-suf.length();
                StringBuffer sb=new StringBuffer();
                for (int i = 0; i < fill; i++) {
                    sb.append("*");
                }
                return new StringBuffer().append(pre).append(sb).append(suf).toString();
            }
        }
        return null;
    }
    
    /**
     * 手机号码保留前3，后4，中间用*号填充
     * @param phoneNumber
     * @return
     */
    public static String formatPhoneNumber(String phoneNumber){
        if(!StringUtils.isEmpty(phoneNumber)){
            if(phoneNumber.length()>10){
                try {
                    String pre=phoneNumber.substring(0,3);
                    String tail=phoneNumber.substring(phoneNumber.length()-4);
                    return pre+"****"+tail;
                } catch (Exception e) {}
            }
        }
        return null;
    }
    
    /**
     * 计算两个坐标点之间的距离,单位为米
     * @param lng1 经度
     * @param lat1 纬度
     * @param lng2 经度
     * @param lat2 纬度
     * @return
     */
    public static long getDistance(double lng1, double lat1, double lng2, double lat2){
       double radLat1 = lat1* Math.PI / 180.0;
       double radLat2 = lat2* Math.PI / 180.0;
       double a = radLat1 - radLat2;
       double b = lng1* Math.PI / 180.0 - lng2* Math.PI / 180.0;
       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
        Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
       s = s * Constant.EARTH_RADIUS;
       s = Math.round(s * 10000) / 10000;
       return Long.parseLong(new DecimalFormat("0").format(s));
    }
    
    /**
     *通过坐标点查位置信息
     * @param lng
     * @param lat
     * @return
     */
    public static AddressBean geocoderWithBaidu(double lng, double lat) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("location", lat + "," + lng));
		params.add(new BasicNameValuePair("output", "json"));
		params.add(new BasicNameValuePair("coord_type", "gcj02"));
		params.add(new BasicNameValuePair("ak", "bvY8YkGvsYjB68wKRwAXeCPY"));
		params.add(new BasicNameValuePair("src", "storeRun"));
		String baseUrl = "http://api.map.baidu.com/geocoder";
		String param = URLEncodedUtils.format(params, "utf-8");

		HttpCallResult res = HttpUtil.get(baseUrl + "?" + param);

		if (res.getStatusCode() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			String content = res.getContent();
			try {
				JsonNode tree = mapper.readTree(content);
				JsonNode statusn = tree.path("status");
				if (null != statusn && statusn.asText().equalsIgnoreCase("OK")) {
					JsonNode component = tree.path("result").path("addressComponent");
					if (component != null) {
						AddressBean address = new AddressBean();
						if (component.has("province"))
							address.setProvince(component.get("province").asText());
						if (component.has("city"))
							address.setCity(component.get("city").asText());
						if (component.has("district"))
							address.setDistrict(component.get("district").asText());
						if (component.has("street"))
							address.setStreet(component.get("street").asText());
						JsonNode code = tree.path("result").path("cityCode");
						address.setCityCode(code.asInt(0));
						return address;
					}
				} else
					return null;

			} catch (IOException e) {
				LOG.error("call geocoderWithBaidu is fail.");
			}
		}
		return null;
	}
    
    /**
     * 通过IP查位置信息
     * @param lng
     * @param lat
     * @return
     */
    public static AddressBean ipWithBaidu(String ip) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("ip", ip));
		params.add(new BasicNameValuePair("output", "json"));
		params.add(new BasicNameValuePair("coor", "gcj02"));
		params.add(new BasicNameValuePair("ak", "bvY8YkGvsYjB68wKRwAXeCPY"));
		String baseUrl = "http://api.map.baidu.com/location/ip";
		String param = URLEncodedUtils.format(params, "utf-8");

		HttpCallResult res = HttpUtil.get(baseUrl + "?" + param);

		if (res.getStatusCode() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			String content = res.getContent();
			try {
				JsonNode tree = mapper.readTree(content);
				JsonNode statusn = tree.path("status");
				if (null != statusn && statusn.asInt()==0) {
					JsonNode detail = tree.path("content").path("address_detail");
					if (detail != null) {
						AddressBean address = new AddressBean();
						if (detail.has("province"))
							address.setProvince(detail.get("province").asText());
						if (detail.has("city"))
							address.setCity(detail.get("city").asText());
						if (detail.has("district"))
							address.setDistrict(detail.get("district").asText());
						if (detail.has("street"))
							address.setStreet(detail.get("street").asText());
						if (detail.has("city_code"))
							address.setCityCode(detail.get("city_code").asInt(0));
						return address;
					}
				} else
					return null;

			} catch (IOException e) {
				LOG.error("call ipWithBaidu is fail.");
			}
		}
		return null;
	}
    
    public static void main(String[] args){
    	AddressBean a=ipWithBaidu("183.37.240.140");
        System.out.println(a.getProvince()+"  "+a.getCity()+"  "+a.getDistrict()+"  "+a.getCityCode());
        AddressBean addr=geocoderWithBaidu(113.95072266574,22.558887751083);
        System.out.println(addr.getProvince()+"  "+addr.getCity()+"  "+addr.getDistrict()+"  "+addr.getCityCode());
    }
}
