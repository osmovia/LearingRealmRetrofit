package com.example.learingrealmandretrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BaseApi {
    private const val baseUrl: String = "http://10.0.1.83:3000/api/v1/"
    val retrofit: ApiInterface = Retrofit
        .Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiInterface::class.java)
}