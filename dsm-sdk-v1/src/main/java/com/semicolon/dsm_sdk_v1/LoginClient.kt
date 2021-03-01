package com.semicolon.dsm_sdk_v1


import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.semicolon.dsm_sdk_v1.BaseService.gson
import com.semicolon.dsm_sdk_v1.DsmSdk.Companion.loginCallbackCom
import com.semicolon.dsm_sdk_v1.DsmSdk.Companion.mustDoCallback
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
                return if (view.url?.contains("code") == true) {
                    val changeurl = view.url!! // 바뀐 url
                    //문자열 자르기!!
                    val code = changeurl.substring(changeurl.lastIndexOf("=") + 1) // 리다리엑트 url ?code= 의 뒷부분
                    post["client_id"] = clientId
                    post["client_secret"] =  clientSecret// 우리가 직접 넣어야됨
                    post["code"] = code // ?code= 뒤에 있는거에여
                    dsmAuthFunToken(post)  // 이게 api post 시작!! 이 안에 다른 api 2개 들어있어요!!
                    false
                } else {
                    true
                }
            }
        })
    }


    private fun dsmAuthFunToken(Post: MutableMap<String, String>) {
        val Basic = BaseService.serverbasic?.postlogin(Post)

        Basic?.enqueue(object : retrofit2.Callback<DTOtoken> {
            override fun onFailure(call: Call<DTOtoken>, t: Throwable) {
                mustDoCallback(null, t)
            }

            override fun onResponse(call: Call<DTOtoken>, response: Response<DTOtoken>) {
                val bodyBasic = response.body()
                if(response.isSuccessful){
                    val access_token = bodyBasic?.access_token.toString()
                    val refrash_token = bodyBasic?.refresh_token.toString()
                    val token= DTOtoken(access_token, refrash_token)
                    mustDoCallback(token, null)
                    basicFun(access_token, loginCallbackCom) // access 토큰을 요청하는 api 함수
                }else{
                    mustDoCallback(null,null)
                }

            }
        })
    }

    fun refreshToken(refresh_token: String,callback:(String)->Unit){
        val time = System.currentTimeMillis().toString() // 시간  받는거
        val Basic = BaseService.serverbasic?.getrefresh(time, "Bearer $refresh_token")

        Basic?.enqueue(object : retrofit2.Callback<refresh> {
            override fun onFailure(call: Call<refresh>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<refresh>, response: Response<refresh>) {
                val accessToken=response.body()?.access_token.toString()
                callback(accessToken)
            }
        })
    }



    fun basicFun(access_token: String,callback: (getUser: DTOuser?) -> Unit) {
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
                    callback(inDto)
                    finish()
                }
            }
        })
    }
}