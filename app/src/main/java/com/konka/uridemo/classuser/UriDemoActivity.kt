package com.konka.uridemo.classuser

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.konka.uridemo.R
import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * @CreateTime : 2024/6/25 11:40
 * @Author : Administrator
 * @Description :
 */
class UriDemoActivity: AppCompatActivity() {
    private var mParams: StringBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uri)

        encode("hl", "中文")
        encode("content", "刘亦菲")
        val uriStr = "https://www.google.com/search${mParams.toString()}"
        Log.i(TAG, "uriStr: $uriStr")
        Log.i(TAG, "decode uriStr: ${URLDecoder.decode(uriStr, "UTF-8")}")
        getUriInfo(URI(uriStr))
    }

    private fun encode(name: String, value: String) {
        if(mParams.isEmpty()) {
            mParams.append("?")
        } else {
            mParams.append("&")
        }
        mParams.append(URLEncoder.encode(name, "UTF-8"))
        mParams.append("=")
        mParams.append(URLEncoder.encode(value, "UTF-8"))
    }

    private fun getUriInfo(uri: URI) {
        Log.i(TAG, "authority:${uri.authority} fragment:${uri.fragment} host:${uri.host} path:${uri.path} port:${uri.port} query:${uri.query} scheme:${uri.scheme} userInfo:${uri.userInfo}")
        Log.i(TAG, "rawAuthority:${uri.rawAuthority} rawFragment:${uri.rawFragment} rawPath:${uri.rawPath} rawQuery:${uri.rawQuery} rawSchemeSpecificPart:${uri.rawSchemeSpecificPart} rawUserInfo:${uri.rawUserInfo} schemeSpecificPart:${uri.schemeSpecificPart}")
    }

    companion object{
        const val TAG = "UriDemo"
    }
}