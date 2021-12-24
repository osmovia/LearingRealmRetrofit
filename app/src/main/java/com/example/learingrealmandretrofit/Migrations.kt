package com.example.learingrealmandretrofit

import io.realm.FieldAttribute
import io.realm.RealmMigration

val migration = RealmMigration { realm, oldVersion, _ ->

    var oldVersion = oldVersion
    val schema = realm.schema

    if (oldVersion == 0L) {
        oldVersion++
    }

    if (oldVersion == 1L) {
        val cardSchema = schema.get("CardRealm")
        val deckSchema = schema.get("DeckRealm")

        cardSchema?.className = "Card"
        deckSchema?.className = "Deck"

        oldVersion++
    }

    if (oldVersion == 2L) {
        val cardSchema = schema.get("Card")
        val deckSchema = schema.get("Deck")

        deckSchema?.addRealmListField("cards", cardSchema)

        oldVersion++
    }

    if (oldVersion == 3L) {
        val cardSchema = schema.get("Card")
        val deckSchema = schema.get("Deck")

        val cardDeckSchema = schema.create("CardDeck")
        cardDeckSchema.addField("id", Int::class.java, FieldAttribute.PRIMARY_KEY)
        cardDeckSchema.addField("deckId", Int::class.java)
        cardDeckSchema.addField("cardId", Int::class.java)

        deckSchema?.removeField("cards")

        cardSchema?.addRealmListField("cardsDecks", cardDeckSchema)
        deckSchema?.addRealmListField("cardsDecks", cardDeckSchema)
    }
}
