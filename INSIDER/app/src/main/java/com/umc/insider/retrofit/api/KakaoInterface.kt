package com.umc.insider.retrofit.api

import com.umc.insider.retrofit.model.LoginPostRes
import com.umc.insider.retrofit.response.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoInterface {

    @GET("/oauth2/callback/kakao")
    suspend fun kakaoCallback(@Query("code") code: String) : BaseResponse<LoginPostRes>
}