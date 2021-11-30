package com.example.learingrealmandretrofit

import android.content.Context
import android.content.SharedPreferences
import com.example.learingrealmandretrofit.objects.UserSharedPref

class SharedPreferencesManager(context: Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val userTokenKey = "USER_TOKEN"
        const val userIdKey = "USER_ID"
        const val userEmailKey = "USER_EMAIL"
    }

    fun saveAuthentication(token: String, email: String, idUser: Int) {
        val editor = sharedPreferences.edit()
        editor.putString(userTokenKey, token)
        editor.putString(userEmailKey, email)
        editor.putInt(userIdKey, idUser)
        editor.apply()
    }

    fun fetchAuthentication(): UserSharedPref {
        val token = sharedPreferences.getString(userTokenKey, null)
        val email = sharedPreferences.getString(userEmailKey, null)
        val userId = sharedPreferences.getInt(userIdKey, 0)
        return UserSharedPref(token = token, email = email, usrId = userId)
    }

    fun clearSharedPreferences() {
        sharedPreferences.edit().clear().apply()
    }
}
