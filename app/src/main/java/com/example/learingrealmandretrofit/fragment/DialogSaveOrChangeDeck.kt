package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.DialogCreateOrUpdateDeckBinding
import com.example.learingrealmandretrofit.objects.Card
import com.example.learingrealmandretrofit.objects.Deck
import com.example.learingrealmandretrofit.objects.DeckRealm
import com.example.learingrealmandretrofit.objects.response.DeckResponse
import com.example.learingrealmandretrofit.showErrorCodeToast
import com.example.learingrealmandretrofit.showErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DialogCreateOrChangeDeck : DialogFragment() {

    companion object{
        const val deckRealmKey = "DECK_KEY"
    }

    private val deck: DeckRealm?
        get() = arguments?.getSerializable(deckRealmKey) as? DeckRealm?
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
            if (deck == null) {
                createDeckRetrofit()
            } else {
                updateDeckRetrofit()
            }
        }
    }

    private fun updateDeckRetrofit() {

    }

    private fun createDeckRetrofit() {
        // Test create work new deck
        val card = Card(1, "","","")
        val list: List<Card> = listOf(card)
        val deck = Deck(0,"NewDeck", list)
        BaseApi.retrofit.createdDeck(deck).enqueue(object : Callback<DeckResponse?> {
            override fun onResponse(call: Call<DeckResponse?>, response: Response<DeckResponse?>) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<DeckResponse?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun createDeckRealm() {

    }

    private fun updateDeckRealm() {

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