package com.example.learingrealmandretrofit

import android.content.Context
import android.content.SharedPreferences
import com.example.learingrealmandretrofit.objects.SessionSharedPreferencesRead

class SharedPreferencesManager(context: Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val userTokenKey = "com.example.learingrealmandretrofit.USER_TOKEN"
        const val userIdKey = "com.example.learingrealmandretrofit.USER_ID"
        const val userEmailKey = "com.example.learingrealmandretrofit.USER_EMAIL"
    }

    fun saveAuthentication(token: String, email: String, userId: Int) {
        val editor = sharedPreferences.edit()
        editor.putString(userTokenKey, token)
        editor.putString(userEmailKey, email)
        editor.putInt(userIdKey, userId)
        editor.apply()
    }

    fun fetchAuthentication(): SessionSharedPreferencesRead {
        val token = sharedPreferences.getString(userTokenKey, null)
        val email = sharedPreferences.getString(userEmailKey, null)
        val userId = sharedPreferences.getInt(userIdKey, 0)
        return SessionSharedPreferencesRead(sessionToken = token, email = email, userId = userId)
    }

    fun clearSharedPreferences() {
        sharedPreferences.edit().clear().apply()
    }
}
