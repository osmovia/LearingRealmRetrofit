package com.example.learingrealmandretrofit.card

import com.example.learingrealmandretrofit.objects.CardDeck
import io.realm.RealmObject
import io.realm.annotations.Required
import java.io.Serializable
import com.google.gson.Gson
import io.realm.RealmList
import io.realm.annotations.PrimaryKey

open class Card(
    @PrimaryKey
    var id: Int = 0,

    @Required
    var word: String = "",

    @Required
    var translation: String = "",

    var example: String = "",

    var cardDecks: RealmList<CardDeck> = RealmList()

) : RealmObject(), Serializable {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
