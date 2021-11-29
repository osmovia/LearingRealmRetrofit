package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.learingrealmandretrofit.databinding.DialogCreateOrUpdateDeckBinding
import com.example.learingrealmandretrofit.objects.Deck

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
    }
}
