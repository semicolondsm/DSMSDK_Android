package com.example.dsm_sdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity(), WebView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webview: WebView = findViewById(R.id.webview)

        webview.settings.javaScriptEnabled = true // 자바스크립트 허용
        webview.webViewClient = WebViewClient()
        webview.webChromeClient = WebChromeClient()
        webview.loadUrl("http://193.123.237.232/external/login")







        fun Basicfun(){
            val Basic = BaseService.serverbasic?.getbasic("json","json","json",1,"json")

            Basic?.enqueue(object : retrofit2.Callback<DTObasic> {
                override fun onFailure(call: Call<DTObasic>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<DTObasic>, response: Response<DTObasic>) {
                    val bodyBasic = response.body()

                }
            })
        }
        fun Dsmauthfun_token(){

            val postid : String =""
            val postsecret : String =""
            val postcode : Int =1

            val Basic = BaseService.serverbasic?.postlogin("json","json",postid,postsecret,postcode)

            Basic?.enqueue(object : retrofit2.Callback<token> {
                override fun onFailure(call: Call<token>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<token>, response: Response<token>) {
                    val bodyBasic = response.body()
                }
            })
        }

        fun Dsmauth_refresh(){
            val Basic = BaseService.serverbasic?.getrefresh(1,"dd","json")

            Basic?.enqueue(object : retrofit2.Callback<refresh> {
                override fun onFailure(call: Call<refresh>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<refresh>, response: Response<refresh>) {
                    val bodyBasic = response.body()
                }
            })
        }


    }
}