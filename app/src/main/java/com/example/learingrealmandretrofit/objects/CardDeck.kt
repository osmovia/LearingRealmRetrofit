package com.example.learingrealmandretrofit.objects

import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.deck.Deck
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class CardDeck(
    @PrimaryKey
    var id: Int = 0,

    var deckId: Int = 0,

    var cardId: Int = 0,

    @LinkingObjects("cardsDecks")
    val cards: RealmResults<Card>? = null,

    @LinkingObjects("cardsDecks")
    val decks: RealmResults<Deck>? = null
    ) : RealmObject()
