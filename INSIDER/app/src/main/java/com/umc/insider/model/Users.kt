package com.umc.insider.model

import com.google.gson.annotations.SerializedName

data class Users (

        @SerializedName("id")
        val id: Long,

        @SerializedName("user_id")
        val userId: String,

        @SerializedName("email")
        val email: String,

        @SerializedName("nickname")
        val nickname: String,

        @SerializedName("address")
        val address: Address

)