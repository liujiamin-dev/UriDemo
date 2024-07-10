package com.konka.uridemo.security

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.konka.uridemo.R

/**
 * @CreateTime : 2024/7/10 15:04
 * @Author : ljm
 * @Description :
 */
class SecurityActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)
        testHash()
        testAES()
        testRSA()
    }

    private fun testHash() {
        val content = "gwegeqwg125gqwwqgeqwggwgwedsgwedag"
        val md5 = HashUtils.md5(content)
        val md5ByCommon = HashUtils.commonsMd5(content)
        val sha = HashUtils.sha(content)
        val shaByCommon = HashUtils.commonsSha(content)
        Log.i(TAG, "md5: $md5 md5ByCommon: $md5ByCommon sha: $sha shaByCommon: $shaByCommon")
    }

    private fun testAES() {
        val key = AESUtils.initKey();
        Log.i(TAG, "key: $key")
        val content = "hello world---"
        val encrypt = AESUtils.encrypt(content, key)
        val decrypt = AESUtils.decrypt(encrypt, key)
        Log.i(TAG, "encrypt: $encrypt decrypt: $decrypt")
        val encryptCBC = AESUtils.encryptCBC(content, key)
        val decryptCBC = AESUtils.decryptCBC(encryptCBC, key)
        Log.i(TAG, "encryptCBC: $encryptCBC decryptCBC: $decryptCBC")
    }

    private fun testRSA() {
        val keyPair = RSAUtils.initKeyPair()
        val publicKey = keyPair["PublicKeyStr"]
        val privateKey = keyPair["PrivateKeyStr"]
        val content = "you are my density"
        val encrypt = RSAUtils.encryptByPrivateKey(content, privateKey)
        val decrypt = RSAUtils.decryptByPublicKey(encrypt, publicKey)
        Log.i(TAG, "encrypt: $encrypt decrypt: $decrypt")
        val encryptByPublicKey = RSAUtils.encryptByPublicKey(content, publicKey)
        val decryptByPrivateKey = RSAUtils.decryptByPrivateKey(encryptByPublicKey, privateKey)
        Log.i(TAG, "encryptByPublicKey: $encryptByPublicKey decryptByPrivateKey: $decryptByPrivateKey")
        val sign = RSAUtils.sign(content, privateKey)
        Log.i(TAG, "sign: $sign")
        if(RSAUtils.verify(content, publicKey, sign)) {
            Log.i(TAG, "verify success")
        } else {
            Log.i(TAG, "verify fail")
        }

    }

    companion object {
        const val TAG = "SecurityActivity"
    }
}