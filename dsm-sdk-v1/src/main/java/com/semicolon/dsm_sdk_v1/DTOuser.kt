package com.semicolon.dsm_sdk_v1

import com.google.gson.annotations.SerializedName

data class DTOtoken(
        @SerializedName("access-token")
        val access_token: String,
        @SerializedName("refresh-token")
        val refresh_token: String
)


data class DTOuser(
        @SerializedName("name")
        val name: String,
        @SerializedName("gcn")
        val gcn: String,
        @SerializedName("email")
        val email: String
)

data class refresh(
        @SerializedName("access-token")
        val access_token: String
)