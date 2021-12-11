package com.example.learingrealmandretrofit.api

import com.example.learingrealmandretrofit.objects.CardParameters
import com.example.learingrealmandretrofit.objects.request.DeckCreateOrUpdateRequest
import com.example.learingrealmandretrofit.objects.request.SignInRequest
import com.example.learingrealmandretrofit.objects.request.SignUpRequest
import com.example.learingrealmandretrofit.objects.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    companion object {
        private const val tokenKey: String = "X-Session-Token"
    }

    @POST("users")
    fun createUser(
        @Body params: SignUpRequest): Call<AuthenticationResponse>

    @POST("sessions")
    fun signIn(
        @Body params: SignInRequest):  Call<AuthenticationResponse>

    @DELETE("sessions/{token}")
    fun signOut(
        @Header(tokenKey) tokenSession: String,
        @Path("token") token: String): Call<SuccessResponse>

    @GET("cards")
    fun getCards(
        @Header(tokenKey) token: String): Call<CardListResponse>

    @POST("cards")
    fun createCard(
        @Header(tokenKey) token: String,
        @Body params: CardParameters): Call<CardResponse>

    @PUT("cards/{id}")
    fun updateCard(
        @Header(tokenKey) token: String,
        @Path("id") id: Int,
        @Body params: CardParameters): Call<CardResponse>

    @DELETE("cards/{id}")
    fun deleteCard(
        @Header(tokenKey) token: String,
        @Path("id") id: Int): Call<CardResponse>

    @GET("decks")
    fun getDecks(
        @Header(tokenKey) token: String): Call<DeckListResponse>

    @POST("decks")
    fun createdDeck(
        @Header(tokenKey) token: String,
        @Body params: DeckCreateOrUpdateRequest): Call<DeckCreateOrUpdateResponse>

    @DELETE("decks/{id}")
    fun deleteDeck(
        @Header(tokenKey) token: String,
        @Path("id") id: Int): Call<DeckCreateOrUpdateResponse>

    @PUT("decks/{id}")
    fun updateDeck(
        @Header(tokenKey) token: String,
        @Path("id") id: Int,
        @Body params: DeckCreateOrUpdateRequest): Call<DeckCreateOrUpdateResponse>
}
