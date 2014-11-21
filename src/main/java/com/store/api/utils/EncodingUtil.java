package com.store.api.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncodingUtil {
	
	
	/**
	 * 转换成unicode码
	 * @param str
	 * @return
	 */
	public static String stringToUnicode(String str) {
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (c > 255) {
                sb.append("\\u");
                j = (c >>> 8);
                tmp = Integer.toHexString(j);
                if (tmp.length() == 1)
                    sb.append("0");
                sb.append(tmp);
                j = (c & 0xFF);
                tmp = Integer.toHexString(j);
                if (tmp.length() == 1)
                    sb.append("0");
                sb.append(tmp);
            } else {
                sb.append(c);
            }

        }
        return (new String(sb));
    }
	/**
	 * unicode转换成中文
	 * @param str
	 * @return
	 */
	public static String unicodeToString(String str){
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");    
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");    
        }
        return str;
	}
	
	/**
	 * 判断字符串的编码类型
	 * @param str
	 * @return
	 */
    public static String getEncoding(String str) {      
        String encode = "GB2312";      
       try {      
           if (str.equals(new String(str.getBytes(encode), encode))) {      
                String s = encode;      
               return s;      
            }      
        } catch (Exception exception) {      
        }      
        encode = "ISO-8859-1";      
       try {      
           if (str.equals(new String(str.getBytes(encode), encode))) {      
                String s1 = encode;      
               return s1;      
            }      
        } catch (Exception exception1) {      
        }      
        encode = "UTF-8";      
       try {      
           if (str.equals(new String(str.getBytes(encode), encode))) {      
                String s2 = encode;      
               return s2;      
            }      
        } catch (Exception exception2) {      
        }      
        encode = "GBK";      
       try {      
           if (str.equals(new String(str.getBytes(encode), encode))) {      
                String s3 = encode;      
               return s3;      
            }      
        } catch (Exception exception3) {      
        }      
       return "";      
    }      
	
    
   
}
