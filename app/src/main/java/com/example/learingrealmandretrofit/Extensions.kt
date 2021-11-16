package com.example.learingrealmandretrofit

import android.content.Context
import android.widget.Toast

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