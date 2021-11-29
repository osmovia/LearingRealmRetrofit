package com.example.learingrealmandretrofit

import android.content.Context
import android.content.SharedPreferences
import com.example.learingrealmandretrofit.objects.UserSharedPref

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val userTokenKey = "USER_TOKEN"
        const val userIdKey = "USER_ID"
        const val userEmailKey = "USER_EMAIL"
    }

    fun saveAuth(token: String, email: String, idUser: Int) {
        val editor = prefs.edit()
        editor.putString(userTokenKey, token)
        editor.putString(userEmailKey, email)
        editor.putInt(userIdKey, idUser)
        editor.apply()
    }

    fun fetchAuth(): UserSharedPref {
        val token = prefs.getString(userTokenKey, null)
        val email = prefs.getString(userEmailKey, null)
        val userId = prefs.getInt(userIdKey, 0)
        return UserSharedPref(token = token, email = email, usrId = userId)
    }

    fun clearSharedPreferences() {
        prefs.edit().clear().apply()
    }
}
