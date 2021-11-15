package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.DialogCreateOrUpdateDeckBinding
import com.example.learingrealmandretrofit.objects.Card
import com.example.learingrealmandretrofit.objects.Deck
import com.example.learingrealmandretrofit.objects.DeckRealm

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
        dialog?.setCanceledOnTouchOutside(false)
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
        val title = binding.editTextTitle.text.toString()
        val cardsList = mutableListOf<Card>()
        val cards1 = Card(id = 1, word = "word1", example = "example1", translation = "translation1")
        cardsList.add(cards1)
        val cards2 = Card(id = 2, word = "word2", example = "example2", translation = "translation2")
        cardsList.add(cards2)
        val deck = Deck(
            id = deck!!.id,
            title = title,
            cards = cardsList
        )
    }

    private fun createDeckRetrofit() {

    }
}