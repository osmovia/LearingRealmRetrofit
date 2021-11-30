package com.example.learingrealmandretrofit.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.DialogCreateOrChangeCardBinding
import com.example.learingrealmandretrofit.objects.CardParameters
import com.example.learingrealmandretrofit.objects.Card
import com.example.learingrealmandretrofit.objects.response.CardResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DialogCreateOrChangeCard : DialogFragment() {
    companion object {
        const val cardRealmKey = "CARD_REALM_KEY"
    }

    private val card: Card?
        get() = arguments?.getSerializable(cardRealmKey) as? Card?
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

    private fun createCardRealm(card: CardParameters) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val cardRealm = Card(
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
        val card = CardParameters(word = word, translation = translate, example = example)
        val token = context?.user()?.token ?: ""
        requireActivity().showProgress()
        BaseApi.retrofit.createCard(token = token, params = card).enqueue(object : Callback<CardResponse?> {
            override fun onResponse(call: Call<CardResponse?>, response: Response<CardResponse?>) {
                val statusCode = response.code()
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    requireActivity().hideProgress()
                    createCardRealm(responseBody.card)
                } else {
                    errorServer(statusCode)
                }
            }
            override fun onFailure(call: Call<CardResponse?>, t: Throwable) {
                checkInternet()
            }
        })
    }

    private fun updateCardRetrofit() {
        val word = binding.editTextOriginalWord.text.toString()
        val translate = binding.editTextTranslateWord.text.toString()
        val example = binding.editTextExample.text.toString()
        val id = card?.id ?: 0
        val card = CardParameters(word = word, translation = translate, example = example)
        val token = context?.user()?.token ?: ""
        requireActivity().showProgress()
        BaseApi.retrofit.updateCard(id = id, params = card , token = token).enqueue(object : Callback<CardResponse?> {
            override fun onResponse(call: Call<CardResponse?>, response: Response<CardResponse?>) {
                val statusCode = response.code()
                val responseBody = response.body()
                if(response.isSuccessful && responseBody != null){
                    requireActivity().hideProgress()
                    updateCardRealm(responseBody.card)
                    } else {
                    errorServer(statusCode)
                }
            }
            override fun onFailure(call: Call<CardResponse?>, t: Throwable) {
                checkInternet()
            }
        })
    }

    private fun updateCardRealm(card: CardParameters) {
        val config = ConfigRealm.config
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val result = realmTransaction
                .where(Card::class.java)
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
        requireActivity().hideProgress()
        context?.showErrorCodeToast(statusCode)
        findNavController().popBackStack()
    }

    private fun checkInternet() {
        requireActivity().hideProgress()
        context?.showErrorToast()
        findNavController().popBackStack()
    }
}
