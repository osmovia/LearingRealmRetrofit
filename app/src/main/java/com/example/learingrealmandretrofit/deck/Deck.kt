package com.example.learingrealmandretrofit.deck

import com.example.learingrealmandretrofit.objects.CardDeck
import com.google.gson.Gson
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.io.Serializable

open class Deck(
    @PrimaryKey
    var id: Int = 0,

    @Required
    var title: String = "",

    var cardDecks: RealmList<CardDeck> = RealmList()

) : RealmObject(), Serializable {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
