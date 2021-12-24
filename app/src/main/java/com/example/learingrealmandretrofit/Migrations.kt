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
        oldVersion++
    }
    if (oldVersion == 3L) {
        val deckSchema = schema.get("Deck")
        val cardSchema = schema.get("Card")

        schema.create("CardDeck")
            .addField("id", Int::class.java, FieldAttribute.PRIMARY_KEY)
            .addField("deckId", Int::class.java)
            .addField("cardId", Int::class.java)


        deckSchema?.removeField("cards")?.addRealmListField("cardsDecks", schema.get("CardDeck"))
        cardSchema?.addRealmListField("cardsDecks", schema.get("CardDeck"))
    }
}
