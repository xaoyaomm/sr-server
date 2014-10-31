package com.store.api.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.store.api.utils.Utils;

public class Common {

    /**
     * 评价等级
     * @param score
     * @return
     */
    public static int getCarCommentGrade(Long score){
        if(null==score || score <= 0){
            return 0;
        }
        else if(score <= 20 && score > 0){
            return 1;
        }
        else if(score <= 60 && score > 20){
            return 2;
        }
        else if(score <= 150 && score > 60){
            return 3;
        }
        else if(score <= 300 && score > 150){
            return 4;
        }
        else{
            return 5;
        }
    }
    
    /**
     * 车辆运营类型
     * @param type
     * @return
     */
    public static String getCarAdeptCargo(String type){
        int id=0;
        if(!StringUtils.isEmpty(type) && StringUtils.isNumeric(type))
            id=Integer.parseInt(type);
        switch (id){
        case 1:
            return "搬家";
        case 2:
            return "农副产品";
        case 3:
            return "水产品";
        case 4:
            return "小商品";
        case 5:
            return "建材";
        case 6:
            return "化工品";
        case 7:
            return "特殊品";
        case 8:
            return "其他";
        default:
            return "";                   
        }
    }
    
    
    /**
     * 奖品
     * @param type
     * @return
     */
    public static String getprize(String type){
        int id=0;
        if(!StringUtils.isEmpty(type) && StringUtils.isNumeric(type))
            id=Integer.parseInt(type);
        switch (id){
        case 1:
            return "手机支架";
        case 2:
            return "移动电源";
        case 3:
            return "50元话费";
        case 4:
            return "100元油卡";
        case 5:
            return "500元油卡";
        case 6:
            return "1000元油卡";
        case 7:
            return "50积分";
        case 8:
            return "100积分";
        case 9:
            return "200积分";
        case 10:
            return "500积分";
        case 11:
            return "1000积分";
        default:
            return "";                   
        }
    }
    
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
    public static double getDistance(double lng1, double lat1, double lng2, double lat2){
       double EARTH_RADIUS = 6378137;
       double radLat1 = lat1* Math.PI / 180.0;
       double radLat2 = lat2* Math.PI / 180.0;
       double a = radLat1 - radLat2;
       double b = lng1* Math.PI / 180.0 - lng2* Math.PI / 180.0;
       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
        Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
       s = s * EARTH_RADIUS;
       s = Math.round(s * 10000) / 10000;
       return s;
    }
    
    /**
     * 从USER-AGENT中解析客户端版本号
     * @param userAgent
     * @return
     */
    public static String getVersionWithUserAgent(String userAgent){
        if(Utils.isEmpty(userAgent))
            return null;
        String[] fields =userAgent.split(",");
        for (String field : fields) {
            if(field.contains("versionName="))
                return field.replace("versionName=", "").trim();
        }
        return null;
    }
    
    public static String getModelDesc(String type){
    	int id = 0;
    	if(!StringUtils.isEmpty(type) && StringUtils.isNumeric(type))
            id=Integer.parseInt(type);
    	switch (id) {
		case 1:
			return "微型货车";
		case 2:
			return "轻型货车";
		case 3:
			return "中型货车";
		case 5:
			return "微型面包车";
		case 6:
			return "中型面包车";
		default:
			return "";
		}
    }
    
    
    public static List<Map<String,String>> getCarType(){
    	List<Map<String,String>> carTypeList = new ArrayList<Map<String,String>>();
    	//面包车
    	Map<String,String>  minibusMap = new HashMap<String, String>();
    	minibusMap.put("name", "面包车");
    	minibusMap.put("value", "5,6");
    	minibusMap.put("description", "微型面包车,中型面包车");
    	Map<String,String>  truck4_2Map = new HashMap<String, String>();
    	truck4_2Map.put("name", "4.2米及以下厢式货车");
    	truck4_2Map.put("value", "1,2");
    	truck4_2Map.put("description", "微型货车,轻型货车");
    	Map<String,String> truck6_2Map =  new HashMap<String,String>();
    	truck6_2Map.put("name", "6.2米及以上厢式货车");
    	truck6_2Map.put("value", "3");
    	truck4_2Map.put("description", "中型货车");
    	carTypeList.add(minibusMap);
    	carTypeList.add(truck4_2Map);
    	carTypeList.add(truck6_2Map);
    	return carTypeList;
    }
    
    
    public static void main(String[] args){
    	String needModel = "5,6,1,2,3,";
    	String modelStr = "";
    	if (!Utils.isEmpty(needModel)) {
    		String[] models = needModel.split(",");
    		List<Map<String,String>> carModelList = Common.getCarType();
    		for (String str : models) {
    			for (Map<String, String> map : carModelList) {
                	if(map.get("value").contains(str) && !modelStr.contains(map.get("name"))){
                		modelStr += map.get("name")+",";
                	}
    			}
			}
    		modelStr = modelStr.substring(0, modelStr.length()-1);
    	}
         System.out.println(modelStr);
    }
}
