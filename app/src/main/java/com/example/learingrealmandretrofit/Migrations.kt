package com.example.learingrealmandretrofit

import io.realm.RealmMigration

val migration = RealmMigration { realm, oldVersion, _ ->

    var oldVersion = oldVersion
    val schema = realm.schema

    if (oldVersion == 0L) {
        oldVersion++
    }

    if (oldVersion == 1L) {
        val deckSchema = schema.get("DeckRealm")
        val cardSchema = schema.get("CardRealm")

        deckSchema?.className = "Deck"
        cardSchema?.className = "Card"
        oldVersion++
    }
    if (oldVersion == 2L) {
        val deckSchema = schema.get("Deck")
        val cardSchema = schema.get("Card")

        deckSchema?.addRealmListField("cards", cardSchema)
    }
}
