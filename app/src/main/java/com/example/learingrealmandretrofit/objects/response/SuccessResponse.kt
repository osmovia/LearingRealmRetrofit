package com.example.learingrealmandretrofit.objects.response

import com.google.gson.Gson
import java.io.Serializable

data class SuccessResponse(val success: Boolean) : Serializable {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
