package com.example.learingrealmandretrofit.repository

import com.example.learingrealmandretrofit.api.RetrofitInstance
import com.example.learingrealmandretrofit.model.Post

class Repository {
    suspend fun getPost(): Post {
        return RetrofitInstance.api.getPost()
    }
}