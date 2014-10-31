package com.store.api.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Md5编码处理类
 * @author vincent
 *
 */
public class MD5 {

    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);

        for (int i = 0; i < bArray.length; ++i) {
            String sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toLowerCase());
        }
        return sb.toString();
    }

    public static byte[] hexToByte(String hex) {
        byte[] ret = new byte[8];
        byte[] tmp = hex.getBytes();
        for (int i = 0; i < 8; ++i) {
            ret[i] = unitBytes(tmp[(i * 2)], tmp[(i * 2 + 1)]);
        }
        return ret;
    }

    public static byte unitBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static String encrypt(String source) {
        MessageDigest md = null;
        byte[] bt = source.getBytes();
        try {
            bt = source.getBytes("UTF-8");
            md = MessageDigest.getInstance("MD5");
            md.update(bt);
            return bytesToHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(encrypt("sfc&2012#^%888189425299352"));
    }
}
