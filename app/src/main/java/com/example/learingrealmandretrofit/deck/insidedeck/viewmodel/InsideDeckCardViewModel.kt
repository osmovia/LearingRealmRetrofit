package com.example.learingrealmandretrofit.deck.insidedeck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.deck.Deck
import com.example.learingrealmandretrofit.objects.CardParameters
import com.example.learingrealmandretrofit.objects.response.DeckGetOrCreateOrUpdateResponse
import io.realm.Realm
import io.realm.RealmList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsideDeckCardViewModel(private val token: String, private val deckId: Int) : ViewModel() {

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    private val _getAllCardsRealm = MutableLiveData<RealmList<Card>>()
    val getAllCardsRealm: LiveData<RealmList<Card>>
        get() = _getAllCardsRealm

    init {
        pullCardsRealm()
        getCardsInsideDeck()
    }


     private fun pullCardsRealm() {
         val config = ConfigurationRealm.configuration
         val realm = Realm.getInstance(config)
         _getAllCardsRealm.value = realm
             .where(Deck::class.java)
             .equalTo("id", deckId)
             .findFirst()
             ?.cards
    }

    private fun getCardsInsideDeck() {
        _showSpinner.value = true
        BaseApi.retrofit.getDeck(token = token, id = deckId)
            .enqueue(object : Callback<DeckGetOrCreateOrUpdateResponse?> {
            override fun onResponse(
                call: Call<DeckGetOrCreateOrUpdateResponse?>,
                response: Response<DeckGetOrCreateOrUpdateResponse?>
            ) {
                val responseBode = response.body()
                if (response.isSuccessful && responseBode != null) {
                    checkCurrentCardsInRealm(responseBode.deck.cards)
                } else {
                    _showToast.value = response.code().toString()
                    _showSpinner.value = false
                }
            }

            override fun onFailure(call: Call<DeckGetOrCreateOrUpdateResponse?>, t: Throwable) {
                _showToast.value = R.string.connection_issues
                _showSpinner.value = false
            }
        })
    }

    private fun checkCurrentCardsInRealm(cards: List<CardParameters>) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            for (card in cards) {
                val currentCard = realmTransaction
                    .where(Card::class.java)
                    .equalTo("id", card.id)
                    .findFirst()

                if (currentCard == null) {
                    val newCard = Card(
                        id = card.id,
                        word = card.word,
                        example = card.example,
                        translation = card.translation
                    )
                    realmTransaction.insert(newCard)
                }
            }
        }, {
            realm.close()
        }, {
            _showToast.value = R.string.problem_realm
            realm.close()
        })
        _showSpinner.value = false
    }

}
