package com.example.learingrealmandretrofit.objects.request

data class SignInUpRequest(
    val session: SessionRequest,
    val user: UserSignUpRequest
)


