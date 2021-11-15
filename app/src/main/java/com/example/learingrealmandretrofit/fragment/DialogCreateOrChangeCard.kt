package com.example.learingrealmandretrofit.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.learingrealmandretrofit.ConfigRealm
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.DialogCreateOrChangeCardBinding
import com.example.learingrealmandretrofit.objects.Card
import com.example.learingrealmandretrofit.objects.CardRealm
import com.example.learingrealmandretrofit.objects.response.CardResponse
import com.example.learingrealmandretrofit.showErrorCodeToast
import com.example.learingrealmandretrofit.showErrorToast
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DialogCreateOrChangeCard : DialogFragment() {
    companion object {
        const val cardRealmKey = "CARD_REALM_KEY"
    }

    private val card: CardRealm?
        get() = arguments?.getSerializable(cardRealmKey) as? CardRealm?
    private lateinit var binding: DialogCreateOrChangeCardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreateOrChangeCardBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fillFields()
        binding.buttonCreateOrUpdateCard.setOnClickListener {
            if (card == null) {
                createCardRetrofit()
            } else {
                updateCardRetrofit()
            }
        }
    }

    private fun createCardRealm(card: Card) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val cardRealm = CardRealm(
                id = card.id,
                word = card.word,
                example = card.example,
                translation = card.translation
            )
            realmTransaction.insert(cardRealm)
        }, {
            findNavController().popBackStack()
            realm.close()
        }, {
            findNavController().popBackStack()
            realm.close()
        })
    }

    private fun createCardRetrofit() {
        val word = binding.editTextOriginalWord.text.toString()
        val translate = binding.editTextTranslateWord.text.toString()
        val example = binding.editTextExample.text.toString()
        val card = Card(word = word, translation = translate, example = example)
        BaseApi.retrofit.createCard(card).enqueue(object : Callback<CardResponse?> {
            override fun onFailure(call: Call<CardResponse?>, t: Throwable) {
                checkInternet()
            }
            override fun onResponse(call: Call<CardResponse?>, response: Response<CardResponse?>) {
                val statusCode = response.code()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        createCardRealm(responseBody.card)
                    } else {
                        errorServer(statusCode)
                    }
                } else {
                    errorServer(statusCode)
                }
            }
        })
    }

    private fun updateCardRetrofit() {
        val word = binding.editTextOriginalWord.text.toString()
        val translate = binding.editTextTranslateWord.text.toString()
        val example = binding.editTextExample.text.toString()
        val id = card!!.id
        val card = Card(word = word, translation = translate, example = example)
        BaseApi.retrofit.updateCard(id, card).enqueue(object : Callback<CardResponse?> {
            override fun onResponse(call: Call<CardResponse?>, response: Response<CardResponse?>) {
                val statusCode = response.code()
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        updateCardRealm(responseBody.card)
                    } else {
                        errorServer(statusCode)
                    }
                } else {
                    errorServer(statusCode)
                }
            }
            override fun onFailure(call: Call<CardResponse?>, t: Throwable) {
                checkInternet()
            }
        })
    }

    private fun updateCardRealm(card: Card) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val result = realmTransaction
                .where(CardRealm::class.java)
                .equalTo("id", card.id)
                .findFirst()
            result?.example = card.example
            result?.translation = card.translation
            result?.word = card.word
        }, {
            findNavController().popBackStack()
            realm.close()
        },{
            findNavController().popBackStack()
            realm.close()
        })
    }

    private fun fillFields() {
        if (card != null && card?.id != null) {
            binding.editTextOriginalWord.setText(card?.word.toString())
            binding.editTextTranslateWord.setText(card?.translation.toString())
            binding.editTextExample.setText(card?.example.toString())
        }
    }

    private fun errorServer(statusCode: Int) {
        context?.showErrorCodeToast(statusCode)
        findNavController().popBackStack()
    }

    private fun checkInternet() {
        context?.showErrorToast()
        findNavController().popBackStack()
    }
}


