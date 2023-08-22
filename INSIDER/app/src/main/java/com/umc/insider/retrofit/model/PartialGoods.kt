package com.umc.insider.retrofit.model

import Category
import com.google.gson.annotations.SerializedName
import com.umc.insider.model.Markets
import com.umc.insider.model.Users
import java.sql.Timestamp

data class PartialGoods(
    @SerializedName("title")
    val title: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("weight")
    val weight: String?,

    @SerializedName("rest")
    val rest: Int?,

    @SerializedName("shelf_life")
    val shelfLife: String,

    @SerializedName("category_id")
    val categoryId: Long?,

    @SerializedName("imageUrl")
    val imageUrl: String?
)
