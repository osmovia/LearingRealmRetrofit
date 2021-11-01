package com.example.learingrealmandretrofit.api

import com.example.learingrealmandretrofit.api.ApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val prefix = "api/v1"
private const val baseUrl: String = "http://10.0.1.4:3000"

object BaseApi {
    val retrofit: ApiInterface = Retrofit
        .Builder()
        .baseUrl("${baseUrl}/${prefix}/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiInterface::class.java)
}