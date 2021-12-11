package com.example.learingrealmandretrofit.objects.response

data class AuthenticationResponse(
    val session: SessionResponse,
    val user: UserAuthenticationResponse
)
