package com.example.learingrealmandretrofit.objects.request

data class SignUpRequest(
    val session: SessionRequest,
    val user: UserSignUpRequest
)

