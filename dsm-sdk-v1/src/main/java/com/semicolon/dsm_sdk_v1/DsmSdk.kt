package com.semicolon.dsm_sdk_v1

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity

class DsmSdk : AppCompatActivity() {
    private lateinit var clientIdSend: String
    private lateinit var clientSecretSend: String
    private lateinit var redirectURLSend: String
    fun loginWithAuth(
            context: Context,
            callback: (DTOtoken: DTOtoken?, error: Throwable?) -> Unit,
            loginCallback: (DTOuser?) -> Unit
    ) {
        doLoginWithAuth(context, callback, loginCallback)
    }
    fun getUserInformation(accessToken:String,callback: (getUser: DTOuser?) -> Unit){
        val loginClient= LoginClient()
        return loginClient.basicFun(accessToken,callback)
    }

    fun initSDK(clientId: String, clientSecret: String, redirectURL: String) {
        doInitSdk(clientId, clientSecret, redirectURL)
    }

    fun refreshToken(refreshToken: String,callback:(accessToken:String)->Unit) {
        val loginClient = LoginClient()
        loginClient.refreshToken(refreshToken,callback)
    }

    private fun doInitSdk(clientId: String, clientSecret: String, redirectURL: String) {
        clientIdSend = clientId
        clientSecretSend = clientSecret
        redirectURLSend = redirectURL
    }

    private fun doLoginWithAuth(
            context: Context,
            callback: (DTOtoken: DTOtoken?, error: Throwable?) -> Unit,
            loginCallback: (DTOuser?) -> Unit
    ) {
        mustDoCallback = callback
        loginCallbackCom = loginCallback
        appContext = context
        val startLoginIntent = Intent(context, LoginClient::class.java)
        startLoginIntent.putExtra("get_redirect", redirectURLSend)
        startLoginIntent.putExtra("get_client_id", clientIdSend)
        startLoginIntent.putExtra("get_client_password", clientSecretSend)
        context.startActivity(startLoginIntent.addFlags(FLAG_ACTIVITY_NEW_TASK))
    }


    companion object {
        @JvmStatic
        val instance by lazy { DsmSdk() }
        lateinit var mustDoCallback: (DTOtoken: DTOtoken?, error: Throwable?) -> Unit
        lateinit var loginCallbackCom: (DTOuser?) -> Unit
        lateinit var appContext: Context

    }
}