package com.semiclone.dsm_sdk_v1

import retrofit2.Call
import retrofit2.http.*

interface ServiceInterface {

    @GET("v1/info/basic?")
    fun getbasic(
            @Header("access-token") access_token : String,
            @Query ("time")time : String


            ) : Call<DTOuser>

    @POST("dsmauth/token")
    fun postlogin(
            @Body param: MutableMap<String, String>
    ) : Call<DTOtoken>


    @GET("dsmauth/refresh?")
    fun getrefresh(
            @Query ("time")time : String,
            @Header("refresh-token") refresh_token:String

    ): Call<refresh>

}