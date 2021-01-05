package com.example.dsm_sdk_v1

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object BaseService {

    val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    val BaseRetrofit = Retrofit.Builder()
        .baseUrl("http://54.180.98.91:8080/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val serverbasic: ServiceInterface? = BaseRetrofit.create(ServiceInterface::class.java) // severbasic 변수 사용해서 만드는 거
}


