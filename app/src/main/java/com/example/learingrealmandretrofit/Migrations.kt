package com.example.learingrealmandretrofit

import io.realm.RealmMigration

val migration = RealmMigration { realm, oldVersion, _ ->
    if (oldVersion == 1L) {
        val deckSchema = realm.schema.get("DeckRealm")
        val cardSchema = realm.schema.get("CardRealm")

        deckSchema?.className = "Deck"
        cardSchema?.className = "Card"
    }
    if (oldVersion == 2L) {
        val deckSchema = realm.schema.get("Deck")
        val cardSchema = realm.schema.get("Card")

        deckSchema?.addRealmListField("cards", cardSchema)
    }
}
