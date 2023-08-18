package com.umc.insider.retrofit.model

import com.google.gson.annotations.SerializedName

data class ChatRoomsPostReq(

    @SerializedName("sellerId")
    val sellerId : Long,

    @SerializedName("buyerId")
    val buyerId : Long,

    @SerializedName("goodsId")
    val goodsId : Long
)
