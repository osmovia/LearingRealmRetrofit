package com.example.learingrealmandretrofit.objects.request

import com.google.gson.annotations.SerializedName

data class SessionRequest(
    @SerializedName("operational_system")
    val operationalSystem: String = "Android"
)
