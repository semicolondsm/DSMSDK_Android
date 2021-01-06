package com.example.dsm_sdk_v1

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity

class DsmSdk : AppCompatActivity() {
    lateinit var clientIdSend:String
    lateinit var clientSecretSend:String
    lateinit var redirectURLSend: String
    fun loginWithAuth(
            context: Context,
            callback: (token: token?, error: Throwable?) -> Unit,
            loginCallback:(DTOuser?)->Unit
    ) {
        doLoginWithAuth(context, callback,loginCallback)
    }
    fun initSDK(clientId:String,clientSecret:String,redirectURL:String){
        doInitSdk(clientId,clientSecret,redirectURL)
    }

    private fun doInitSdk(clientId:String,clientSecret:String,redirectURL:String){
        clientIdSend=clientId
        clientSecretSend= clientSecret
        redirectURLSend=redirectURL
    }
    private fun doLoginWithAuth(context: Context, callback: (token: token?, error: Throwable?) -> Unit, loginCallback: (DTOuser?) -> Unit) {
        mustDoCallback = callback
        loginCallbackCom=loginCallback
        appContext = context
        val startLoginIntent = Intent(context, LoginClient::class.java)
        startLoginIntent.putExtra("get_redirect", redirectURLSend)
        startLoginIntent.putExtra("get_client_id",clientIdSend)
        startLoginIntent.putExtra("get_client_password",clientSecretSend)
        context.startActivity(startLoginIntent.addFlags(FLAG_ACTIVITY_NEW_TASK))
    }


    companion object {
        @JvmStatic
        val instance by lazy { DsmSdk() }
        lateinit var mustDoCallback: (token: token?, error: Throwable?) -> Unit
        lateinit var loginCallbackCom: (DTOuser?) -> Unit
        lateinit var appContext: Context

    }
}