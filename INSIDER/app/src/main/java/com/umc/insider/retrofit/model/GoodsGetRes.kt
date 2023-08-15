package com.umc.insider.retrofit.model

import com.google.gson.annotations.SerializedName
import com.umc.insider.model.Markets
import com.umc.insider.model.Users

data class GoodsGetRes (

    @SerializedName("users_id")
    val users_id : Users,

    @SerializedName("markets_id")
    val markets_id : Markets,

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

    @SerializedName("sale")
    val sale : Integer,

    @SerializedName("img_url")
    val img_url : String

)
