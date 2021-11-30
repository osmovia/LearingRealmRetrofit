package com.example.learingrealmandretrofit.objects.response

data class SignUpResponse(
    val session: SessionResponse,
    val user: UserSignUpResponse
)
