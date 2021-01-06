package com.example.dsm_sdk_v1


import android.os.Build
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.dsm_sdk_v1.BaseService.gson
import com.example.dsm_sdk_v1.DsmSdk.Companion.loginCallbackCom
import com.example.dsm_sdk_v1.DsmSdk.Companion.mustDoCallback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginClient : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sdk_layout)
        val webview: WebView = findViewById(R.id.webview)
        val redirectUrl=intent.getStringExtra("get_redirect").toString()
        val clientId=intent.getStringExtra("get_client_id").toString()
        val clientSecret=intent.getStringExtra("get_client_password").toString()

        webview.settings.javaScriptEnabled = true // 자바스크립트 허용
        webview.webViewClient = WebViewClient()
        webview.webChromeClient = WebChromeClient()
        webview.loadUrl("http://193.123.237.232/external/login?redirect_url=$redirectUrl&client_id=$clientId")
        val post = mutableMapOf<String, String>() // post api안 body안에 담을 것
        webview.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url) // 바뀐 url을 가져옴
                if (view.url?.contains("code") == true) {
                    val changeurl = view.url!! // 바뀐 url
                    //문자열 자르기!!
                    val code = changeurl.substring(changeurl.lastIndexOf("=") + 1) // 리다리엑트 url ?code= 의 뒷부분
                    post["client_id"] = clientId
                    post["client_secret"] =  clientSecret// 우리가 직접 넣어야됨
                    post["code"] = code // ?code= 뒤에 있는거에여
                    dsmAuthFunToken(post)  // 이게 api post 시작!! 이 안에 다른 api 2개 들어있어요!!
                    return false
                } else {
                    return true
                }
            }
        })
    }


    private fun dsmAuthFunToken(Post: MutableMap<String, String>) {
        val Basic = BaseService.serverbasic?.postlogin(Post)

        Basic?.enqueue(object : retrofit2.Callback<token> {
            override fun onFailure(call: Call<token>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<token>, response: Response<token>) {
                val bodyBasic = response.body()
                val access_token = bodyBasic?.access_token.toString()
                val refrash_token = bodyBasic?.refresh_token.toString()
                dsmAuthRefresh(refrash_token) // refresh 토큰을 이용하여 access 토큰을 받는 함수
                basicFun(access_token) // access 토큰을 요청하는 api 함수

            }
        })
    }

    private fun dsmAuthRefresh(refresh_token: String) {
        val time = System.currentTimeMillis().toString() // 시간  받는거
        val Basic = BaseService.serverbasic?.getrefresh(time, "Bearer $refresh_token")

        Basic?.enqueue(object : retrofit2.Callback<refresh> {
            override fun onFailure(call: Call<refresh>, t: Throwable) {
                t.printStackTrace()
                mustDoCallback(null, t)
            }

            override fun onResponse(call: Call<refresh>, response: Response<refresh>) {
                val getAccessToken = response.body()?.access_token // 재요청한 어세스 토큰 값
                val token = getAccessToken?.let { token(it, refresh_token) }
                if (token != null) {
                    mustDoCallback(token, null)
                }

            }
        })
    }

    private fun basicFun(access_token: String) {
        val time = System.currentTimeMillis().toString() // 시간 받는거
        val BaseRetrofit = Retrofit.Builder()
                .baseUrl("http://54.180.98.91:8090/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        val serverbasic: ServiceInterface? = BaseRetrofit.create(ServiceInterface::class.java) // severbasic 변수 사용해서 만드는 거
        val Basic = serverbasic?.getbasic("Bearer $access_token", time)

        Basic?.enqueue(object : retrofit2.Callback<DTOuser> {
            override fun onFailure(call: Call<DTOuser>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<DTOuser>, response: Response<DTOuser>) {
                if (response.body() != null) {
                    val userName = response.body()?.name.toString()
                    val userGcn = response.body()?.gcn.toString()
                    val userEmail = response.body()?.email.toString()
                    val inDto = DTOuser(userName, userGcn, userEmail)
                    loginCallbackCom(inDto)
                    finish()
                }
            }
        })
    }
}