package com.example.learingrealmandretrofit.api

import com.example.learingrealmandretrofit.model.Post
import retrofit2.http.GET

interface SimpleApi {
    @GET("/api/v1/cards/")
    suspend fun getPost(): Post
}