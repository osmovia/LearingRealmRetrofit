package com.example.learingrealmandretrofit.objects.request

data class SignInRequest(
    val session: SessionRequest,
    val user: UserSignInRequest
)
