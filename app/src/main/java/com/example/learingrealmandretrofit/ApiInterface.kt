package com.example.learingrealmandretrofit

import retrofit2.Call
import retrofit2.http.*
import java.lang.StringBuilder

interface ApiInterface {
    @GET("cards/")
    fun getData(): Call<DataServer>
    @DELETE("cards/{id}")
    fun deleteData(@Path("id") id: Int): Call<DataServer>
    @PATCH("cards/{id}")
    fun updateData(@Path("id") id : Int, @Body params : RealmCard): Call<DataServer>
    @POST("cards/")
    fun createData(@Body params: RealmCard): Call<DataServer>
    @GET("cards/")
    fun getWord(@Query("word") word: String): Call<DataServer>
}