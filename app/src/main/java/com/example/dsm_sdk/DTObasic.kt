package com.example.dsm_sdk

import android.provider.ContactsContract

data class DTObasic(val name: String,
                    val gcn: String,
                    val email: String,
                    val code: Int,
                    val massage: String)


data class token(
    val access_token: String,
    val refrash_token: String
)

data class refresh(
    val code: Int,
    val access_token: String,
    val massage: String
)