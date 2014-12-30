package com.store.api.utils.security;

import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import com.store.api.utils.Utils;

/**
 * 安全工具类
 * 
 * Revision History
 * 
 * 2014年7月9日,vincent,created it
 */
public class SecurityUtil {

    /**
     * base64URL方式编码，末尾不会有两个＝号，不会包含+号与/号
     * 
     * @param str
     * @return
     */
    public static String Base64Encode(String str) {
        if (!Utils.isEmpty(str))
            return Base64.encodeBase64URLSafeString(str.getBytes());
        return null;
    }

    /**
     * base64解码
     * @param str
     * @return
     */
    public static String Base64Decode(String str) {
        if (!Utils.isEmpty(str))
            return new String(Base64.decodeBase64(str));
        return null;
    }

    /**
     * 将字符串先做base64编码，然后再做Caesar加密
     * 
     * @param key 待加密串
     * @return 已加密串
     */
    public static String encrypt(String key) {
        if (Utils.isEmpty(key))
            return "";
        String base64 = Base64Encode(key);
        return Caesar.encrypt(base64);
    }

    /**
     * 对做Caesar和base64的加密串解密
     * 
     * @param key 待解密串
     * @return 已解密串
     */
    public static String decrypt(String key) {
        if (Utils.isEmpty(key))
            return "";
        String decryptStr = Caesar.decrypt(key);
        return Base64Decode(decryptStr);
    }
    
    public static void main(String[] args){
        System.out.println(decrypt("aaaDNWQJHxm3R_OxCVaNu"));
        //cargo_199_5410075   A2IaH29iKYt4QhV3KYMzOhV
        System.out.println(encrypt("visitor_192_"));
    }
}
