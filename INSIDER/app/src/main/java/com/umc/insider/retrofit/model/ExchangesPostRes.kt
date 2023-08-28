package com.umc.insider.retrofit.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ExchangesPostRes(

    @SerializedName("id")
    val id : Long,

    @SerializedName("title")
    val title : String,

    @SerializedName("imageUrl")
    val imageUrl : String,

    @SerializedName("name")
    val name : String,

    @SerializedName("count")
    val count : Int,

    @SerializedName("wantItem")
    val wantItem : String,

    @SerializedName("weight")
    val weight : String,

    @SerializedName("shelfLife")
    val shelfLife : String,

    @SerializedName("createdAt")
    val createdAt : Date,

    @SerializedName("categoryId")
    val categoryId : Long,

    @SerializedName("userId")
    val userId : Long,
)
