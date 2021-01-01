package com.example.dsm_sdk

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var b: String = ""
        val webview: WebView = findViewById(R.id.webview)

        webview.settings.javaScriptEnabled = true // 자바스크립트 허용
        webview.webViewClient = WebViewClient()
        webview.webChromeClient = WebChromeClient()
        webview.loadUrl("http://193.123.237.232/external/login?redirect_url=http://localhost:3000&client_id=123456")
        val Post  = mutableMapOf<String, String>()
        webview.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                val a: Uri = Uri.parse(view.url)
                if (view.url?.contains("code") == true) {
                    b = ("${view.url}")
                    Log.d("로그","(${b}sagadgas)")

                    //문자열 자르기!!
                    val code = b.substring(b.lastIndexOf("=")+1)
                    Log.d("로그", "${code}")

                    Post["client_id"] = "123456"
                    Post["client_secret"] = "1234"
                    Post["code"] = "${code}"
                    //val c = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    //startActivity(c)
                    Dsmauthfun_token(Post)

                    return false
                }
                else{

                    Log.e("로그","qetertqer   ${view.url}             ,${a.path}")
                    return true
                }
            }
        })



        Log.d("로그", "${webview.url}ㅁㄴㅇㄹㅁㅇㄹ")
        webview.webViewClient


        fun Dsmauth_refresh(){
            val Basic = BaseService.serverbasic?.getrefresh(3, "qwer1234", "qwer1234")

            Basic?.enqueue(object : retrofit2.Callback<refresh> {
                override fun onFailure(call: Call<refresh>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<refresh>, response: Response<refresh>) {
                    val bodyBasic = response.body()
                }
            })
        }

        fun Basicfun(){
            val Basic = BaseService.serverbasic?.getbasic("json", "json", "json", 1, "json")

            Basic?.enqueue(object : retrofit2.Callback<DTObasic> {
                override fun onFailure(call: Call<DTObasic>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<DTObasic>, response: Response<DTObasic>) {
                    val bodyBasic = response.body()

                }
            })
        }



    }
    fun Dsmauthfun_token(Post:MutableMap<String,String>){


        val Basic = BaseService.serverbasic?.postlogin(Post)

        Basic?.enqueue(object : retrofit2.Callback<token> {
            override fun onFailure(call: Call<token>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<token>, response: Response<token>) {
                val bodyBasic = response.body()
                bodyBasic?.access_token
                Log.d("로그", "${response.raw()}")
            }
        })
    }
}
