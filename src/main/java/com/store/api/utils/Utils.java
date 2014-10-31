package com.store.api.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.jdbc.StringUtils;

public class Utils {
	
	/**
	 * 验证手机号码的有效性
	 * 
	 */
    public static boolean checkMobile(String mobile){
        if(isEmpty(mobile))
            return false;
        return Pattern.matches("^1([3|4|5|8][0-9]{9})+$",mobile);
    }
    
    /**
     * 验证订单号码的有效性
     * 
     */
    public static boolean checkOrderId(String orderId){
        if(isEmpty(orderId))
            return false;
    	return Pattern.matches("^([0-9])+$", orderId);
    }
    
   
    /**
     * 验证价格的有效性
     * @param price
     * @return
     */
    public static boolean checkPrice(String price) {
        if(isEmpty(price))
            return false;
    	return Pattern.matches("^([0-9])+(\\.[0-9]{1,2})*+$", price);
    }
    
    public static boolean checkUserName(String userName){
        if(isEmpty(userName))
            return false;
        return Pattern.matches("^([a-zA-Z0-9_\\x{4e00}-\\x{9fa5}]{2,30})+", userName);
    }
    
    /**
     * 评价等级
     */
    public static int getGrade(long score){
    	if(score <= 0){
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
     * double 保留两位小数
     * @param d
     * @return
     */
    public static String ceil(Double d){
    	DecimalFormat df = new DecimalFormat("0.00");
    	return df.format(d);
    }
    
    /**
     * 按年/月/日/时拼装文件地址
     * @param basePath
     * @return
     */
    public static String buildFilePath(String basePath){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        StringBuffer sb=new StringBuffer();
        sb.append(basePath);
        if(basePath.endsWith("/")||basePath.endsWith("\\"))
            sb.append(year);
        else
            sb.append("/").append(year);
        sb.append("/").append(month);
        sb.append("/").append(day);
        sb.append("/").append(hour).append("/");
        return sb.toString();
    }
    
    /**
     * 格式化日期
     * @param date
     * @param format 默认格式为‘yyyy-MM-dd HH:mm’
     * @return
     */
    public static String formatDate(Date date,String format){
        SimpleDateFormat sm;
        if(isEmpty(format))
            sm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        else
            sm = new SimpleDateFormat(format);
        return sm.format(date);
    }
    
    /**
     * 将"null"字符串或者null值转换成""
     * 
     * @param str
     * @return
     */
    public static String nullStringToEmptyString(String str) {
        if (str == null) {
            str = "";
        }
        if (str.equalsIgnoreCase("null")) {
            str = "";
        }
        return str.trim();
    }
    
    /**
     * 是否是数字
     * 
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        str = nullStringToEmptyString(str);
        String regex = "\\d+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        boolean validate = m.matches();
        return validate;
    }
    
    public static boolean isNumberOrFloat(String str) {
        str = nullStringToEmptyString(str);
        String regex = "\\d+\\.?\\d+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        boolean validate = m.matches();
        return validate;
    }
    
    /**
     * 检测字符串是否为空，或者空字符串
     * 
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        str = nullStringToEmptyString(str);
        String regex = "\\s*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        boolean validate = m.matches();
        return validate;
    }
    
    public static String listToString(List<String> list){
        StringBuffer sb=new StringBuffer();
        if(null!=list && list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                if(i>0)
                    sb.append(",");
                sb.append(list.get(i));
            }
        }
        return sb.toString();
    }
    
    /**
     * * 保留小数
     * @param args
     *
     * @param dbStr   数字字符串
     * @param size    保留位数
     * @return
     */
     
    public static String doubleToSize(String dbStr,int size){
    	BigDecimal   bd   =   new   BigDecimal(dbStr);  
    	bd   =   bd.setScale(size,BigDecimal.ROUND_HALF_UP);   
    	return bd+"";
    }
    

    
    public static String format(String str){
    	if(StringUtils.isNullOrEmpty(str)){
    		return "未知";
    	}
    	
    	for (int i = 0; i < str.length(); i++){
    		   if (!Character.isDigit(str.charAt(i))){
    			   return "未知";
    		   }
    	}
    	return str+"米";
    }
    public static Integer[] stringToIntegerArray(String str){
    	 String[] modelStr = str.split(",");
         Integer[] models = new Integer[modelStr.length];
         for(int i = 0 ;i<modelStr.length;i++){
         	models[i] = Integer.valueOf(modelStr[i]);
         }
         return models;
    }
    
    
    /**
 	 * 过滤非中文,英文数字外的其它字符
 	 * 
 	 * @param str
 	 * @return
 	 */
 	public static String isChinessNumEnglish(String str) {
 		char[] chs = str.toCharArray();
 		StringBuffer sb = new StringBuffer();
 		for (int i = 0; i < chs.length; i++) {
 			if (String.valueOf(chs[i]).matches("[0-9]|[a-z]|[A-Z]")
 					|| String.valueOf(chs[i]).matches("[\u4e00-\u9fa5]")) {
 				sb.append(String.valueOf(chs[i]));
 			}
 		}
 		return sb.toString();
 	}
 	
 	
 	private static double EARTH_RADIUS = 6378137;
 	private static double rad(double d)
 	{
 	   return d * Math.PI / 180.0;
 	}

 	public static double GetDistance(Double lat1, Double lng1, Double lat2, Double lng2)
 	{
 	   if(lat1 == null || lng1 == null || lat2 == null || lng2 == null){
 		   return 0;
 	   }
 	   double radLat1 = rad(lat1);
 	   double radLat2 = rad(lat2);
 	   double a = radLat1 - radLat2;
 	   double b = rad(lng1) - rad(lng2);
 	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
 	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
 	   s = s * EARTH_RADIUS;
 	   s = Math.round(s * 10000) / 10000;
 	   return s;
 	}
 	
 	private static final int MAX_MATCH_NUMBER = 1;
	private static final String MATCH_REG = ".\\d路;";

	public static boolean isBusLines(String str) {
		Pattern pattern = Pattern.compile(MATCH_REG);
		Matcher matcher = pattern.matcher(str);
//		System.out.println(matcher.groupCount());
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		return count >= MAX_MATCH_NUMBER;
	}
 	
 	
 	public static void main(String[] args) {
 		System.out.println(GetDistance(113.676795,22.941322,114.091837,22.547244));
 		//114.091837,22.547244
	}
}
