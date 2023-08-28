package com.umc.insider.retrofit.api

import com.umc.insider.retrofit.model.ExchangesPostRes
import com.umc.insider.retrofit.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ExchangesInterface {

    @Multipart
    @POST("/exchanges/create")
    suspend fun createExchanges(
        @Part("postExchangesReq") postExchangesReq: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<BaseResponse<ExchangesPostRes>>
}