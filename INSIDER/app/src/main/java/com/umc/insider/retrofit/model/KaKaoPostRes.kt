package com.umc.insider.retrofit.model

import com.google.gson.annotations.SerializedName

data class KaKaoPostRes(

    @SerializedName("userId")
    val userId: String,

    @SerializedName("pw")
    val pw: String,
)
