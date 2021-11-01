package com.example.learingrealmandretrofit.objects


import io.realm.RealmObject
import io.realm.annotations.Required
import java.io.Serializable
import com.google.gson.Gson

// TODO: Rename to "Card"
open class Card(
    @Required
    var id: Int? = null,
    var word: String = "",
    var translation: String = "",
    var example: String = ""
) : RealmObject(), Serializable{
    override fun toString(): String {
        return Gson().toJson(this)
    }
}