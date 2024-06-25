package com.konka.uridemo.classuser

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.konka.uridemo.R
import java.net.MalformedURLException
import java.net.URL

/**
 * @CreateTime : 2024/6/21 13:55
 * @Author : ljm
 * @Description :
 */
class UrlDemoActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url)

        supportedProtocols()

        val url = URL("http://api.kkapp.com/OnlineKeyValueServer/onlineKV/getTvAppKey.shtml?appId=20&appkey=b3e2c38b2be35d452d40904f7746a81d&sn=PRG2411WS000021OE671&keyName=screenConfig&sign=2FD7061FA4860436A4CFBABADAA6E042")
        getUrlInfo(url)
    }

    private fun supportedProtocols() {
        // 当前设备支持的协议
        createUrl("http://www.baidu.com")             //support
        createUrl("https://www.baidu.com")             //support
        createUrl("ftp://ibiblio.org/pub/languages/java/javafaq/")        //文件传输协议support
        createUrl("mailto:elharo@ibiblro.org")                        //简单邮件传输协议don't support
        createUrl("telnet:/dibner.poly.edu/")              //don't support
        createUrl("file:///etc/passwd")                  //support
        createUrl("gopher://gopher.anc.org.za/")              //don't support
        createUrl("ldap://ldap.itd.umich.edu/o=University%20of%2oMichigan,c=US?postalAddress")       //don't support
        createUrl("jar:http://cafeaulait.org/books/javaio/ioexamples/javaio.jar!/com/macfaq/io/StreamCopier.class")       //support
        createUrl("nfs://utopia.poly.edu/usr/tmp/")                      //don't support
        createUrl("jdbc:mysql://luna.ibiblio.org:3306/NEWS")              //don't support
        createUrl("rmi://ibiblio.org/RenderEngine")                 //don't support
    }

    private fun createUrl(urlStr: String) {
        try {
            val url = URL(urlStr)
            Log.i(TAG, "support $urlStr")
        } catch (ex: MalformedURLException) {
            Log.i(TAG, "don't support $urlStr")
        }
    }

    private fun getUrlInfo(url: URL) {
        Log.i(TAG, "protocol: ${url.protocol} host: ${url.host} port: ${url.port} defaultport: ${url.defaultPort} path: ${url.path} file: ${url.file} authority: ${url.authority}")
    }

    companion object {
        const val TAG = "UrlDemo"
    }
}