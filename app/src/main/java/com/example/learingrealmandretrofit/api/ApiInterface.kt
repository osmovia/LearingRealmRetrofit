package com.example.learingrealmandretrofit.api

import com.example.learingrealmandretrofit.objects.Card
import com.example.learingrealmandretrofit.objects.response.CardResponse
import com.example.learingrealmandretrofit.objects.response.CardListResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @GET("cards")
    fun getCards(): Call<CardListResponse>

    @POST("cards")
    fun createCard(@Body params: Card): Call<CardResponse>

    @PUT("cards/{id}")
    fun updateCard(@Path("id") id : Int, @Body params : Card): Call<CardResponse>

    @DELETE("cards/{id}")
    fun deleteCard(@Path("id") id: Int): Call<CardResponse>
}