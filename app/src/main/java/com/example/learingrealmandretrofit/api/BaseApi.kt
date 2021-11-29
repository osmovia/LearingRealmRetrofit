package com.example.learingrealmandretrofit.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val prefix = "api/v1"
private const val baseUrlHeroku: String = "https://word-notes.herokuapp.com"
private const val baseUrlLocal: String = "http://10.0.1.4:3000"

object BaseApi {
    val retrofit: ApiInterface = Retrofit
        .Builder()
        .baseUrl("${baseUrlLocal}/${prefix}/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiInterface::class.java)
}
