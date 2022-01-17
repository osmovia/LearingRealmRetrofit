package com.example.learingrealmandretrofit.deck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.databinding.DeckFragmentRecyclerBinding
import com.example.learingrealmandretrofit.deck.viewmodel.DeckViewModel
import com.example.learingrealmandretrofit.deck.viewmodel.factory.DeckViewModelFactory

class DeckFragment : Fragment() {

    private lateinit var binding: DeckFragmentRecyclerBinding
    private lateinit var viewModelFactory: DeckViewModelFactory
    private lateinit var viewModel: DeckViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DeckFragmentRecyclerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.deckFragment, R.id.cardFragment, R.id.settingFragment))

        NavigationUI.setupWithNavController(binding.toolbarContainerDeck.toolbarId, findNavController(), appBarConfiguration)

        viewModelFactory = DeckViewModelFactory(requireActivity().application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(DeckViewModel::class.java)

        viewModel.pullDecks()
        viewModel.getAllDecksRetrofit()

        viewModel.showSpinner.observe(viewLifecycleOwner, Observer { showSpinner ->
            if (showSpinner) {
                activity?.showProgress()
            } else {
                activity?.hideProgress()
            }
        })

        viewModel.showToast.observe(viewLifecycleOwner, Observer { message ->
            context?.showErrorCodeOrStringResource(message)
        })

        viewModel.gelAllDecksRealm.observe(viewLifecycleOwner, Observer { allDecks ->
            val adapter = RecyclerAdapterDeck(this, allDecks, autoUpdate = true)
            binding.recyclerDeck.layoutManager = LinearLayoutManager(context)
            binding.recyclerDeck.adapter = adapter
        })

        binding.buttonCreateDeck.setOnClickListener {
            val action = DeckFragmentDirections.actionDeckFragmentToDialogCreateOrChangeDeck()
            findNavController().navigate(action)
        }

        val item = object : SwipeToDelete(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentSwipeDeckId = viewModel.gelAllDecksRealm.value?.get(viewHolder.absoluteAdapterPosition)?.id
                if (currentSwipeDeckId != null) {
                    val action = DeckFragmentDirections.actionDeckFragmentToDialogDeleteDeck(
                        currentSwipeDeckId,
                    )
                    findNavController().navigate(action)
                    binding.recyclerDeck.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(binding.recyclerDeck)
    }

    fun onItemClick(deck: Deck) {
        val action = DeckFragmentDirections.actionDeckFragmentToInsideDeckCardFragment(
            deckId = deck.id,
            deckTitle = deck.title,
        )
        findNavController().navigate(action)
    }
}
