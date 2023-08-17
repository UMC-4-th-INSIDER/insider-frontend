package com.umc.insider.retrofit.model

import com.google.gson.annotations.SerializedName

data class ChatRoomsListRes (

    @SerializedName("chatRoomId")
    val chatRoomId : Long,

    @SerializedName("otherNickName")
    val otherNickName : String,

    @SerializedName("lastMessage")
    val lastMessage : String

)