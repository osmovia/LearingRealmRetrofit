package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.DialogCreateOrUpdateDeckBinding
import com.example.learingrealmandretrofit.objects.Deck
import com.example.learingrealmandretrofit.objects.request.DeckCreateRequest
import com.example.learingrealmandretrofit.objects.request.DeckTitleRequest
import com.example.learingrealmandretrofit.objects.response.DeckResponse
import com.example.learingrealmandretrofit.user
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DialogCreateOrChangeDeck : DialogFragment() {

    companion object{
        const val deckRealmKey = "DECK_KEY"
    }

    private val deck: Deck?
        get() = arguments?.getSerializable(deckRealmKey) as? Deck?
    private lateinit var binding : DialogCreateOrUpdateDeckBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreateOrUpdateDeckBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(deck != null) binding.editTextTitle.setText(deck?.title)
        binding.buttonCreateOrUpdateDeck.setOnClickListener {
            val token = context?.user()?.token ?: ""
            val request = DeckCreateRequest(deck = DeckTitleRequest("Hello"))
            BaseApi.retrofit.createdDeck(token = token, params = request).enqueue(object : Callback<DeckResponse?> {
                override fun onResponse(call: Call<DeckResponse?>, response: Response<DeckResponse?>
                ) {
                    val code = response.code()
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        Log.d("KEK", "Id : ${body.deck.id}")
                        Log.d("KEK", "Title : ${body.deck.title}")
                        Log.d("KEK", "Cards : ${body.deck.cards}")
                        Log.d("KEK", "Code $code")
                    } else {
                        Log.d("KEK", "Code $code")
                    }
                }

                override fun onFailure(call: Call<DeckResponse?>, t: Throwable) {
                    Log.d("KEK", "$t")
                }
            })
        }
    }

    private fun createDeck() {

    }
}
