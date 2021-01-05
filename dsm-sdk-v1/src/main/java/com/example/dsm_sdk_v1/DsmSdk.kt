package com.example.dsm_sdk_v1

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity

class DsmSdk : AppCompatActivity() {
    fun loginWithAuth(
            clientId: String,
            clientPassword: String,
            context: Context,
            callback: (token: token?, error: Throwable?) -> Unit,
            loginCallback:(DTOuser?)->Unit
    ) {
        doLoginWithAuth(clientId,clientPassword,context, callback,loginCallback)
    }

    private fun doLoginWithAuth(clientId: String, clientPassword: String, context: Context, callback: (token: token?, error: Throwable?) -> Unit, loginCallback: (DTOuser?) -> Unit) {
        mustDoCallback = callback
        loginCallbackCom=loginCallback
        appContext = context
        val startLoginIntent = Intent(context, LoginClient::class.java)
        startLoginIntent.putExtra("client_id",clientId)
        startLoginIntent.putExtra("client_password",clientPassword)
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