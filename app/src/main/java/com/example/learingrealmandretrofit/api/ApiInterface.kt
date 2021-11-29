package com.example.learingrealmandretrofit.api

import com.example.learingrealmandretrofit.objects.CardParameters
import com.example.learingrealmandretrofit.objects.DeckParameters
import com.example.learingrealmandretrofit.objects.request.SignInRequest
import com.example.learingrealmandretrofit.objects.request.SignInUpRequest
import com.example.learingrealmandretrofit.objects.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @GET("cards")
    fun getCards(): Call<CardListResponse>

    @GET("decks")
    fun getDeck(): Call<DeckListResponse>

    @POST("cards")
    fun createCard(@Body params: CardParameters): Call<CardResponse>

    @POST("decks")
    fun createdDeck(@Body params: DeckParameters): Call<DeckResponse>

    @POST("users")
    fun createUser(@Body params: SignInUpRequest): Call<SignUpResponse>

    @POST("sessions")
    fun signIn(@Body params: SignInRequest):  Call<SignUpResponse>

    @PUT("cards/{id}")
    fun updateCard(@Path("id") id : Int, @Body params : CardParameters): Call<CardResponse>

    @DELETE("cards/{id}")
    fun deleteCard(@Path("id") id: Int): Call<Success>

    @DELETE("decks/{id}")
    fun deleteDeck(@Path("id") id : Int): Call<Success>

    @DELETE("sessions/{token}")
    fun logOut(@Header("X-Session-Token") tokenSession: String, @Path("token") token: String): Call<Success>

}
