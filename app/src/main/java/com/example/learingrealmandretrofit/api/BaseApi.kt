package com.example.learingrealmandretrofit.api

import com.example.learingrealmandretrofit.HeaderInterceptor
import okhttp3.OkHttpClient
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

    fun retrofitHeader(token: String): ApiInterface {
        return Retrofit
            .Builder()
            .baseUrl("${baseUrlHeroku}/${prefix}/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient(token))
            .build()
            .create(ApiInterface::class.java)
    }
}

private fun okhttpClient(token: String): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor(token))
        .build()
}

