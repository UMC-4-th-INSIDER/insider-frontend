package com.umc.insider.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{

        private const val BASE_URL = "http://10.0.2.2:8080/"
        //private const val BASE_URL = "http://165.246.130.26:8080/"

        private var INSTANCE : Retrofit? = null

        fun getInstance() : Retrofit {
            if(INSTANCE == null){
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return INSTANCE!!
        }
    }
}