package com.umc.insider.retrofit.api

import com.umc.insider.retrofit.model.GoodsGetRes
import com.umc.insider.retrofit.response.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoodsInterface {

    @GET("/goods/read")
    suspend fun getGoods(@Query("title") title: String?): BaseResponse<List<GoodsGetRes>>
}