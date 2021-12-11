package com.example.learingrealmandretrofit

import android.app.Activity
import android.content.Context
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.learingrealmandretrofit.objects.SessionSharedPreferencesRead

fun Context.showErrorToast(text: Int = R.string.network_error_message) = Toast.makeText(
    this,
    getString(text),
    Toast.LENGTH_LONG
).show()

fun Context.showErrorCodeToast(code: Int) = Toast.makeText(
    this,
    getString(R.string.code_error_message, code),
    Toast.LENGTH_LONG
).show()

fun Context.showErrorCodeOrStringResource(message: Any) = when (message) {
    is String -> Toast.makeText(
        this,
        getString(R.string.code_error_message, message.toInt()),
        Toast.LENGTH_LONG
    ).show()
    is Int -> Toast.makeText(
        this,
        message,
        Toast.LENGTH_LONG
    ).show()
    else -> throw Exception("Use only int or string")
}

fun Activity.showProgress() {
    this.findViewById<FrameLayout>(R.id.mainProgressBarHolder).isVisible = true
}

fun Activity.hideProgress() {
    this.findViewById<FrameLayout>(R.id.mainProgressBarHolder).isVisible = false
}

fun Context.user(): SessionSharedPreferencesRead {
    return SharedPreferencesManager(this).fetchAuthentication()
}
