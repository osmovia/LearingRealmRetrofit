package com.example.learingrealmandretrofit.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val prefix = "api/v1"
private const val baseUrl: String = "https://word-notes.herokuapp.com/"

object BaseApi {
    val retrofit: ApiInterface = Retrofit
        .Builder()
        .baseUrl("${baseUrl}/${prefix}/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiInterface::class.java)
}