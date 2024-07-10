package com.konka.uridemo.security;

import static android.icu.lang.UScript.SYMBOLS;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @CreateTime : 2024/7/10 11:47
 * @Author : Administrator
 * @Description :
 */
public class AESUtils {
    /**
     * 编码
     */
    private static final String ENCODING = "UTF-8";
    /**
     * 算法定义
     */
    private static final String AES_ALGORITHM = "AES";
    /**
     * 指定填充方式
     */
    private static final String CIPHER_PADDING = "AES/ECB/PKCS5Padding";
    private static final String CIPHER_CBC_PADDING = "AES/CBC/PKCS5Padding";
    /**
     * 偏移量(CBC中使用，增强加密算法强度)
     */
    private static final String IV_SEED = "1234567812345678";


    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; // 数字和26个字母组成
    private static final Random RANDOM = new SecureRandom();
    public static String initKey() {
        char[] nonceChars = new char[16];  //指定长度为6位/自己可以要求设置

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    /**
     * AES加密
     * @param content 待加密内容
     * @param aesKey  密码
     * @return
     */
    public static String encrypt(String content, String aesKey){
        if(TextUtils.isEmpty(content)){
            return null;
        }
        //判断秘钥是否为16位
        if(!TextUtils.isEmpty(aesKey) && aesKey.length() == 16){
            try {
                //对密码进行编码
                byte[] bytes = aesKey.getBytes(ENCODING);
                //设置加密算法，生成秘钥
                SecretKeySpec skeySpec = new SecretKeySpec(bytes, AES_ALGORITHM);
                // "算法/模式/补码方式"
                Cipher cipher = Cipher.getInstance(CIPHER_PADDING);
                //选择加密
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
                //根据待加密内容生成字节数组
                byte[] encrypted = cipher.doFinal(content.getBytes(ENCODING));
                //返回base64字符串
                return encryptBASE64(encrypted);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }else {
            return null;
        }
    }

    /**
     * 解密
     *
     * @param content 待解密内容
     * @param aesKey  密码
     * @return
     */
    public static String decrypt(String content, String aesKey){
        if(TextUtils.isEmpty(content)){
            return null;
        }
        //判断秘钥是否为16位
        if(!TextUtils.isEmpty(aesKey) && aesKey.length() == 16){
            try {
                //对密码进行编码
                byte[] bytes = aesKey.getBytes(ENCODING);
                //设置解密算法，生成秘钥
                SecretKeySpec skeySpec = new SecretKeySpec(bytes, AES_ALGORITHM);
                // "算法/模式/补码方式"
                Cipher cipher = Cipher.getInstance(CIPHER_PADDING);
                //选择解密
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);

                //先进行Base64解码
                byte[] decodeBase64 = decryptBASE64(content);

                //根据待解密内容进行解密
                byte[] decrypted = cipher.doFinal(decodeBase64);
                //将字节数组转成字符串
                return new String(decrypted, ENCODING);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }else {
            return null;
        }
    }

    /**
     * AES_CBC加密
     *
     * @param content 待加密内容
     * @param aesKey  密码
     * @return
     */
    public static String encryptCBC(String content, String aesKey){
        if(TextUtils.isEmpty(content)){
            return null;
        }
        //判断秘钥是否为16位
        if(!TextUtils.isEmpty(aesKey) && aesKey.length() == 16){
            try {
                //对密码进行编码
                byte[] bytes = aesKey.getBytes(ENCODING);
                //设置加密算法，生成秘钥
                SecretKeySpec skeySpec = new SecretKeySpec(bytes, AES_ALGORITHM);
                // "算法/模式/补码方式"
                Cipher cipher = Cipher.getInstance(CIPHER_CBC_PADDING);
                //偏移
                IvParameterSpec iv = new IvParameterSpec(IV_SEED.getBytes(ENCODING));
                //选择加密
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
                //根据待加密内容生成字节数组
                byte[] encrypted = cipher.doFinal(content.getBytes(ENCODING));
                //返回base64字符串
                return encryptBASE64(encrypted);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }else {
            return null;
        }
    }

    /**
     * AES_CBC解密
     *
     * @param content 待解密内容
     * @param aesKey  密码
     * @return
     */
    public static String decryptCBC(String content, String aesKey){
        if(TextUtils.isEmpty(content)){
            return null;
        }
        //判断秘钥是否为16位
        if(!TextUtils.isEmpty(aesKey) && aesKey.length() == 16){
            try {
                //对密码进行编码
                byte[] bytes = aesKey.getBytes(ENCODING);
                //设置解密算法，生成秘钥
                SecretKeySpec skeySpec = new SecretKeySpec(bytes, AES_ALGORITHM);
                //偏移
                IvParameterSpec iv = new IvParameterSpec(IV_SEED.getBytes(ENCODING));
                // "算法/模式/补码方式"
                Cipher cipher = Cipher.getInstance(CIPHER_CBC_PADDING);
                //选择解密
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

                //先进行Base64解码
                byte[] decodeBase64 = decryptBASE64(content);

                //根据待解密内容进行解密
                byte[] decrypted = cipher.doFinal(decodeBase64);
                //将字节数组转成字符串
                return new String(decrypted, ENCODING);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }else {
            return null;
        }
    }

    /**
     * @description:  字节数组转字符串
     * @date: 2024/1/17 14:59
     * @param bytes 字节数组
     * @return java.lang.String
     */
    private static String encryptBASE64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }


    /**
     * @description: 字符串转字节数组
     * @date: 2024/1/17 15:00
     * @param str 字符串
     * @return byte[]
     */
    private static byte[] decryptBASE64(String str) {
        return Base64.getDecoder().decode(str);
    }
}
