package com.store.api.utils.security;

/**
 * 凯撒加密，通过位移，替换掉原始字符
 * @author vincent
 *
 */
public class Caesar {
 
    private static final char[] UC_ENCRYPT_CHARS = { 'M', 'D', 'X', 'U', 'P', 'I', 'B', 'E', 'J', 'C', 'T', 'N',   
        'K', 'O', 'G', 'W', 'R', 'S', 'F', 'Y', 'V', 'L', 'Z', 'Q', 'A', 'H' };  
  
    private static final char[] LC_ENCRYPT_CHARS = { 'm', 'd', 'x', 'u', 'p', 'i', 'b', 'e', 'j', 'c', 't', 'n',   
        'k', 'o', 'g', 'w', 'r', 's', 'f', 'y', 'v', 'l', 'z', 'q', 'a', 'h' };  
    
    private static final char[] NUM_ENCRYPT_CHARS ={'3','5','2','8','0','4','7','6','1','9'};
  
    private static char[] UC_DECRYPT_CHARS = new char[26];  
  
    private static char[] LC_DECRYPT_CHARS = new char[26];  
    
    private static char[] NUM_DECRYPT_CHARS = new char[10]; 
  
    static {  
        for (int i = 0; i < 26; i++) {  
            char b = UC_ENCRYPT_CHARS[i];  
            UC_DECRYPT_CHARS[b - 'A'] = (char) ('A' + i);  
  
            b = LC_ENCRYPT_CHARS[i];  
            LC_DECRYPT_CHARS[b - 'a'] = (char) ('a' + i);  
        }
        for(int i=0;i<10;i++){
            char b=NUM_ENCRYPT_CHARS[i];
            NUM_DECRYPT_CHARS[b-'0']=(char) ('0' + i);
        }
    }  
  
    public static char encrypt(char b) {  
        if (b >= 'A' && b <= 'Z') {  
            return UC_ENCRYPT_CHARS[b - 'A'];  
        } else if (b >= 'a' && b <= 'z') {  
            return LC_ENCRYPT_CHARS[b - 'a'];  
        } else if (b >= '0' && b <= '9') {  
            return NUM_ENCRYPT_CHARS[b - '0']; 
        } else {  
            return b;  
        }  
    }  
  
    public static char decrypt(char b) {  
        if (b >= 'A' && b <= 'Z') {  
            return UC_DECRYPT_CHARS[b - 'A'];  
        } else if (b >= 'a' && b <= 'z') {  
            return LC_DECRYPT_CHARS[b - 'a']; 
        } else if (b >= '0' && b <= '9') {  
            return NUM_DECRYPT_CHARS[b - '0']; 
        } else {  
            return b;  
        }  
    }  
      
    public static String encrypt(String input){  
        StringBuilder sb = new StringBuilder();  
        for(int i = 0; i < input.length(); i++){  
            sb.append(encrypt(input.charAt(i)));  
        }  
        return sb.toString();  
    }  
      
    public static String decrypt(String input){  
        StringBuilder sb = new StringBuilder();  
        for(int i = 0; i < input.length(); i++){  
            sb.append(decrypt(input.charAt(i)));  
        }  
        return sb.toString();  
    }  
  
    public static void main(String[] args){
        System.out.println(encrypt("YWJjMTIzNGVmZw=="));
        System.out.println(decrypt("AZCcKYJhOBLkHz=="));
    }
} 
