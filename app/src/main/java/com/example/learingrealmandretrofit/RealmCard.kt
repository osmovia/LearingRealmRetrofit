package com.example.learingrealmandretrofit


import io.realm.RealmObject
import io.realm.annotations.Required
import java.io.Serializable
import com.google.gson.Gson


open class RealmCard(
    @Required
    var id: Int? = null,
    var word: String? = null,
    var translation: String? = null,
    var example: String? = null
) : RealmObject(), Serializable{
    override fun toString(): String {
        return Gson().toJson(this)
    }
}