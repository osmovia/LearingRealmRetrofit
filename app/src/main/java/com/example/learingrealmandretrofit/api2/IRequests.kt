package com.example.learingrealmandretrofit.api2

import com.example.learingrealmandretrofit.DataServer
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val prefix = "api/v1"

interface IRequests {

    @GET("${prefix}/cards/")
    fun getCards(): Call<DataServer>

    @GET("${prefix}/word/")
    fun getWord(
        @Query("id") id: Int
    ): Call<String>

    @DELETE("${prefix}/cards/{id}")
    fun deleteData(@Path("id") id: Int): Call<DataServer>
}