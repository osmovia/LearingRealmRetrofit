package com.example.learingrealmandretrofit.objects

import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.deck.Deck
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.Index
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class CardDeck(
    @PrimaryKey
    var id: Int = 0,

    @Index
    var deckId: Int = 0,

    @Index
    var cardId: Int = 0,

) : RealmObject() {
    @LinkingObjects("cardDecks")
    val card: RealmResults<Card>? = null

    @LinkingObjects("cardDecks")
    val deck: RealmResults<Deck>? = null
}
