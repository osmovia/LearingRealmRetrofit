package com.example.learingrealmandretrofit.deck.insidedeck

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.databinding.CardFragmentRecyclerBinding
import com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.InsideDeckCardViewModel
import com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.factory.InsideDeckCardViewModelFactory
import com.google.android.gms.ads.AdRequest

class InsideDeckCardFragment : Fragment() {

    private lateinit var binding: CardFragmentRecyclerBinding
    private lateinit var viewModel: InsideDeckCardViewModel
    private lateinit var viewModelFactory: InsideDeckCardViewModelFactory
    private val arguments: InsideDeckCardFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CardFragmentRecyclerBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        NavigationUI.setupWithNavController(binding.toolbarContainerCard.toolbarId, findNavController())

        binding.adView.loadAd(AdRequest.Builder().build())

        binding.toolbarContainerCard.toolbarId.title = arguments.deckTitle

        binding.toolbarContainerCard.toolbarId.inflateMenu(R.menu.toolbar_rename)

        binding.toolbarContainerCard.toolbarId.setOnMenuItemClickListener { itemMenu ->
            if (itemMenu.itemId == R.id.renameItem) {
                val action = InsideDeckCardFragmentDirections
                    .actionInsideDeckCardFragmentToDialogCreateOrChangeDeck(
                        deckId = arguments.deckId,
                    )
                findNavController().navigate(action)
            }
            true
        }

        // New title if has changed
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(Constants.TITLE_INSIDE_DECK)
            ?.observe(viewLifecycleOwner) { newTitle ->
                binding.toolbarContainerCard.toolbarId.title = newTitle
            }

        viewModelFactory = InsideDeckCardViewModelFactory(
            deckId = arguments.deckId,
            application = requireActivity().application
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(InsideDeckCardViewModel::class.java)

        viewModel.getCardsForDeck()

        viewModel.getAllCardsRealm.observe(viewLifecycleOwner, Observer { allCardDecks ->
            val adapter = RecyclerAdapterCardDeck(this, allCardDecks, true)
            binding.recyclerCard.layoutManager = LinearLayoutManager(context)
            binding.recyclerCard.adapter = adapter
        })

        viewModel.showToast.observe(viewLifecycleOwner, Observer { message ->
            context?.showErrorCodeOrStringResource(message)
        })

        viewModel.showSpinner.observe(viewLifecycleOwner, Observer { showSpinner ->
            if (showSpinner) {
                activity?.showProgress()
            } else {
                activity?.hideProgress()
            }
        })

        val cardDelete = object : SwipeToDelete(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentSwipe = viewModel.getAllCardsRealm.value?.get(viewHolder.absoluteAdapterPosition)
                if (currentSwipe != null) {
                    val action = InsideDeckCardFragmentDirections
                        .actionInsideDeckCardFragmentToInsideDeckCardDeleteDialog(
                            cardDeckId = currentSwipe.id
                        )
                    findNavController().navigate(action)
                    binding.recyclerCard.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(cardDelete)
        itemTouchHelper.attachToRecyclerView(binding.recyclerCard)

        binding.buttonCreateCard.setOnClickListener {
            val action = InsideDeckCardFragmentDirections
                .actionInsideDeckCardFragmentToInsideDeckUpdateCardDialog(
                    card = null,
                    deckId = arguments.deckId,
                )
            findNavController().navigate(action)
        }
    }

    fun onCardClick(card: Card) {
        val action = InsideDeckCardFragmentDirections
            .actionInsideDeckCardFragmentToShowDetailsCardFragment2(cardId = card.id)
        findNavController().navigate(action)
    }
}
