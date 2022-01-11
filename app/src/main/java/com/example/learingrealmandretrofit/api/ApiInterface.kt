package com.example.learingrealmandretrofit.api

import com.example.learingrealmandretrofit.objects.CardParameters
import com.example.learingrealmandretrofit.objects.request.*
import com.example.learingrealmandretrofit.objects.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST("users")
    fun createUser(
        @Body params: SignUpRequest
    ): Call<AuthenticationResponse>

    @POST("sessions")
    fun signIn(
        @Body params: SignInRequest
    ):  Call<AuthenticationResponse>

    @DELETE("sessions/{token}")
    fun signOut(
        @Path("token") token: String
    ): Call<SuccessResponse>

    @GET("cards")
    fun getCards(
    ): Call<CardListResponse>

    @POST("cards")
    fun createCard(
        @Body params: CardParameters
    ): Call<CardResponse>

    @PUT("cards/{id}")
    fun updateCard(
        @Path("id") id: Int,
        @Body params: CardParameters
    ): Call<CardResponse>

    @DELETE("cards/{id}")
    fun deleteCard(
        @Path("id") id: Int
    ): Call<CardResponse>

    @PUT("cards/{id}/decks")
    fun updateDecksForCardResponse(
        @Path("id") id: Int,
        @Body params: AddCardToDecksRequest
    ) : Call<AddCardToDecksResponse>

    @GET("decks")
    fun getDecks(
    ): Call<DeckListResponse>

    @GET("decks/{id}")
    fun getDeck(
        @Path("id") id: Int
    ): Call<DeckResponse>

    @POST("decks")
    fun createdDeck(
        @Body params: DeckCreateOrUpdateRequest
    ): Call<DeckResponse>

    @DELETE("decks/{id}")
    fun deleteDeck(
        @Path("id") id: Int
    ): Call<DeckResponse>

    @PUT("decks/{id}")
    fun updateDeck(
        @Path("id") id: Int,
        @Body params: DeckCreateOrUpdateRequest
    ): Call<DeckResponse>

    @POST("decks/{deckId}/cards/{cardId}/add")
    fun addCardToDeck(
        @Path("deckId") deckId: Int,
        @Path("cardId") cardId: Int
    ): Call<SuccessResponse>

    @DELETE("decks/{deckId}/cards/{cardId}/remove")
    fun removeCardFromDeck(
        @Path("deckId") deckId: Int,
        @Path("cardId") cardId: Int
    ): Call<SuccessResponse>

    @GET("card_decks")
    fun getCardDecks(
    ): Call<CardDeckListResponse>

    @POST("card_decks")
    fun createCardDeck(
        @Body params: CardDeckRequest
    ): Call<CardDeckMainResponse>

    @DELETE("card_decks/{id}")
    fun deleteCardDeck(
        @Path("id") id: Int
    ) : Call<CardDeckMainResponse>
}
