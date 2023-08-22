package com.umc.insider.retrofit.api

import com.umc.insider.retrofit.model.LoginPostReq
import com.umc.insider.retrofit.model.LoginPostRes
import com.umc.insider.retrofit.model.SignUpPostReq
import com.umc.insider.retrofit.model.UserGetByIdRes
import com.umc.insider.retrofit.model.UserPostRes
import com.umc.insider.retrofit.model.UserPutReq
import com.umc.insider.retrofit.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserInterface {

    // 회원가입
    @POST("/create")
    suspend fun createUser(@Body postUserReq: SignUpPostReq): Response<BaseResponse<SignUpPostReq>>

    @POST("/logIn")
    suspend fun logIn(@Body postLoginReq: LoginPostReq): Response<BaseResponse<LoginPostRes>>

    @GET("/user/{id}")
    suspend fun getUserById(@Path("id") id: Long): UserGetByIdRes

    @PUT("/user/modify")
    suspend fun modifyUser(@Body putUserReq: UserPutReq): BaseResponse<UserPostRes>


}