package com.example.dsm_sdk

import retrofit2.Call
import retrofit2.http.*

interface ServiceInterface {

    @GET("./")

    fun getbasic(
        @Query("name") name: String,
        @Query("gcn") gcn : String,
        @Query("email") emali: String,
        @Query("code") code : Int,
        @Query("massage") massage : String
    ) : Call<DTObasic>

    @GET
    @POST("./")
    fun postlogin(
        @Query("access_token") access_token: String,
        @Query("refresh_token") refresh_token: String,
        @Body client_id: String,
        @Body client_secret : String,
        @Body code: Int

    ) : Call<token>


    @Headers ("refesh-token: Bearer <refresh_token>")
    @GET("./")
    fun getrefresh(

        @Query ("code") code : Int,
        @Query("access_token")access_token: String,
        @Query("message") message: String

    ): Call<refresh>

}