package com.example.learingrealmandretrofit.api

import com.example.learingrealmandretrofit.objects.Card
import com.example.learingrealmandretrofit.objects.Deck
import com.example.learingrealmandretrofit.objects.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @GET("cards")
    fun getCards(): Call<CardListResponse>

    @GET("decks")
    fun getDeck(): Call<DeckListResponse>

    @POST("cards")
    fun createCard(@Body params: Card): Call<CardResponse>

    @POST("decks")
    fun createdDeck(@Body params: Deck): Call<DeckResponse>

    @POST("users")
    fun createUser(@Body params: UserResponse): Call<Success>

    @PUT("cards/{id}")
    fun updateCard(@Path("id") id : Int, @Body params : Card): Call<CardResponse>

    @DELETE("cards/{id}")
    fun deleteCard(@Path("id") id: Int): Call<Success>

    @DELETE("decks/{id}")
    fun deleteDeck(@Path("id")id : Int): Call<Success>

}
