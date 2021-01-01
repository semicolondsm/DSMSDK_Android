package com.example.dsm_sdk

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

class WebViewer:AppCompatActivity() {
    fun startLoginWithDsmAuth(url:String){
        val browserIntent= Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

}