package com.konka.uridemo.security;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * @CreateTime : 2024/7/9 11:36
 * @Author : Administrator
 * @Description :
 */
public class RSAUtils {
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY_STR = "PublicKeyStr";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY_STR = "PrivateKeyStr";

    /**
     * RSA最大加密明文大小(字节数)
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小(字节数)
     */
    private static final int MAX_DECRYPT_BLOCK = 128;



    /**
     * @description: 生成公钥私钥
     * @date: 2024/1/17 14:47
     * @param
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    public static Map<String, String> initKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        kpg.initialize(1024);
        KeyPair keyPair = kpg.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String publicKeyStr = encryptBASE64(publicKey.getEncoded());
        String privateKeyStr = encryptBASE64(privateKey.getEncoded());

        Map<String, String> keyMap = new HashMap<>();
        keyMap.put(PUBLIC_KEY_STR, publicKeyStr);
        keyMap.put(PRIVATE_KEY_STR, privateKeyStr);
        return keyMap;
    }


    /**
     * @description:  公钥加密
     * @date: 2024/1/17 14:49
     * @param str 待加密字符串
     * @param key 公钥
     * @return java.lang.String
     */
    public static String encryptByPublicKey(String str, String key) throws Exception {
        byte[] data = str.getBytes();
        PublicKey publicKey = strToPublicKey(key);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] bytes = dataSegment(data, cipher, MAX_ENCRYPT_BLOCK);
        return encryptBASE64(bytes);
    }


    /**
     * @description:  私钥解密
     * @date: 2024/1/17 14:52
     * @param str 待解密字符串
     * @param key 私钥
     * @return java.lang.String
     */
    public static String decryptByPrivateKey(String str, String key) throws Exception {
        byte[] data = decryptBASE64(str);
        PrivateKey privateKey = strToPrivateKey(key);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] bytes = dataSegment(data, cipher, MAX_DECRYPT_BLOCK);
        return new String(bytes);
    }


    /**
     * @description:  私钥加密
     * @date: 2024/1/17 14:58
     * @param str 待加密字符串
     * @param key 私钥
     * @return java.lang.String
     */
    public static String encryptByPrivateKey(String str, String key) throws Exception {
        byte[] data = str.getBytes();
        PrivateKey privateKey = strToPrivateKey(key);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        byte[] bytes = dataSegment(data, cipher, MAX_ENCRYPT_BLOCK);
        return encryptBASE64(bytes);
    }


    /**
     * @description:  公钥解密
     * @date: 2024/1/17 15:23
     * @param str 待解密字符串
     * @param key 公钥
     * @return java.lang.String
     */
    public static String decryptByPublicKey(String str, String key) throws Exception {
        byte[] data = decryptBASE64(str);
        PublicKey publicKey = strToPublicKey(key);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] bytes = dataSegment(data, cipher, MAX_DECRYPT_BLOCK);
        return new String(bytes);
    }


    /**
     * @description:  使用私钥对数据进行数字签名
     * @date: 2024/1/17 15:36
     * @param str 待加签字符串
     * @param privateKey 私钥
     * @return java.lang.String
     */
    public static String sign(String str, String privateKey) throws Exception {
        byte[] data = str.getBytes();
        PrivateKey priKey = strToPrivateKey(privateKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        byte[] bytes = signature.sign();
        return encryptBASE64(bytes);
    }


    /**
     * @description: 使用公钥对数据验证签名
     * @date: 2024/1/17 15:46
     * @param str 待验签字符串(即要加签的内容)
     * @param publicKey 公钥
     * @param sign 数据签名
     * @return boolean
     */
    public static boolean verify(String str, String publicKey, String sign) throws Exception {
        byte[] data = str.getBytes();
        byte[] signBytes = decryptBASE64(sign);
        PublicKey pubKey = strToPublicKey(publicKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        return signature.verify(signBytes);
    }

    /**
     * @description: 对数据进行分段加密或解密
     * @date: 2024/1/17 16:11
     * @param data
     * @param cipher
     * @param maxBlock
     * @return byte[]
     */
    private static byte[] dataSegment(byte[] data, Cipher cipher, int maxBlock) throws Exception{
        byte[] toByteArray;
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        int i = 0;
        byte[] cache;
        // 对数据分段加密解密
        while (inputLen - offSet > 0) {
            int var = inputLen - offSet > maxBlock ? maxBlock : inputLen - offSet;
            cache = cipher.doFinal(data, offSet, var);
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxBlock;
        }
        toByteArray = out.toByteArray();
        out.close();
        return toByteArray;
    }


    /**
     * @description: 获取私钥
     * @date: 2024/1/17 14:57
     * @param str 私钥字符串
     * @return java.security.PrivateKey
     */
    private static PrivateKey strToPrivateKey(String str) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(decryptBASE64(str));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        return privateKey;
    }

    /**
     * @description: 获取公钥
     * @date: 2024/1/17 14:57
     * @param str 公钥字符串
     * @return java.security.PrivateKey
     */
    private static PublicKey strToPublicKey(String str) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(decryptBASE64(str));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        return publicKey;
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
