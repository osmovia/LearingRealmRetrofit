package com.example.learingrealmandretrofit.api

import android.content.Context
import com.example.learingrealmandretrofit.HeaderInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val prefix = "api/v1"
private const val baseUrlHeroku: String = "https://word-notes.herokuapp.com"

object BaseApi {

    fun retrofit(context: Context?): ApiInterface {
        if (context == null) {
            return Retrofit
                .Builder()
                .baseUrl("${baseUrlHeroku}/${prefix}/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        } else {
            return Retrofit
                .Builder()
                .baseUrl("${baseUrlHeroku}/${prefix}/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context))
                .build()
                .create(ApiInterface::class.java)
        }
    }
}

private fun okhttpClient(context: Context): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor(context))
        .build()
}

