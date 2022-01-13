package com.example.learingrealmandretrofit

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(context: Context) : Interceptor {

    private val sessionManager = SharedPreferencesManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()

        sessionManager.fetchAuthentication().sessionToken?.let {
            requestBuilder.addHeader(Constants.X_SESSION_TOKEN, it)
        }

        return chain.proceed(requestBuilder.build())
    }
}
