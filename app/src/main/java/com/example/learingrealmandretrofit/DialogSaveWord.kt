package com.example.learingrealmandretrofit


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.FragmentSaveWordBinding
import com.example.learingrealmandretrofit.objects.Card
import com.example.learingrealmandretrofit.objects.response.CardResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DialogSaveWord : DialogFragment() {
    companion object {
        const val newWordKey = "NEW_WORD_KEY"
        const val changeWordKey = "CHANGE_WORD_KEY"
        const val cardRealmKey = "CARD_REALM_KEY"
        const val TAG = "DialogSaveWord"
    }

    private val card: Card?
        get() = arguments?.getSerializable(cardRealmKey) as? Card?
    lateinit var binding: FragmentSaveWordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveWordBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fillFields()
        binding.saveOrUpdateWord.setOnClickListener {
            if (card == null) {
                createNewCard()
            } else {
                updateCurrentCard()
            }
        }
    }

    private fun saveCardRealm(card: Card) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            realmTransaction.insert(card)
        }, {
            findNavController().popBackStack()
            realm.close()
        }, {
            realm.close()
        })

//        1. Init Realm
//        2. Create or update card
//        3. Check result, if ok - hide this fragment and show list of Cards to user.
    }

    private fun createNewCard() {
        val word = binding.editTextOriginalWord.text.toString()
        val translate = binding.editTextTranslateWord.text.toString()
        val example = binding.editTextExample.text.toString()
        val realmCard = Card(word = word, translation = translate, example = example)

        createNewCardRetrofit(realmCard)
    }

    private fun createNewCardRetrofit(card: Card) {
        BaseApi.retrofit.createCard(card).enqueue(object : Callback<CardResponse?> {
            override fun onFailure(call: Call<CardResponse?>, t: Throwable) {
                Log.d(TAG, "onFailure response dialog save word $t")
            }
            override fun onResponse(call: Call<CardResponse?>, response: Response<CardResponse?>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val statusCode = response.code()
                    if (responseBody != null) {
                        saveCardRealm(responseBody.card)
                        Log.d(TAG, "onResponse ok, status code: $statusCode ")
                    }
                }
            }
        })
    }

    private fun updateCurrentCard() {
        val word = binding.editTextOriginalWord.text.toString()
        val translate = binding.editTextTranslateWord.text.toString()
        val example = binding.editTextExample.text.toString()
        val id = card?.id
        val cardRealm = Card(word = word, translation = translate, example = example, id = id)
        updateCardRealm(cardRealm)
        Log.d(TAG, "Update current card 1")
        findNavController().previousBackStackEntry?.savedStateHandle?.set(changeWordKey, "")
        findNavController().popBackStack()
        Log.d(TAG, "Update current card 2")

    }

    private fun updateCardRealm(cardRealm: Card) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransaction { realmTransaction ->
            val result = realmTransaction
                .where(Card::class.java)
                .equalTo("id", cardRealm.id)
                .findFirst()
            result?.example = cardRealm.example
            result?.translation = cardRealm.translation
            result?.word = cardRealm.word
            Log.d(TAG, "Update card realm")
        }
    }

    private fun fillFields() {
        if (card != null) {
            binding.editTextOriginalWord.setText(card?.word.toString())
            binding.editTextTranslateWord.setText(card?.translation.toString())
            binding.editTextExample.setText(card?.example.toString())
        }
    }
}


