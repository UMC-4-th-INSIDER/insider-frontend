package com.umc.insider.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{

        // 에뮬레이터
        private const val BASE_URL = "http://10.0.2.2:8080/"
        // 해당 ip
<<<<<<< HEAD
        //private const val BASE_URL = "http://172.30.1.44:8080/"
=======
        //private const val BASE_URL = "http://165.246.130.26:8080/"
>>>>>>> 10f90bbb754890b120e20be2579e83fc4b5d705c
        // aws 탄력 ip
        //private const val BASE_URL = "http://15.164.46.63:8080/"

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