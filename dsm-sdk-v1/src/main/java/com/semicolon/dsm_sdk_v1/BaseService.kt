package com.semicolon.dsm_sdk_v1

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object BaseService {

    val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    val BaseRetrofit = Retrofit.Builder()
        .baseUrl("https://developer-api.dsmkr.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val serverbasic: ServiceInterface? = BaseRetrofit.create(
        ServiceInterface::class.java) // severbasic 변수 사용해서 만드는 거
}


