package com.example.learingrealmandretrofit

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val token: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()
                .addHeader(Constants.X_SESSION_TOKEN, token)
                .build()

        return chain.proceed(requestBuilder)
    }
}
