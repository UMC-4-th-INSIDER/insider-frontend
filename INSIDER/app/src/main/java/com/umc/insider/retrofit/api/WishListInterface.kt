package com.umc.insider.retrofit.api

import com.umc.insider.retrofit.model.GoodsGetRes
import com.umc.insider.retrofit.model.WishListGetRes
import com.umc.insider.retrofit.model.WishListPostReq
import com.umc.insider.retrofit.model.WishListPostRes
import com.umc.insider.retrofit.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WishListInterface {

    @POST("/wishlist/create")
    suspend fun addGoodsToWishList(@Body postWishListsReq: WishListPostReq): Response<BaseResponse<WishListPostRes>>

    @GET("/wishlist/{userId}")
    suspend fun getGoodsInWishList (@Path("userId") userId : Long) : Response<List<GoodsGetRes>>

    @DELETE("/wishlist/delete/{userId}/{goodsId}")
    suspend fun deleteWishList(@Path("userId") userId: Long, @Path("goodsId") goodsId: Long): Response<String>
}