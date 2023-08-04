package com.umc.insider.retrofit.api

import com.umc.insider.retrofit.model.LoginPostReq
import com.umc.insider.retrofit.model.LoginPostRes
import com.umc.insider.retrofit.model.SignUpPostReq
import com.umc.insider.retrofit.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserInterface {

    // 회원가입
    @POST("/create")
    suspend fun createUser(@Body postUserReq: SignUpPostReq): Response<BaseResponse<SignUpPostReq>>

    @POST("/logIn")
    suspend fun logIn(@Body postLoginReq: LoginPostReq): Response<BaseResponse<LoginPostRes>>

}