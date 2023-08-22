package com.umc.insider.retrofit.model

import com.google.gson.annotations.SerializedName

data class WishListPostReq(

    @SerializedName("userId")
    val userId : Long,
    @SerializedName("goodsId")
    val goodsId : Long
)
