package com.example.learingrealmandretrofit.objects

import com.google.gson.Gson
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.io.Serializable

open class Deck(
    @PrimaryKey
    var id: Int = 0,
    @Required
    var title: String = ""
) : RealmObject(), Serializable {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
