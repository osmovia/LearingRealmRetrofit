package com.example.learingrealmandretrofit.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learingrealmandretrofit.card.viewmodel.AddCardToDeckViewModel
import com.example.learingrealmandretrofit.databinding.DeckFragmentRecyclerBinding
import com.example.learingrealmandretrofit.deck.RecyclerAdapterDeck

class AddCardToDeckFragment : Fragment() {

    private lateinit var viewModel: AddCardToDeckViewModel
    private lateinit var binding: DeckFragmentRecyclerBinding
    private val arguments: AddCardToDeckFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DeckFragmentRecyclerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this).get(AddCardToDeckViewModel::class.java)

        viewModel.gelAllDecksRealm.observe(viewLifecycleOwner, Observer { allDecks ->
            val adapter = RecyclerAdapterDeck(null, allDecks, visibility = true, autoUpdate = true)
            binding.recyclerDeck.layoutManager = LinearLayoutManager(context)
            binding.recyclerDeck.adapter = adapter
        })
    }
}
