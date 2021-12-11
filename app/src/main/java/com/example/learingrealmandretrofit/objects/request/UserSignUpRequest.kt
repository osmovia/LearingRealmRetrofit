package com.example.learingrealmandretrofit.objects.request

import com.google.gson.annotations.SerializedName

data class UserSignUpRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("password_confirmation")
    val passwordConfirmation: String
)
