package com.store.api.common;

import org.apache.commons.lang3.StringUtils;

public class Common {

    
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
       double radLat1 = lat1* Math.PI / 180.0;
       double radLat2 = lat2* Math.PI / 180.0;
       double a = radLat1 - radLat2;
       double b = lng1* Math.PI / 180.0 - lng2* Math.PI / 180.0;
       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
        Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
       s = s * Constant.EARTH_RADIUS;
       s = Math.round(s * 10000) / 10000;
       return s;
    }
    public static void main(String[] args){
    }
}
