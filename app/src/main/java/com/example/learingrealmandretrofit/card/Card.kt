package com.example.learingrealmandretrofit.card

import com.example.learingrealmandretrofit.deck.Deck
import io.realm.RealmObject
import io.realm.annotations.Required
import java.io.Serializable
import com.google.gson.Gson
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey

open class Card(
    @PrimaryKey
    var id: Int = 0,

    @Required
    var word: String = "",

    @Required
    var translation: String = "",

    @Required
    var example: String = "",

    @LinkingObjects("cards")
    val owner: RealmResults<Deck>? = null

) : RealmObject(), Serializable {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}