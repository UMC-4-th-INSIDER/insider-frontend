package com.umc.insider.retrofit.model

import com.google.gson.annotations.SerializedName

data class SignUpPostReq(

    @SerializedName("userId")
    val userId : String,

    @SerializedName("nickname")
    val nickname : String,

    @SerializedName("pw")
    val pw : String,

    @SerializedName("email")
    val email : String

)
