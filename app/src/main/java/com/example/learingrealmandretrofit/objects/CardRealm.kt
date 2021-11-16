package com.example.learingrealmandretrofit.objects


import io.realm.RealmObject
import io.realm.annotations.Required
import java.io.Serializable
import com.google.gson.Gson
import io.realm.annotations.PrimaryKey

open class CardRealm(
    @PrimaryKey
    var id: Int = 0,
    @Required
    var word: String = "",
    @Required
    var translation: String = "",
    @Required
    var example: String = ""
) : RealmObject(), Serializable{
    override fun toString(): String {
        return Gson().toJson(this)
    }
}