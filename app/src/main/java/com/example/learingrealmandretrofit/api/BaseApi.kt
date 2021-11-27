package com.example.learingrealmandretrofit.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val prefix = "api/v1"
private const val baseUrlHeroku: String = "https://word-notes.herokuapp.com"

object BaseApi {
    val retrofit: ApiInterface = Retrofit
        .Builder()
        .baseUrl("${baseUrlHeroku}/${prefix}/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiInterface::class.java)
}
