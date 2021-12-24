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

fun getRandomColorGradient(position: Int): Int {
    val listColor = listOf(
        R.drawable.gradient_1,
        R.drawable.gradient_2,
        R.drawable.gradient_3,
        R.drawable.gradient_4,
        R.drawable.gradient_5,
        R.drawable.gradient_6,
        R.drawable.gradient_7,
        R.drawable.gradient_8,
        R.drawable.gradient_9,
        R.drawable.gradient_10,
        R.drawable.gradient_11,
        R.drawable.gradient_12,
        R.drawable.gradient_13,
        R.drawable.gradient_14,
        R.drawable.gradient_15,
        R.drawable.gradient_16,
    )
    return if (position <= 15) {
        listColor[position]
    } else {
        listColor[position % listColor.size]
    }
}
