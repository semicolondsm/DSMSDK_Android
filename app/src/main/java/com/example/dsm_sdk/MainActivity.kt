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
import com.example.dsm_sdk.BaseService.gson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val webview: WebView = findViewById(R.id.webview)

        webview.settings.javaScriptEnabled = true // 자바스크립트 허용
        webview.webViewClient = WebViewClient()
        webview.webChromeClient = WebChromeClient()
        webview.loadUrl("http://193.123.237.232/external/login?redirect_url=https://www.google.com&client_id=hello")
        val post = mutableMapOf<String, String>() // post api안 body안에 담을 것
        webview.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url) // 바뀐 url을 가져옴
                if (view.url?.contains("code") == true) {
                    val changeurl = view.url!! // 바뀐 url

                    //문자열 자르기!!
                    val code = changeurl.substring(changeurl.lastIndexOf("=") + 1) // 리다리엑트 url ?code= 의 뒷부분
                    post["client_id"] = "hello" // 우리가 직접 넣어야됨
                    post["client_secret"] = "hello" // 우리가 직접 넣어야됨
                    post["code"] = "${code}" // ?code= 뒤에 있는거에여
                    Dsmauthfun_token(post)  // 이게 api post 시작!! 이 안에 다른 api 2개 들어있어요!!
                    return false
                } else {
                    return true
                }
            }
        })
    }


    fun Dsmauthfun_token(Post: MutableMap<String, String>) {

        val Basic = BaseService.serverbasic?.postlogin(Post)

        Basic?.enqueue(object : retrofit2.Callback<token> {
            override fun onFailure(call: Call<token>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<token>, response: Response<token>) {
                val bodyBasic = response.body()
                val access_token = bodyBasic?.access_token.toString()
                val refrash_token = bodyBasic?.refresh_token.toString()
                Dsmauth_refresh(refrash_token) // 리프레쉬 토큰을 이용하여 어서ㅔ스 토큰을 받는 함수
                Basicfun(access_token) // 어세스 토큰을 요청하는 api 함수

            }
        })
    }

    fun Dsmauth_refresh(refresh_token: String) {
        val time = System.currentTimeMillis().toString() // 시간  받는거
        val Basic = BaseService.serverbasic?.getrefresh(time, "Bearer $refresh_token")

        Basic?.enqueue(object : retrofit2.Callback<refresh> {
            override fun onFailure(call: Call<refresh>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<refresh>, response: Response<refresh>) {
                val reaccess_token1 = response.body()?.access_token // 재요청한 어세스 토큰 값
                if (refresh_token == "") println("refresh_token 값이 없습니다! refresh_token 값을 받기 위해서 Dsmauthfun_token함수 먼저 사용해 주세요!!!")
            }
        })
    }

    fun Basicfun(access_token: String) {
        val time = System.currentTimeMillis().toString() // 시간 받는거

        val BaseRetrofit = Retrofit.Builder()
                .baseUrl("http://54.180.98.91:8090/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        val serverbasic: ServiceInterface? = BaseRetrofit.create(ServiceInterface::class.java) // severbasic 변수 사용해서 만드는 거

        val Basic = serverbasic?.getbasic("Bearer $access_token", time)

        Basic?.enqueue(object : retrofit2.Callback<DTObasic> {
            override fun onFailure(call: Call<DTObasic>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<DTObasic>, response: Response<DTObasic>) {
                if (access_token == "") println("access_token 값이 없습니다! access_token 값을 받기 위해서 Dsmauthfun_token 함수 먼저 사용해 주세요!!!")
                val name = response.body()?.name // 이름
                val gcn = response.body()?.gcn // 학번같은데?
                val email = response.body()?.email // 이메일
            }
        })
    }
}