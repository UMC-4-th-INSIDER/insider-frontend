package com.umc.insider.retrofit.api

import com.umc.insider.retrofit.model.ExchangesPostRes
import com.umc.insider.retrofit.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ExchangesInterface {

    @Multipart
    @POST("/exchanges/create")
    suspend fun createExchanges(
        @Part("postExchangesReq") postExchangesReq: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<BaseResponse<ExchangesPostRes>>

    @GET("/exchanges/read")
    suspend fun getAllExchanges(@Query("title") title: String?) : BaseResponse<List<ExchangesPostRes>>

    @GET("/exchanges/category/{category_id}")
    suspend fun getExchangesByCategoryId(@Path("category_id") category_id : Long) : Response<List<ExchangesPostRes>>
}