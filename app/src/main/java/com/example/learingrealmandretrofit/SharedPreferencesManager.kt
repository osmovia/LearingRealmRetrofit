package com.example.learingrealmandretrofit

import android.content.Context
import android.content.SharedPreferences
import com.example.learingrealmandretrofit.objects.SessionSharedPreferencesRead

class SharedPreferencesManager(context: Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun saveAuthentication(token: String, email: String, userId: Int) {
        val editor = sharedPreferences.edit()
        editor.putString(Constants.USER_TOKEN_KEY, token)
        editor.putString(Constants.USER_EMAIL_KEY, email)
        editor.putInt(Constants.USER_KEY_ID, userId)
        editor.apply()
    }

    fun fetchAuthentication(): SessionSharedPreferencesRead {
        val token = sharedPreferences.getString(Constants.USER_TOKEN_KEY, null)
        val email = sharedPreferences.getString(Constants.USER_EMAIL_KEY, null)
        val userId = sharedPreferences.getInt(Constants.USER_KEY_ID, 0)
        return SessionSharedPreferencesRead(sessionToken = token, email = email, userId = userId)
    }

    fun clearSharedPreferences() {
        sharedPreferences.edit().clear().apply()
    }
}
