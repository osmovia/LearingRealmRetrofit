package com.example.learingrealmandretrofit.api

import com.example.learingrealmandretrofit.objects.CardParameters
import com.example.learingrealmandretrofit.objects.DeckParameters
import com.example.learingrealmandretrofit.objects.request.DeckCreateRequest
import com.example.learingrealmandretrofit.objects.request.SignInRequest
import com.example.learingrealmandretrofit.objects.request.SignInUpRequest
import com.example.learingrealmandretrofit.objects.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    companion object {
        private const val tokenKey: String = "X-Session-Token"
    }

    @GET("cards")
    fun getCards(@Header(tokenKey) token: String): Call<CardListResponse>

    @GET("decks")
    fun getDeck(@Header(tokenKey) token: String): Call<DeckListResponse>

    @POST("cards")
    fun createCard(@Header(tokenKey) token: String, @Body params: CardParameters): Call<CardResponse>

    @POST("decks")
    fun createdDeck(@Header(tokenKey) token: String, @Body params: DeckCreateRequest): Call<DeckResponse>

    @POST("users")
    fun createUser(@Body params: SignInUpRequest): Call<SignUpResponse>

    @POST("sessions")
    fun signIn(@Body params: SignInRequest):  Call<SignUpResponse>

    @PUT("cards/{id}")
    fun updateCard(@Header(tokenKey) token: String,@Path("id") id : Int, @Body params : CardParameters): Call<CardResponse>

    @DELETE("cards/{id}")
    fun deleteCard(@Header(tokenKey) token: String, @Path("id") id: Int): Call<CardResponse>

    @DELETE("decks/{id}")
    fun deleteDeck(@Path("id") id : Int): Call<Success>

    @DELETE("sessions/{token}")
    fun logOut(@Header(tokenKey) tokenSession: String, @Path("token") token: String): Call<Success>

}
