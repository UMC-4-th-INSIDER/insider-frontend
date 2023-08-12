package com.umc.insider.retrofit.model

import com.google.gson.annotations.SerializedName

data class GoodsGetRes (

    @SerializedName("title")
    val title : String,

    @SerializedName("price")
    val price : String,

    @SerializedName("weight")
    val weight : String,

    @SerializedName("rest")
    val rest : Int,

    @SerializedName("shelf_life")
    val shelf_life : String,

    @SerializedName("img_url")
    val img_url : String

)
