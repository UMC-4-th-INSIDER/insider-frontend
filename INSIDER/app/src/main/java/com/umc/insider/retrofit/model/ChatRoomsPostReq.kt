package com.umc.insider.retrofit.model

import com.google.gson.annotations.SerializedName

data class ChatRoomsPostReq(

    @SerializedName("sellerIdx")
    val sellerIdx : Long,

    @SerializedName("buyerIdx")
    val buyerIdx : Long
)
