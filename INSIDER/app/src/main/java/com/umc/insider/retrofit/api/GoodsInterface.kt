package com.umc.insider.retrofit.api

import com.umc.insider.retrofit.model.GoodsGetRes
import com.umc.insider.retrofit.model.GoodsPostRes
import com.umc.insider.retrofit.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File

interface GoodsInterface {

    @GET("/goods/read")
    suspend fun getGoods(@Query("title") title: String?): BaseResponse<List<GoodsGetRes>>

    @Multipart
    @POST("/goods/create")
    suspend fun createGoods(
        @Header("X-ACCESS-TOKEN") token: String,
        @Part("postgoodsReq") postgoodsReq: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<BaseResponse<GoodsPostRes>>

    @GET("/goods/{id}")
    suspend fun getGoodsById(@Path("id") id: Long): GoodsGetRes
}
