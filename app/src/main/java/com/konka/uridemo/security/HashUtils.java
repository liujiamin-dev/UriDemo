package com.konka.uridemo.security;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @CreateTime : 2024/7/10 14:58
 * @Author : Administrator
 * @Description :
 */
public class HashUtils {
    public static String md5(String src) {
        byte[] pwd = null;
        try {
            pwd = MessageDigest.getInstance("md5").digest(src.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String code = new BigInteger(1, pwd).toString(16);
        for (int i = 0; i < 32 - code.length(); i++) {
            code = "0" + code;
        }
        return code;
    }

    public static String commonsMd5(String src) {
        char[] hexChars = Hex.encodeHex(DigestUtils.md5(src));
        return new String(hexChars);
    }

    public static String sha(String src) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("sha");
        byte[] shaByte = sha.digest(src.getBytes("utf-8"));
        StringBuffer code = new StringBuffer();
        for (int i = 0; i < shaByte.length; i++) {
            int val = ((int) shaByte[i]) & 0xff;
            if (val < 16) {
                code.append("0");
            }
            code.append(Integer.toHexString(val));
        }
        return code.toString();
    }
    public static String commonsSha(String src) {
        char[] hexChars = Hex.encodeHex(DigestUtils.sha1(src));
        return new String(hexChars);
    }
}
