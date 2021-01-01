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

    @POST("dsmauth/token")
    fun postlogin(
           // @Query("access_token") access_token: String,
            //@Query("refresh_token") refresh_token: String,
            //@Field ("client_id")client_id: String,
            //@Field ("client_sereet")client_secret : String,
            //@Field ("code")code: Int,
            @Body param: MutableMap<String, String>

    ) : Call<token>


    @Headers ("refesh-token: Bearer <refresh_token>")
    @GET("./")
    fun getrefresh(

            @Query ("code") code: Int,
            @Query("access_token") access_token: String,
            @Query("message") message: String

    ): Call<refresh>

}